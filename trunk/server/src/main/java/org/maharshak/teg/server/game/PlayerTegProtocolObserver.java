package org.maharshak.teg.server.game;



import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TegProtocol;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.GameState;
import org.maharshak.teg.game.Mission;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.event.GameChangedEvent;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.game.event.GameConfigurationChangedEvent;
import org.maharshak.teg.player.ChipsColor;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.player.PlayerToBeData;
import org.maharshak.teg.server.Fow;
import org.maharshak.teg.server.Helper;


public class PlayerTegProtocolObserver implements Observer {


	private Person _p;

  public PlayerTegProtocolObserver(Person p) {
		_p=p;
	}

	/**
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
    if (arg1 instanceof GameChangedEvent) {
      gameChanged((TegGame) arg0, (GameChangedEvent) arg1);
    } else {
      definitionChanged((GameDefinition) arg0, (GameConfigurationChangedEvent) arg1);
    }


	}


  private void definitionChanged(GameDefinition game, GameConfigurationChangedEvent e) {
    PlayerToBeData p = game.getPlayerData(e.getOwner());
		int pid = getIndex(game.getPlayersToBe(), p);
    switch (e.getType()) {
      case COLOUR_SET:
        Net.net_print(_p, String.format("%s=%s,%d,%d\n", TOKEN.TOKEN_NEWPLAYER, p.getName(), pid,
                ChipsColor.getId(p.getColor())));
        break;
      case PLAYER_ADDED:
        if (e.getOwner() == _p)
          sendToPlayer("%s=%s,%d%s\n", TOKEN.TOKEN_PLAYERID.getToken(), p.getName(), pid, e.get(DataType.COLORS));
        break;
      case MISSION_SET:
        if (e.getOwner() == _p)
          Net.net_printf(_p, "%s=%d\n", TOKEN.TOKEN_MISSION, Mission.indexOf(p.getMission()));
        break;
      case DISMISS_PLAYER:
        sendToPlayer(TOKEN.TOKEN_EXIT + "=%d\n", pid);
        break;
      case DEFINITION_LOCKED:
			TegGame game2 = game.getGame();
			game2.addObserver(this);
        break;
      default:
        System.out.println("PO " + _p + ":" + e.getType() + " : " + e.getAll());
        break;
    }

  }

	public static int getIndex(PlayerToBeData[] playersToBe, PlayerToBeData p) {
		for (int i = 0; i < playersToBe.length; i++) {
			if (playersToBe[i] == p)
				return i;
		}
		return -1;
	}

	private void gameChanged(TegGame game, GameChangedEvent e) {
		Player eventPlayer= e.getPlayer();
    Player reciever = game.getPlayer(_p);
		switch (e.getType()) {
			case GAME_STATE_CHANGED:
				GameState state = GameState.valueOf(e.get(DataType.NEW_STATE));
        gameStateChanged(game, state);
				break;
			case EXCHANGE:
				String[] countries = e.get(DataType.CARDS).split(",");
				String cardsIdStrings=TegProtocol.getCountryId(countries[0])+","+TegProtocol.getCountryId(countries[1])+","+TegProtocol.getCountryId(countries[2]);
				sendToPlayer( "%s=%d,%d,%s\n", TOKEN.TOKEN_CANJE,
						eventPlayer.getId(),Integer.parseInt(e.get(DataType.ARMIES)),cardsIdStrings);
				break;
			case GOT_CARD:
				String countryName=e.get(DataType.CARDS);
        if (eventPlayer == reciever)
					sendToPlayer("%s=%d,%d\n", 
							TOKEN.TOKEN_TARJETA, 
							TegProtocol.getCountryId(countryName),
							0);

				break;
			case USED_CARD_FOR_ARMY:
				String cardsCountryName=e.get(DataType.CARDS);
        Country cardsCountry = reciever.getGame().getBoard().getCountry(cardsCountryName);
        Card c = reciever.getGame().getCard(cardsCountry);
				sendToPlayer(
						"%s=%d,%d,%d\n", TOKEN.TOKEN_COUNTRY
						,TegProtocol.getCountryId( c.getCountry()) ,eventPlayer.getId()  ,game.getArmies(c.getCountry()));
				break;
			case MESSAGE:
				String name=eventPlayer.getName();
				sendToPlayer( "%s=%s,%d,\"%s\"\n", TOKEN.TOKEN_MESSAGE, name,eventPlayer.getId(),e.get(DataType.MSG));
				break;
			case MOVE_TROOPS:
        Country srcR = reciever.getGame().getBoard().getCountry(e.get(DataType.SRC));
        Country dstR = reciever.getGame().getBoard().getCountry(e.get(DataType.DST));
        move(srcR, dstR, eventPlayer, game);
				break;
			case LOST_GAME:
				sendToPlayer( "%s=%d\n",TOKEN.TOKEN_LOST, eventPlayer.getId() );
				break;
			case WON_GAME:
				wonGame(eventPlayer);
				break;
			case ATTACK_FINISHED:
				String diceAttacker=e.get(DataType.DICE_ATTACKER);
				String diceDefender=e.get(DataType.DICE_DEFENDER);
        attackFinished(diceAttacker, diceDefender, eventPlayer, game);
				break;
			case ATTACK_STARTED:
        attackStarted(reciever.getGame());
				break;
			case NEW_ROUND:
				if(eventPlayer!=null)
					sendToPlayer( TOKEN.TOKEN_NEW_ROUND+"=%d,%d\n",eventPlayer.getId(), game.getRoundNumber());
				break;
			case PLAYER_STATE_CHANGED:
				PLAYER_STATE pstate=PLAYER_STATE.valueOf(e.get(DataType.NEW_STATE));
        playerStateChanged(pstate, eventPlayer, reciever);
				break;
			case DISMISS_PLAYER:
				sendToPlayer( TOKEN.TOKEN_EXIT+"=%d\n",eventPlayer.getId() );
				break;
			default:
        System.out.println("PO " + reciever.getName() + ":" + e.getType() + " : " + e.getAll());
				break;
		}


	}

  private void playerStateChanged(PLAYER_STATE pstate, Player eventOwner, Player reciever) {
		switch (pstate) {
			case FIRST_PUT:
        sendToPlayer(TOKEN.TOKEN_FICHAS + "=%d,%d\n", reciever.getGame().getTurnOwner().getId(), reciever.getGame().getConf()
            .getArmiesInFirstRound());
				break;
			case SECOND_PUT:
        sendToPlayer(TOKEN.TOKEN_FICHAS2 + "=%d,%d\n", reciever.getGame().getTurnOwner().getId(), reciever.getGame().getConf()
            .getArmiesInSecondRound());
				break;
			case TURNOSTART:
        sendToPlayer(TOKEN.TOKEN_TURNO + "=%d\n", reciever.getGame().getTurnOwner().getId());
				break;
			case PUT_ARMIES:
				int armiesToPut = eventOwner.calculateArmiesToPutNoConts();
				sendToPlayer( TOKEN.TOKEN_FICHASC+"=%d,%d,%d\n",eventOwner.getId(),encodeArmiesPerContinent(eventOwner.continentsOwned(),eventOwner.getGame().getBoard()),armiesToPut );
				break;
			case MOVE_AFTER_CONQUEST:
			  if (eventOwner == reciever) {
          Country srcCon = reciever.getGame().getTurnData().getAttackSource();
          Country dstCon = reciever.getGame().getTurnData().getAttackDst();
          int maxArmiesToPut = reciever.getGame().getArmies(srcCon) - 1; /* cantidad que se pueden pasar */
          if( maxArmiesToPut > 2 )      /* En verdad son 3, pero ya se le paso 1 */
            maxArmiesToPut =2;
          sendToPlayer("%s=%d,%d,%d\n", TOKEN.TOKEN_TROPAS,TegProtocol.getCountryId( srcCon),TegProtocol.getCountryId(dstCon),maxArmiesToPut);
          if( eventOwner.getGame() .getConf().isFogOfWar() ) {
            String buffer = Fow.fow_fill_with_boundaries(dstCon, reciever);
            if(buffer!=null)
              Net.net_printf( _p, "%s\n", buffer );
          }
        }
        break;
			default:
			
				break;
		}

	}

	/**
	 * @param dst 
	 * @param src 
	 * 
	 */
  protected void attackStarted(TegGame g) {

    Country src = g.getTurnData().getAttackSource();
    Country dst = g.getTurnData().getAttackDst();
    sendToPlayer(src, dst, g, "%s=%d,%d\n", TOKEN.TOKEN_ATAQUE, TegProtocol.getCountryId(src), TegProtocol.getCountryId(dst));
	}

  private void attackFinished(String diceAttacker, String diceDefender, Player eventPlayer, TegGame game) {

    Country src = game.getTurnData().getAttackSource();
    Country dst = game.getTurnData().getAttackDst();
      sendToPlayer(src, dst, game, "%s=%d,%s,%d,%s\n", TOKEN.TOKEN_DADOS,
					TegProtocol.getCountryId(src),diceAttacker, TegProtocol.getCountryId(dst),diceDefender  );

    sendToPlayer(src, game, "%s=%d,%d,%d\n", TOKEN.TOKEN_COUNTRY, TegProtocol.getCountryId(src), (game.getOwner(src)).getId(),
        game.getArmies(src));
    sendToPlayer(src, game, "%s=%d,%d,%d\n", TOKEN.TOKEN_COUNTRY, TegProtocol.getCountryId(dst), (game.getOwner(dst)).getId(),
        game.getArmies(dst));

	}

	private void wonGame(Player winner) {
		String strout = Helper.aux_token_stasta(winner.getGame());
		if( strout!=null) {
			sendToPlayer(TOKEN.TOKEN_STATUS.getToken()+"=%s\n",strout);
		}
		/* send who is the winner */
		if( winner!=null )
			sendToPlayer( TOKEN.TOKEN_WINNER.getToken()+"=%d,%d\n",winner.getId(),winner.getMission().getType().ordinal() );
	}

  private void move(Country src, Country dst, Player p, TegGame game) {
    sendToPlayer(src, game, "%s=%d,%d,%d\n", TOKEN.TOKEN_COUNTRY, TegProtocol.getCountryId(src), (game.getOwner(src)).getId(),
        game.getArmies(src));
    sendToPlayer(dst, game, "%s=%d,%d,%d\n", TOKEN.TOKEN_COUNTRY, TegProtocol.getCountryId(dst), (game.getOwner(dst)).getId(),
        game.getArmies(dst));
	}


	/**
	 * @param e 
	 * @param game 
	 * @param state
	 */
  private void gameStateChanged(TegGame game, GameState state) {
		switch (state) {
			case FIRST_PUT:
				Net.net_printf( _p, prepareBeginGameString(game));
				break;
			case SECOND_PUT:
				sendToPlayer( TOKEN.TOKEN_FICHAS2+"=%d,%d\n",game.getTurnOwner().getId(),game.getConf().getArmiesInSecondRound());
				break;
			case ATTACK:
				sendToPlayer(TOKEN.TOKEN_TURNO+"=%d\n",game.getTurnOwner().getId());
			default:
				break;
		}
	}

	private void sendToPlayer(String string, Object ...args ) {
		Net.net_printf(_p,string, args);

	}

  private void sendToPlayer(Country c, TegGame g, String string, Object... args) {
    if (g.getConf().isFogOfWar()) {
      if (!Fow.canSeeCountry(g.getPlayer(_p), c))
        return;
    }
    Net.net_printf(_p, string, args);

  }

  private void sendToPlayer(Country c1, Country c2, TegGame g, String string, Object... args) {
    if (g.getConf().isFogOfWar()) {
      if (!Fow.canSeeCountry(g.getPlayer(_p), c1) || !Fow.canSeeCountry(g.getPlayer(_p), c2))
        return;
    }
    Net.net_printf(_p, string, args);
  }

  private static String prepareBeginGameString(TegGame game) {
		String strout=Helper.aux_token_stasta(game );

		String res = String.format( "%s=%s;%s=%d,%d;%s=%d,%d,%d,%d;%s=%d,%d\n"
				,TOKEN.TOKEN_START
				,strout			/* available players */
				,TOKEN.TOKEN_NEW_ROUND
				,game.getTurnOwner().getId() 	/* who starts the new turn */
				,game.getRoundNumber()	/* the round number */
    , TOKEN.TOKEN_MODALIDAD, game.getConf().hasSecretMission() ? 1 : 0 /*
                                                                       * play
                                                                       * with
                                                                       * missions
                                                                       * ?
                                                                       */
    , game.getConf().hasCommonMission() ? 1 : 0 /* play with common mission */
								,game.getConf().isFogOfWar() ?1:0	/* play with fog of war */
    , game.getConf().getRules().ordinal() /* which rules ? */
										,TOKEN.TOKEN_FICHAS
										,game.getTurnOwner().getId(),	/* who starts ? */
										game.getConf().getArmiesInFirstRound() );	/* how many armies to place */
		return res;
	}

	public static int encodeArmiesPerContinent(List<Continent> contsPlayerHas, Board board) {
		int res=0;
		for (Continent cont : contsPlayerHas) {
			res+=Math.pow(2, TegProtocol.getContId(cont));
		}
		return res;
	}

}
