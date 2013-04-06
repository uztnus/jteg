package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;

@XmlRootElement
public class SurrenderRequest extends PlayerRequest {

	public SurrenderRequest(Person p) {
		super(p);
	}

	public SurrenderRequest() {
	}

	@Override
	public String human() {
		return "Plauer wants to surrender";
	}

	@Override
	protected boolean isValid(TegGame g) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		// TODO Auto-generated method stub
		// Player pJ= game.getPlayerfd( fd);
		//
		//
		// if( pJ ==null){
		// Net.net_print(fd,TOKEN.TOKEN_ERROR+"="+TOKEN.TOKEN_SURRENDER+"\n");
		// return TEG_STATUS.TEG_STATUS_PARSEERROR;
		// }
		//
		// if( !pJ.isPlaying() ){
		// Net.net_print(fd,TOKEN.TOKEN_ERROR+"="+TOKEN.TOKEN_SURRENDER+"\n");
		// return TEG_STATUS.TEG_STATUS_PARSEERROR;
		// }
		//
		// if( pJ.getState().ordinal() <
		// PLAYER_STATUS.PLAYER_STATUS_HABILITADO.ordinal() ){
		// Net.net_print(fd,TOKEN.TOKEN_ERROR+"="+TOKEN.TOKEN_SURRENDER+"\n");
		// return TEG_STATUS.TEG_STATUS_PARSEERROR;
		// }
		//
		// Console.con_text_out(Common.M_INF,
		// String.format("Player %s(%d) abandoned the game\n",pJ.getName(),pJ.getNumjug()));
		//
		// game.netall_printf("%s=%d\n", TOKEN.TOKEN_SURRENDER, pJ.getNumjug() );
		//
		// game.player_del_soft( pJ );
		//
		// return TEG_STATUS.TEG_STATUS_SUCCESS;
		throw new TegRequestException("Not yet supported");
	}

}
