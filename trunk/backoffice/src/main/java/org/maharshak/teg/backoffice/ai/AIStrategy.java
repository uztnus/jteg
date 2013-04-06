/**
 * 
 */
package org.maharshak.teg.backoffice.ai;

import org.maharshak.teg.backoffice.IGameRunner;
import org.maharshak.teg.player.Player;

/**
 * @author uztnus
 *
 */
public interface AIStrategy {
	
	public void onFirstPut(Player p, IGameRunner runner);
	
	public void onSecondPut(Player p, IGameRunner runner);
	
	public void onAttack(Player p, IGameRunner runner);
	
	public void onRegroup(Player p, IGameRunner runner);
	
	public void onArmyPut(Player p, IGameRunner runner);
}
