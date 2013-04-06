/**
 * 
 */
package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
@XmlRootElement
public class RegroupRequest extends PlayerRequest {

	private Country _src;
	private Country _dst;
	private int _amount;

	public RegroupRequest() {
		// TODO Auto-generated constructor stub
	}

	public RegroupRequest(Person p, Country src, Country dst, int amount) {
		super(p);
		_src=src;
		_dst=dst;
		_amount=amount;
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctState=p!=null && 
				p.isAtLeastAt(PLAYER_STATE.TURNOSTART) &&
 p.getState().lessEqualThen(PLAYER_STATE.REGROUP);
		boolean countriesOk=_src!=null && _dst!=null &&
 p.getGame().getOwner(_src) == p
				&& p.getGame().getOwner(_dst) == p
				&& p.getGame().getBoard().haveBorders(_src, _dst);
		int leftToRegroup = p.getGame().getArmies(_src)
				- p.getGame().leftForRegruping(_src) - 1;
		boolean amountOk= _amount<=leftToRegroup;
		boolean isValid = correctState&&countriesOk&&amountOk;
		return isValid;

	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		if (p.getState() != PLAYER_STATE.REGROUP) {
      p.setState(PLAYER_STATE.REGROUP);
    }
		p.getGame().moveTroops(p, _src, _dst, _amount);
		p.getGame().setLeftForRegrouping(_dst,
				p.getGame().leftForRegruping(_dst) + _amount);
	}

	@Override
	public String human() {
		return "Regrouping "+_src.getName()+"--("+_amount+")->"+_dst.getName();
	}


	@Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctState = p != null && p.isAtLeastAt(PLAYER_STATE.TURNOSTART)
				&& p.getState().ordinal() <= PLAYER_STATE.REGROUP.ordinal();

		boolean countriesOk=_src!=null && _dst!=null &&
 p.getGame().getOwner(_src) == p && p.getGame().getOwner(_dst) == p
				&& p.getGame().getBoard().haveBorders(_src, _dst);
		int leftToRegroup = p.getGame().getArmies(_src)
				- p.getGame().leftForRegruping(_src) - 1;
		boolean amountOk= _amount<=leftToRegroup;
		if(!correctState)
			return "Requester " + p + " is at wrong state " + p.getState()
					+ " current turn owner is " + p.getGame().getTurnOwner();
		if(!countriesOk)
			return "Wrong countries Src:" + _src.getName() + " owned by "
					+ p.getGame().getOwner(_src).getName() + " Dst:" + _dst.getName()
					+ " owned by " + p.getGame().getOwner(_dst).getName();
		if(!amountOk)
			return "Left to regroup "+leftToRegroup +" but requested amount is "+_amount;

		return super.whyNotValid(g);
	}

	public Country getSrc() {
		return _src;
	}

	public void setSrc(Country src) {
		_src = src;
	}

	public Country getDst() {
		return _dst;
	}

	public void setDst(Country dst) {
		_dst = dst;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

}
