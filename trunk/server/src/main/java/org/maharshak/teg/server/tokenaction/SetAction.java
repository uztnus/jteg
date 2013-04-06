package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;


public class SetAction extends AbstractTokenAction <Data> {
	//		{ ,		con_set,	N_() },
	@Override
	public TEG_STATUS act(Data d) {
//		return Options.option_parse(d.getPerson(),d.getVal(),((GameManager)d.getGame()));
    return TEG_STATUS.TEG_STATUS_ERROR;
	}

	public SetAction() {
		super(TOKEN.TOKEN_SET, "sets options");
    throw new UnsupportedOperationException("Currently not supported");
	}

}
