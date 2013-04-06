package org.maharshak.teg.server.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;

@XmlRootElement
public class ShowRoundData extends ReadOnlyRequest {

	public ShowRoundData(Person p) {
		super(p);
	}

	@Override
	public String human() {
		return "to know who started the round, and the round number";
	}

	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());

		Net.net_printf(getOwner(), "%s=%d,%d\n", TOKEN.TOKEN_NEW_ROUND,
 p
				.getGame().getTurnOwner()
				.getId() /*
																					 * who starts the new turn
																					 */
		, p.getGame().getRoundNumber() /* the round number */
		);

	}

}
