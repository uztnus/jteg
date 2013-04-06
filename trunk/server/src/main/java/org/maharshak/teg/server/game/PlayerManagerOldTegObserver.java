/**
 * 
 */
package org.maharshak.teg.server.game;

import java.io.IOException;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.event.GameChangedEvent.DataType;
import org.maharshak.teg.game.event.GameConfigurationChangedEvent;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.PlayerToBeData;


/**
 * @author uztnus
 *
 */
public class PlayerManagerOldTegObserver implements Observer{

	private Set<Person> _people=new HashSet<Person>();
	

	
	@Override
	public void update(Observable arg0, Object arg1) {
    if (!(arg0 instanceof GameDefinition))
      return;
    GameDefinition def = (GameDefinition) arg0;
    GameConfigurationChangedEvent e = (GameConfigurationChangedEvent) arg1;
		switch (e.getType()) {
		case PLAYER_ADDED:
        playerAdded(def, e);
			break;
		case DISMISS_PLAYER:
        playerDissmised(def, e);
			break;
		default:
			break;
		}

	}

  private void playerDissmised(GameDefinition def, GameConfigurationChangedEvent e) {
    PlayerToBeData player = def.getPlayerData(e.getOwner());
		if((player).isHuman()){
			
      String reason = e.get(DataType.REASON);

      TOKEN t = null;
      if (reason.compareTo("server full") == 0)
        t = TOKEN.TOKEN_SERVERFULL;
      else if (reason.compareTo("game in progress") == 0) {
        t = TOKEN.TOKEN_GAMEINPROGRESS;
      } else {
        t = null;
      }
      removePerson(e.getOwner());
      Net.net_print(e.getOwner(), t + "\n");
			try {
        Net.net_close(e.getOwner());
			} catch (IOException err) {
        System.err.println("Failed to close (" + e.getOwner() + ") due to " + err);
			}
		}
	}

  private void playerAdded(GameDefinition g, GameConfigurationChangedEvent e) {

    if (personHasObserver(e.getOwner()))
      return;
    PlayerToBeData player = g.getPlayerData(e.getOwner());
		if((player).isHuman()){
			
      g.addObserver(new PlayerTegProtocolObserver(e.getOwner()));
			addPerson(player.getPerson());
		}
	}

	private void removePerson(Person p) {
		_people.remove(p);
	}

	private void addPerson(Person person) {
		_people.add(person);
	}

	private boolean personHasObserver(Person p) {
		return _people.contains(p);
	}

}
