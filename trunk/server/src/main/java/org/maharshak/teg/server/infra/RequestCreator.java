package org.maharshak.teg.server.infra;

import java.util.HashMap;
import java.util.Map;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TegProtocol;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.request.AttackRequest;
import org.maharshak.teg.game.request.CardRequest;
import org.maharshak.teg.game.request.ExchangeCards;
import org.maharshak.teg.game.request.FinishTurnRequest;
import org.maharshak.teg.game.request.InitialPutArmiesRequest;
import org.maharshak.teg.game.request.MessageRequest;
import org.maharshak.teg.game.request.MoveAfterConquestRequest;
import org.maharshak.teg.game.request.PlayerRequest;
import org.maharshak.teg.game.request.PutArmyRequest;
import org.maharshak.teg.game.request.PutCardsTwoArmies;
import org.maharshak.teg.game.request.RegroupRequest;
import org.maharshak.teg.game.request.StartGameRequest;
import org.maharshak.teg.game.request.SurrenderRequest;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.server.request.GameStatusRequest;
import org.maharshak.teg.server.request.MissionRequest;
import org.maharshak.teg.server.request.RequestConteiner;
import org.maharshak.teg.server.request.RobotInstructions;
import org.maharshak.teg.server.request.ShowCards;
import org.maharshak.teg.server.request.ShowCountries;
import org.maharshak.teg.server.request.ShowRoundData;
import org.maharshak.teg.server.request.ShowScores;


public class RequestCreator {

	private GameDefinition _gameDefinition;


	public RequestCreator(GameDefinition gd) {
		_gameDefinition = gd;
	}


	public PlayerRequest create(RequestConteiner c) throws TegRequestException {
		Person p = c.getPerson();

		switch (c.getToken()) {
		case TOKEN_FICHAS:
			return createInitialPutRequest(p, c, PLAYER_STATE.FIRST_PUT);
		case TOKEN_FICHAS2:
			return createInitialPutRequest(p, c, PLAYER_STATE.SECOND_PUT);
		case TOKEN_FICHASC:
			return createPutArmiesRequest(c, p);
		case TOKEN_TURNO:
			return new FinishTurnRequest(p);

		case TOKEN_MISSION:
			return new MissionRequest(p);
		case TOKEN_START:
				return new StartGameRequest(p);
		case TOKEN_CANJE:
			return createNewCardExchangeCommand(c, p);
		case TOKEN_TARJETA:
			return new CardRequest(p);
		case TOKEN_EJER2:
			int cardsCountryId = Integer.parseInt(c.getValue());
			Country cardsCountry = TegProtocol.getCountry(cardsCountryId,
					_gameDefinition.getGame().getBoard());
			return new PutCardsTwoArmies(p, _gameDefinition.getGame().getCard(
					cardsCountry));
		case TOKEN_TROPAS:
			return createMoveAfterConquestRequest(p, c);
		case TOKEN_MESSAGE:
			return new MessageRequest(p, c.getValue());
		case TOKEN_REAGRUPE:
			return createReagroupRequest(p, c);
		case TOKEN_ATAQUE:
			return createAttackRequest(p, c);
		case TOKEN_SURRENDER:
			return new SurrenderRequest(p);
		case TOKEN_ENUM_CARDS:
			return new ShowCards(p);
		case TOKEN_SCORES:
			return new ShowScores(p);
		case TOKEN_COUNTRIES:
			return new ShowCountries(p, Integer.parseInt(c.getValue()));
		case TOKEN_NEW_ROUND:
			return new ShowRoundData(p);
		case TOKEN_LOQUE:
			return new RobotInstructions(p);
		case TOKEN_STATUS:
			return new GameStatusRequest(p);
		default:
			throw new TegRequestException("Unknown token!!!");
		}

	}





	private AttackRequest createAttackRequest(Person p, RequestConteiner c) {
		String[] tmp=c.getValue().split(",");
		int src = Integer.parseInt( tmp[0] );		
		int dst = Integer.parseInt( tmp[1]);		
		Country countrySrc = TegProtocol.getCountry(src, _gameDefinition
				.getGame().getBoard());
		Country countryDst = TegProtocol.getCountry(dst, _gameDefinition
				.getGame().getBoard());
		return new AttackRequest(p,countrySrc,countryDst);
	}


