package org.maharshak.teg.server.tokenaction;


import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.common.ref.TokenAction;
import org.maharshak.teg.server.Console;

public class HelpAction  extends AbstractTokenAction<Data> {

	
	private Console _con;


	@Override
	public TEG_STATUS act(Data d) {
		for(TokenAction  ca:Console.getActions()) {
			_con.write("'%s' %s\n",ca.getToken().getToken(),ca.help());
		}
		return TEG_STATUS.TEG_STATUS_SUCCESS;

	}


	public HelpAction(Console con) {
		super(TOKEN.TOKEN_HELP, "shows help");
		_con=con;
	}

}
