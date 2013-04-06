package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.player.Player;


public class TestAction extends AbstractTokenAction <Data> {

	
	private static TEG_STATUS doPlayer(Player pJ) {
		System.out.println(String.format("Nombre: %s\n",pJ.getName()));
		System.out.println(String.format("fd: %d\n",pJ.getPerson()));
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	@Override
	public TEG_STATUS act(Data d) {

    // for (Player p : ((GameManager)d.getGame()).getPlayerList()) {
    // doPlayer(p);
    // }

		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public TestAction() {
	super(TOKEN.TOKEN_TEST, "internal use. Dont use it");
    throw new UnsupportedOperationException("Currently not supported");
	}
	

}
