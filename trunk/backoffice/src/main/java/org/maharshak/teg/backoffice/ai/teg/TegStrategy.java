/**
 * 
 */
package org.maharshak.teg.backoffice.ai.teg;




import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maharshak.teg.backoffice.IGameRunner;
import org.maharshak.teg.backoffice.ai.AIStrategy;
import org.maharshak.teg.backoffice.ai.Pair;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.request.AbstractPutArmyRequest;
import org.maharshak.teg.game.request.AttackRequest;
import org.maharshak.teg.game.request.CardRequest;
import org.maharshak.teg.game.request.ExchangeCards;
import org.maharshak.teg.game.request.FinishTurnRequest;
import org.maharshak.teg.game.request.InitialPutArmiesRequest;
import org.maharshak.teg.game.request.MoveAfterConquestRequest;
import org.maharshak.teg.game.request.PlayerRequest;
import org.maharshak.teg.game.request.PlayerRequest.RequestState;
import org.maharshak.teg.game.request.PutArmyRequest;
import org.maharshak.teg.game.request.PutCardsTwoArmies;
import org.maharshak.teg.game.request.RegroupRequest;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
public class TegStrategy implements AIStrategy {

	/**
	 * @see org.maharshak.teg.backoffice.ai.AIStrategy#onFirstPut(org.maharshak.teg.player.Player, org.maharshak.teg.server.infra.IGameRunner)
	 */
	@Override
	public void onFirstPut(Player p, IGameRunner runner) {
		int amount = p.getGame().getConf().getArmiesInFirstRound();
		Map<Country, Integer> puts=TegStrategy.whatToPut(p,amount);
		InitialPutArmiesRequest put = new InitialPutArmiesRequest(p.getPerson(),
				puts, PLAYER_STATE.FIRST_PUT);
		runner.addRequest(put, p.getGame());
		put.waitTillProccessed();
	}

	public static List<Country> getSortedCountries(int armiesToPut, Map<Country, Integer> scores, List<Continent> contsOwned) {
		List<Country> res=new ArrayList<Country>();
		int alreadYPut = 0;
		for (Continent continent : contsOwned) {
			for (int i = 0; i < continent.getChips(); i++) {
				Country c = getMaxScore(scores, continent);
				res.add(c);
				// System.out.println(c.getName() + ":" + scores.get(c));
				scores.put(c, scores.get(c) - 2);
				alreadYPut++;
			}
		}

		for (int i = alreadYPut; i < armiesToPut; i++) {
			Country c = getMaxScore(scores, null);
			res.add(c);
			// System.out.println(c.getName() + ":" + scores.get(c));
			scores.put(c, scores.get(c)-2);

		}
		return res;
	}

	private static Country getMaxScore(Map<Country, Integer> scores, Continent continent) {
		Country maxCountry = scores.keySet().iterator().next();
		int max = -1000;
		for (Country c : scores.keySet()) {
			if (continent != null)
				if (c.getContinent() != continent)
					continue;
			if (scores.get(c) > max) {
				maxCountry = c;
				max = scores.get(maxCountry);
			}
		}
		return maxCountry;

	}

	/**
	 * @see org.maharshak.teg.backoffice.ai.AIStrategy#onSecondPut(org.maharshak.teg.player.Player, org.maharshak.teg.server.infra.IGameRunner)
	 */
	@Override
	public void onSecondPut(Player p, IGameRunner runner) {
		int amount = p.getGame().getConf().getArmiesInSecondRound();
		Map<Country, Integer> puts=TegStrategy.whatToPut(p,amount);
		InitialPutArmiesRequest put = new InitialPutArmiesRequest(p.getPerson(),
				puts, PLAYER_STATE.SECOND_PUT);
		runner.addRequest(put, p.getGame());
		put.waitTillProccessed();


	}

	/**
	 * @see org.maharshak.teg.backoffice.ai.AIStrategy#onAttack(org.maharshak.teg.player.Player, org.maharshak.teg.server.infra.IGameRunner)
	 */
	@Override
	public void onAttack(Player p, IGameRunner runner) {
		TegGame game = p.getGame();
		while(true){
			Map<Country,Integer> attackPoints=new HashMap<Country, Integer>();
			Map<Country,Country> attackDestination=new HashMap<Country, Country>();
			for (Country c : game.getBoard().getCountries()) {

				if( game.getOwner(c)== p && game.getArmies( c) > 1){
					Pair<Integer, Country> res = Attack.calculateAttackPoints(c,game);
					if(res.getSecond()!=null){
						attackPoints.put(c, res.getFirst());
						attackDestination.put(c, res.getSecond());
					}
				}
			}

			Country src=maxPoints(attackPoints);
			Country dst=attackDestination.get(src);

			if( src!=null && dst !=null && attackPoints.get(src)  > 0 ) {
				AttackRequest r = new AttackRequest(p.getPerson(), src, dst);
				runner.addRequest(r, game);
				r.waitTillProccessed();
				if(r.isConqueredCountry()&&game.getArmies(src)>1){
					onConqueredCountry(src, dst, p, runner, game);
				}
			}else{
				break;
			}
		}
		game.setState(p, PLAYER_STATE.REGROUP);
	}


	private void onConqueredCountry(Country src, Country dst, Player p,
			IGameRunner runner, TegGame g) {
		int srcI = ArmyPut.calculateCoutryStrategy(src, p, null);
		int dstI = ArmyPut.calculateCoutryStrategy(dst, p, null);

		int diff = dstI-srcI;
		if(diff>0){
			int moveAmount=p.getGame().getArmies(src)-1;
			if(moveAmount>2)
				moveAmount=2;
			if(diff<moveAmount)
				moveAmount=diff;
			MoveAfterConquestRequest req = new MoveAfterConquestRequest(
					p.getPerson(), src, dst, moveAmount);
			runner.addRequest(req, g);
			req.waitTillProccessed();
		}

	}

