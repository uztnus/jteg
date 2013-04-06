/**
 * 
 */
package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.game.Mission;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
@XmlRootElement
public class FinishTurnRequest extends PlayerRequest {

	public FinishTurnRequest() {
		super(null);
	}

	public FinishTurnRequest(Person p) {
		super(p);
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean turnOwner = p == p.getGame().getTurnOwner();
    boolean correctState = p.getState().atLeast(PLAYER_STATE.TURNOSTART)
        && p.getState().lessThen(PLAYER_STATE.TURN_END);
		boolean valid = turnOwner&&correctState;
		return valid ;
	}

	@Override
	public void runCommand(TegGame g) throws TegRequestException {


		Player p = g.getPlayer(getOwner());
		if(Mission.isMissionAccomplished( p )  ) {
			p.getGame().gameWon(p);
			return;
		}
		p.getGame().getTurnData().init();
		/* clean all the regroups the player could have done */
		p.getGame().clearRegoupingData();
		 
		/* give turn to the next player */
		p.getGame().turnFinished();
	}

	@Override
	public String human() {
		return "Finished turn";
	}

	@Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean turnOwner = p == p.getGame().getTurnOwner();
		boolean correctState=!(p.getState().ordinal() < PLAYER_STATE.TURNOSTART.ordinal()|| 
				p.getState().ordinal() > PLAYER_STATE.TURN_END.ordinal());
		if(!turnOwner)
			return "Wrog turn owner " + p.getName() + "!="
					+ p.getGame().getTurnOwner();
		if(!correctState)
			return "Wrong state "+p.getState();
		return super.whyNotValid(g);
	}


}
