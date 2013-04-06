package org.maharshak.teg.server.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.server.Helper;

@XmlRootElement
public class GameStatusRequest extends ReadOnlyRequest {

	public GameStatusRequest(Person p) {
		super(p);
	}

	@Override
	public String human() {
		return "shows the status of the players";
	}

	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());

		String strout = Helper.aux_token_stasta(p.getGame());
		Net.net_printf(p.getPerson(), "%s=%s\n", TOKEN.TOKEN_STATUS, strout);

	}

}
