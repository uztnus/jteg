package org.maharshak.teg.server.game;

import java.util.Observable;
import java.util.Observer;

import org.maharshak.teg.common.Common;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.GameState;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.event.GameChangedEvent;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.game.event.GameConfigurationChangedEvent;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.player.PlayerToBeData;
import org.maharshak.teg.server.Console;


public class ConcoleObserver implements Observer{

	@Override
	public void update(Observable arg0, Object arg1) {
    if (arg1 instanceof GameChangedEvent) {
      gameChanged((TegGame) arg0, (GameChangedEvent) arg1);
    } else {
      definitionChanged((GameDefinition) arg0, (GameConfigurationChangedEvent) arg1);
    }
	}

  private void definitionChanged(GameDefinition def, GameConfigurationChangedEvent e) {
    PlayerToBeData p = def.getPlayerData(e.getOwner());
    switch (e.getType()) {
      case PLAYER_ADDED:
        if (e.get(DataType.PERSON_UUID) != null)
          Console.con_text_out(Common.M_INF,
 String.format(
						"Player %s(%d) is connected from %s\n", p.getName(),
						PlayerTegProtocolObserver.getIndex(def.getPlayersToBe(), p), p
								.getPerson().getAddr()));
        break;
      case COLOUR_SET:
        Console.con_text_out(Common.M_INF,
 String.format(
					"Player %s(%d) has color %s\n", p.getName(),
					PlayerTegProtocolObserver.getIndex(def.getPlayersToBe(), p), p
							.getColor().getName()));
        break;
      case DISMISS_PLAYER:
        if (p == null)
          break;
        String name = "NoName";
        if (p.getName() != null)
          name = p.getName();
			Console.con_text_out(Common.M_INF, String.format(
					"Player %s(%d) quit the game\n", name,
					PlayerTegProtocolObserver.getIndex(def.getPlayersToBe(), p)));
        break;
      case DEFINITION_LOCKED:
        def.getGame().addObserver(this);
        break;
      default:
        break;
    }

  }

  private void gameChanged(TegGame g, GameChangedEvent e) {
    Player eventPlayer = e.getPlayer();
    switch (e.getType()) {
      case EXCHANGE:
        Console.con_text_out(Common.M_INF, String.format("Player %s(%d) performed exchange , and has %d new armies \n",
            eventPlayer.getName(), eventPlayer.getId(), Integer.parseInt(e.get(DataType.ARMIES))));
        break;
      case MESSAGE:
        Console.con_text_out(Common.M_INF,
            String.format("Player %s(%d) sent a message: %s \n", eventPlayer.getName(), eventPlayer.getId(), e.get(DataType.MSG)));
        break;
      case LOST_GAME:
        Console.con_text_out(Common.M_INF, String.format("Player %s(%d) lost the game\n", eventPlayer.getName(), eventPlayer.getId()));
        break;
      case WON_GAME:
        Console.con_text_out(Common.M_INF,
            String.format("Player %s(%d) is the winner! Game Over\n", eventPlayer.getName(), eventPlayer.getId()));
        break;
      case GAME_STATE_CHANGED:
        switch (GameState.valueOf(e.get(DataType.NEW_STATE))) {
          case FIRST_PUT:
            Console.con_text_out(Common.M_INF, String.format("Starting game number: %d with seed: %d , uuid=%s\n", g.getGameNumber(), g
                .getGameID().getLeastSignificantBits(), g.getGameID().toString()));
            Console.con_text_out(Common.M_INF, g.getConf().toPrettyPrint());
            break;

          default:
            break;
        }
        break;
      case NEW_ROUND:
        Console.con_text_out(Common.M_INF, String.format("Starting round : %d starter: %s\n", g.getRoundNumber(), g.getTurnOwner()));
        break;
      default:
        break;
    }

  }

}
