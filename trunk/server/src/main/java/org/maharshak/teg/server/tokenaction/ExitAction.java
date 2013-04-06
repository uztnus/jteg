package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.game.GameManager;


public class ExitAction  extends AbstractTokenAction<Data> {

	@Override
	public TEG_STATUS act(Data d) {
		//((Game)d.getGame()).player_flush();
		return TEG_STATUS.TEG_STATUS_GAMEOVER;
	}


	public ExitAction() {
		super(TOKEN.TOKEN_EXIT,"exits the game");

	}
}