	public static Country maxPoints(Map<Country, Integer> attackPoints) {
		if(attackPoints.isEmpty()){
			return null;
		}
		Country maxCountry=attackPoints.keySet().iterator().next();

		for (Country c : attackPoints.keySet()) {
			if(attackPoints.get(c)>attackPoints.get(maxCountry))
				maxCountry=c;
		}
		return maxCountry;
	}






	/**
	 * @see org.maharshak.teg.backoffice.ai.AIStrategy#onRegroup(org.maharshak.teg.player.Player, org.maharshak.teg.server.infra.IGameRunner)
	 */
	@Override
	public void onRegroup(Player me, IGameRunner runner) {
		int p=0;
		TegGame g = me.getGame();
		for (Country c : g.getBoard().getCountries()) {
			if (g.getOwner(c) == me) {
				if( Regroup. ai_many_country_enemigo(c, me) == 0 ) {//No enemies
					p = g.getArmies(c) - g.leftForRegruping(c);
					if( p < 2 )
						continue;
					Pair<Country, Integer> res = Regroup.__ai_reagrupe(c,p-1,me);
					RegroupRequest req = new RegroupRequest(me.getPerson(), c,
							res.getFirst(), res.getSecond());
					runner.addRequest(req, me.getGame());
					req.waitTillProccessed();
				}
			}
		}
		CardRequest cardCommand = new CardRequest(me.getPerson());
		if (cardCommand.isValid(g)) {
			runner.addRequest(cardCommand, me.getGame());
			cardCommand.waitTillProccessed();
			if (g.getOwner(cardCommand.getCard().getCountry()) == me) {
				runner.addRequest(new PutCardsTwoArmies(me.getPerson(), cardCommand
.getCard()),
						me.getGame());
			}
		}
		runner.addRequest(new FinishTurnRequest(me.getPerson()), me.getGame());
	}

	/**
	 * @see org.maharshak.teg.backoffice.ai.AIStrategy#onArmyPut(org.maharshak.teg.player.Player, org.maharshak.teg.server.infra.IGameRunner)
	 */
	@Override
	public void onArmyPut(Player p, IGameRunner runner) {
		List<Card> whatCanIExchange = ArmyPut.whatCanIExchange(p.getGame().getCards(p), p);
		if(whatCanIExchange!=null){
			ExchangeCards exchangeReq = new ExchangeCards(p.getPerson(),
					whatCanIExchange.get(0), whatCanIExchange.get(1),
					whatCanIExchange.get(2));
			runner.addRequest(exchangeReq, p.getGame());
			exchangeReq.waitTillProccessed();
		}

		int armiesToPut=ArmyPut.howMuchShouldIPut(p);
		Map<Country, Integer> puts = whatToPut(p,armiesToPut);
		PutArmyRequest put = new PutArmyRequest(p.getPerson(), puts);
		runner.addRequest(put, p.getGame());
		put.waitTillProccessed();
		if (put.getState() != PlayerRequest.RequestState.FINISHED) {
			System.err.println(" Request not finsied!!!");
			AbstractPutArmyRequest.print(puts);
			onArmyPut(p, runner);

		}

	}

	public static Map<Country, Integer> whatToPut(Player p, int armiesToPut) {
		TegGame game = p.getGame();
		Map<Country,Integer> countryScore=new HashMap<Country, Integer>();
		Map<Country, List<String>> explainScore = new HashMap<Country, List<String>>();

		for (Country c : game.getBoard().getCountries()) {
			if(game.getOwner(c)==p){
				countryScore.put(c, ArmyPut.calculateCoutryStrategy(c, p, explainScore));
			}
		}
		if (false)
			printExplanation(countryScore, explainScore);

		List<Country> sorted = getSortedCountries(armiesToPut, countryScore, p.continentsOwned());
		Map<Country, Integer> res=new HashMap<Country, Integer>();
		for (Country country : sorted) {
			if(!res.containsKey(country))
				res.put(country, 1);
			else
				res.put(country, res.get(country)+1);
		}
		return res;


	}

	private static void printExplanation(Map<Country, Integer> countryScore, Map<Country, List<String>> explainScore) {
		List<Country> sorted = sort(countryScore);
		for (Country c : sorted) {
			System.out.print("(" + countryScore.get(c) + ")" + c.getContinent().getName() + "|" + c.getName());
			System.out.print("\t");
			for (String s : explainScore.get(c)) {
				System.out.print("|" + s);
			}
			System.out.print("\n");
		}

	}

	private static List<Country> sort(Map<Country, Integer> countryScore) {
		// Map<Continent, List<Country>> contsToCountries =
		// Board.split(countryScore.keySet());
		// List<Country> res = new ArrayList<Country>();
		// for (Continent cont : contsToCountries.keySet()) {
		// res.addAll(sort(contsToCountries.get(cont), countryScore));
		// }

		return sort(new ArrayList<Country>(countryScore.keySet()), countryScore);
	}

	private static List<Country> sort(List<Country> list, final Map<Country, Integer> countryScore) {
		Collections.sort(list, new Comparator<Country>() {

			@Override
			public int compare(Country o1, Country o2) {
				return countryScore.get(o1).compareTo(countryScore.get(o2));
			}
		});
		return list;
	}

}
