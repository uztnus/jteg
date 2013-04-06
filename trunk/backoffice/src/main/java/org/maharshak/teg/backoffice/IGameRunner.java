/**
 * 
 */
package org.maharshak.teg.backoffice;

import java.util.List;

import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.request.PlayerRequest;


/**
 * @author uztnus
 *
 */
public interface IGameRunner {
	
	public void init();
	
	public void addRequest(PlayerRequest command, TegGame g);

	public void stop();

	public void runCommands(List<PlayerRequest> commands, TegGame g);
  


}
