package org.maharshak.teg.game.rules;

import org.maharshak.teg.player.Player;

public class TegRuleBook implements RuleBook {

	@Override
	public int armiesPerExchange(int exchangeNumber) {
		if (exchangeNumber < 1)
			return 0;
		switch (exchangeNumber) {
			case 0:
				return 0;
			case 1:
				return 4;
			case 2:
				return 7;
			case 3:
				return 10;
			default:
				return (exchangeNumber - 1) * 5;
		}
	}

	@Override
	public int amountOfArmiesBasedOnOwnedCountries(Player pJ) {
		assert (pJ != null);

		if (pJ.getGame().countriesOf(pJ).size() <= 6)
			return 3;
		else
			return pJ.getGame().countriesOf(pJ).size() / 2;
	}

}
