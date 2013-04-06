package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;


public class ViewAction extends AbstractTokenAction<Data>{
	public ViewAction() {
		super(TOKEN.TOKEN_VIEW, "view options");
    throw new UnsupportedOperationException("Currently not supported");
	}

	@Override
	public TEG_STATUS act(Data d) {
    // return
    // Options.option_view(d.getPerson(),d.getVal(),((GameManager)d.getGame()));
    return TEG_STATUS.TEG_STATUS_ERROR;
		}
}