	private PlayerRequest createReagroupRequest(Person p, RequestConteiner c) {
		String[] tmp=c.getValue().split(",");

		int src = Integer.parseInt( tmp[0]);
		int dst = Integer.parseInt( tmp[1] );
		int cant = Integer.parseInt( tmp[2] );

		Country countrySrc = TegProtocol.getCountry(src, _gameDefinition
				.getGame().getBoard());
		Country countryDst = TegProtocol.getCountry(dst, _gameDefinition
				.getGame().getBoard());

		return new RegroupRequest(p, countrySrc, countryDst, cant);
	}


	private MoveAfterConquestRequest createMoveAfterConquestRequest(Person p,
			RequestConteiner c) {
		String[] tmp=c.getValue().split(",");

		int src = Integer.parseInt( tmp[0] );		
		int dst = Integer.parseInt( tmp[1] );		
		int amount = Integer.parseInt( tmp[2] );		
		Country countrySrc = TegProtocol.getCountry(src, _gameDefinition
				.getGame().getBoard());
		Country countryDst = TegProtocol.getCountry(dst, _gameDefinition
				.getGame().getBoard());
		return new MoveAfterConquestRequest(p, countrySrc, countryDst, amount);
	}


	private ExchangeCards createNewCardExchangeCommand(RequestConteiner c,
			Person p) {
		String tmp[]=c.getValue().split(",");

		int t1 = Integer.parseInt( tmp[0] );		
		int t2 = Integer.parseInt( tmp[1] );		
		int t3 = Integer.parseInt( tmp[2] );	
		Country c1 = TegProtocol.getCountry(t1, _gameDefinition.getGame()
				.getBoard());
		Country c2 = TegProtocol.getCountry(t2, _gameDefinition.getGame()
				.getBoard());
		Country c3 = TegProtocol.getCountry(t3, _gameDefinition.getGame()
				.getBoard());

		/* se puede hacer el canje ? */
		Card card1 = _gameDefinition.getGame().getCard(c1);
		Card card2 = _gameDefinition.getGame().getCard(c2);
		Card card3 = _gameDefinition.getGame().getCard(c3);

		return new ExchangeCards(p, card1, card2, card3);
	}




	private PlayerRequest createPutArmiesRequest(RequestConteiner c, Person p)
			throws TegRequestException {

		Map<Country, Integer> armies = parseArmiesToPutMap(c.getValue(),
				_gameDefinition.getGame().getBoard());
		PutArmyRequest request=new PutArmyRequest( p,armies);
		return request;
	}


	private PlayerRequest createInitialPutRequest(Person p, RequestConteiner c,
			PLAYER_STATE state) throws TegRequestException {
		Map<Country, Integer> armies = parseArmiesToPutMap(c.getValue(),
				_gameDefinition.getGame().getBoard());
		InitialPutArmiesRequest request = new InitialPutArmiesRequest(p, armies,
				state);
		return request;
	}

	private static TOKEN[] DONT_CARE_TOKENS={TOKEN.TOKEN_COUNTRIES,TOKEN.TOKEN_LOQUE,TOKEN.TOKEN_CVERSION,TOKEN.TOKEN_SVERSION,TOKEN.TOKEN_STATUS,TOKEN.TOKEN_PVERSION};
	private static boolean iCare(TOKEN token) {
		for (TOKEN t : DONT_CARE_TOKENS) {
			if(t==token)
				return false;
		}
		return true;
	}


	private static Map<Country, Integer> parseArmiesToPutMap(String str, Board b) throws TegRequestException {
		if( str.length()==0 )
			throw new TegRequestException("Failed parse str - its empty");

		String[] data = str.split(",");
		Map<Country, Integer> res=new HashMap<Country, Integer>();
		for (String one : data) {
			String[] tmp = one.split(":");
			int countryId = Integer.parseInt( tmp[0] );		
			int armiesToPut = Integer.parseInt( tmp[1] );		
			Country cc = TegProtocol.getCountry(countryId, b) ;
			if( cc==null  || armiesToPut<0 ) {
				throw new TegRequestException("Failed to parse "+str+" cc="+cc+" armiesToPut="+armiesToPut);
			}
			res.put(cc, armiesToPut);
		}
		return res;
	}




}
