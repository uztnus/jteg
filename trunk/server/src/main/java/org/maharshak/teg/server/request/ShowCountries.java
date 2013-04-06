package org.maharshak.teg.server.request;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TegProtocol;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.server.Fow;

public class ShowCountries extends ReadOnlyRequest {

	private int _forWho;

	public ShowCountries(Person p, int forWho) {
		super(p);
		_forWho = forWho;
	}

	@Override
	public String human() {
		return "It shows info about the countries";
	}

	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());

		Person fd = p.getPerson();

		Player pJ = g.getPlayer(_forWho);
		if (_forWho != -1 && pJ == null) {
			Net.net_print(fd, TOKEN.TOKEN_ERROR + "=" + TOKEN.TOKEN_COUNTRIES + "\n");
			throw new TegRequestException("Requesting player doesn't exist");
		}

		Player requester = g.getPlayer(fd);
		String strout;
		if (_forWho != -1) {

			/* ask just for 1 player */
			strout = printCountries(pJ, requester);
			if (strout == null) {
				Net.net_print(fd, TOKEN.TOKEN_ERROR + "=" + TOKEN.TOKEN_COUNTRIES
						+ "\n");
				throw new TegRequestException("Failed to print countries");
			}
			Net.net_printf(fd, "%s=%d/%s\n", TOKEN.TOKEN_COUNTRIES, pJ.getId(),
					strout);
		} else {
			/* ask for all the players */

			for (Player toPrint : g.getPlayerList()) {
				strout = printCountries(toPrint, requester);
				if (strout == null) {
					Net.net_print(fd, TOKEN.TOKEN_ERROR + "=" + TOKEN.TOKEN_COUNTRIES
							+ "\n");
					throw new TegRequestException("Failed to print countries");
				}
				Net.net_printf(fd, "%s=%d/%s\n", TOKEN.TOKEN_COUNTRIES,
						toPrint.getId(), strout);
			}

			if (g.getConf().isFogOfWar()) {

				strout = printNonSeenCountries(requester);
				if (strout != null)
					Net.net_printf(fd, "%s=%d/%s\n", TOKEN.TOKEN_COUNTRIES, -1, strout);
			}
		}

	}

	private static String printCountries(Player toPrint, Player requester) {
		String buf = "";
		TegGame game = requester.getGame();
		List<Country> countries = game.countriesOf(toPrint);
		Collections.sort(countries, TegProtocol.COMP);
		boolean notFoW = !game.getConf().isFogOfWar();
		for (Country country : countries) {
			if (notFoW
					|| (game.getConf().isFogOfWar() && Fow.canSeeCountry(requester,
							country))) {
				buf += String.format(",%d:%d", TegProtocol.getCountryId(country),
						game.getArmies(country));

			}
		}
		if (!buf.isEmpty())
			return buf.substring(1);
		return buf;

	}

	/* special case where I want to send all countries that he CAN'T see */
	private static String printNonSeenCountries(Player p) {

		// boolean n=false;
		String buf = "";
		List<Country> countries = new ArrayList<Country>(p.getGame().getBoard()
				.getCountries());
		Collections.sort(countries, TegProtocol.COMP);
		for (Country c : countries) {
			if (!Fow.canSeeCountry(p, c)) {
				buf += String.format(",%d:%d", TegProtocol.getCountryId(c), -1);
			}
		}

		if (!buf.isEmpty())
			return buf.substring(1);
		return buf;

	}

}
