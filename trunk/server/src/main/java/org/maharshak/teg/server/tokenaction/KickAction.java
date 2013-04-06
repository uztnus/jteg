package org.maharshak.teg.server.tokenaction;




import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;


public class KickAction  extends AbstractTokenAction<Data>  {
	//	{ ,		con_kick,	N_() },
	@Override
	public TEG_STATUS act(Data d) {
    // String name = d.getVal();
    // GameManager game = ((GameManager)d.getGame());
    // if( name!=null && !name.isEmpty() ) {
    //
    // Player pJ=game.getPlayer(name);
    // if( pJ!=null) {
    // game.player_del_hard( pJ );
    // } else
    // Console.con_text_out_wop(Common.M_ERR,String.format("Player %s was not found\n",name));
    // }
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}
	public KickAction() {
		super(TOKEN.TOKEN_KICK, "kick player from the game");
    throw new UnsupportedOperationException("Currently not supported");
	}


}
