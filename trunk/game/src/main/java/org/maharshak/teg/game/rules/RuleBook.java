package org.maharshak.teg.game.rules;

import java.io.Serializable;

import org.maharshak.teg.player.Player;

public interface RuleBook extends Serializable {

	/**
	 * amount of countries given to player on each exhange
	 * 
	 * @param exchangeNumber
	 * @return
	 */
	public int armiesPerExchange(int exchangeNumber);

	/**
	 * number of countries given to player
	 * 
	 * @param pJ
	 * @return
	 */
	public int amountOfArmiesBasedOnOwnedCountries(Player pJ);

}
