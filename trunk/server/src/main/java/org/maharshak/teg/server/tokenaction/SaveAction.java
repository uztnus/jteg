package org.maharshak.teg.server.tokenaction;


import org.maharshak.teg.common.Common;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.server.Console;


public class SaveAction extends AbstractTokenAction<Data>  {
//	{ ,		con_save,	N_() },
	@Override
	public TEG_STATUS act(Data d) {
		Console.con_text_out_wop(Common.M_INF,"Not yet implemented\n");
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public SaveAction() {
	super(TOKEN.TOKEN_SAVE, "save the game");
	}

}
