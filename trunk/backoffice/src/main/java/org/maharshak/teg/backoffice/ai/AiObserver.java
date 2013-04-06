/**
 * 
 */
package org.maharshak.teg.backoffice.ai;

import java.util.Observable;
import java.util.Observer;

import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.event.EventType;
import org.maharshak.teg.game.event.GameChangedEvent;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.game.event.GameConfigurationChangedEvent;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.player.PlayerToBeData;


/**
 * @author ninel
 *
 */
public class AiObserver implements Observer {

  private final IAIManager _manager;

  public AiObserver(IAIManager manager) {
    _manager = manager;
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

  private void definitionChanged(GameDefinition def, GameConfigurationChangedEvent e) {
    if (e.getType() == EventType.PLAYER_ADDED) {
      PlayerToBeData pl = def.getPlayerData(e.getOwner());
      if(!pl.isHuman()){
        _manager.addAIPlayer(pl.getPerson());
      }
    }
    if (e.getType() == EventType.DEFINITION_LOCKED) {
      def.getGame().addObserver(this);
    }
  }

  private void gameChanged(TegGame g, GameChangedEvent e) {
    if (e.getType() == EventType.PLAYER_STATE_CHANGED) {
      Player pl = e.getPlayer();
      if (!pl.isHuman()) {
        PLAYER_STATE newState = PLAYER_STATE.valueOf(e.get(DataType.NEW_STATE));
        if (newState != PLAYER_STATE.IDLE) {
          _manager.stateChanged(pl, newState);
          if (newState != pl.getState()) {
            System.err.println("Weird , sent state is " + newState + " but real state is " + pl.getState());
          }
        }
      }
    }

  }
}
