package org.maharshak.teg.server.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;

@XmlRootElement
public class RobotInstructions extends ReadOnlyRequest {

	public RobotInstructions(Person p) {
		super(p);
	}

	@Override
	public String human() {
		return "to remind me what to do";
	}

	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());


		// System.out.println(pJ.getEstado());
		if (p.getGame().getTurnOwner() != p)
			return;

		TegGame game = p.getGame();
		Person fd = p.getPerson();
		switch (p.getState()) {
		case FIRST_PUT:
			Net.net_printf(fd, "%s=%d,%d\n", TOKEN.TOKEN_FICHAS, game.getTurnOwner()
					.getId(), game.getConf().getArmiesInFirstRound());
			break;
		case SECOND_PUT:
			Net.net_printf(fd, "%s=%d,%d\n", TOKEN.TOKEN_FICHAS2, game.getTurnOwner()
					.getId(), game.getConf().getArmiesInFirstRound());
			break;
		case PUT_ARMIES:
		case EXCHANGE:
			game.getTurnOwner().setState(PLAYER_STATE.PUT_ARMIES);
			break;
		case ATAQUE:
		case TURNOSTART:
		case MOVE_AFTER_CONQUEST:
			Net.net_printf(fd, "%s=%d\n", TOKEN.TOKEN_TURNO, game.getTurnOwner()
					.getId());
			break;
		default:
			break;
		}
	}

}
