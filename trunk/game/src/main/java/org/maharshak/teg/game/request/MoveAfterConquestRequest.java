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
public class MoveAfterConquestRequest extends PlayerRequest {


	private int _amount;

	private Country _destination;
	private Country _source;

	public MoveAfterConquestRequest() {
		// TODO Auto-generated constructor stub
	}

	public MoveAfterConquestRequest(Person p, Country src, Country dst, int amount) {
		super(p);
		_source=src;
		_destination=dst;
		_amount=amount;
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean playerState=p!=null && p.getState()==PLAYER_STATE.MOVE_AFTER_CONQUEST;
		boolean countryParse= _source!=null && _destination!=null ;
		boolean correctConquest = _source == p.getGame().getTurnData()
				.getAttackSource()
				&& _destination == p.getGame().getTurnData().getAttackDst();
		boolean correctArmies = _amount < p.getGame().getArmies(_source)
				&& _amount <= 3;
		boolean valid = playerState&&countryParse&&correctConquest&&correctArmies;
		return valid;

	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {

		Player p = g.getPlayer(getOwner());
		if (_amount > 0) {
			p.getGame().moveTroops(p, _source, _destination, _amount);

		}
    p.setState(PLAYER_STATE.ATAQUE);
		p.getGame().getTurnData().setAttackSource(null);
		p.getGame().getTurnData().setAttackDst(null);

	}

	@Override
	public String human() {
		return "Moved after conquest of "+_destination.getName()+ " "+_amount+" armies";
	}

	@Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean playerState=p!=null && p.getState()==PLAYER_STATE.MOVE_AFTER_CONQUEST;
		boolean countryParse= _source!=null && _destination!=null ;
		boolean correctConquest = _source == p.getGame().getTurnData()
				.getAttackSource()
				&& _destination == p.getGame().getTurnData().getAttackDst();
		boolean correctArmies = _amount < p.getGame().getArmies(_source)
				&& _amount <= 3;
		boolean valid = playerState&&countryParse&&correctConquest&&correctArmies;
		if(valid)
			return "All valid";
		if(!playerState)
			return "Wrong state "+p.getState();
		if(!countryParse)
			return "Wrong src/dst "+_source+"/"+_destination;
		if(!correctConquest)
			return "Not correct conquest (" + _source.getName() + "->"
					+ _destination.getName() + " != "
					+ p.getGame().getTurnData().getAttackSource() + "->"
					+ p.getGame().getTurnData().getAttackDst();
		if(!correctArmies)
			return "Wrong amount of armies" +_amount;
		return super.whyNotValid(g);
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public Country getDestination() {
		return _destination;
	}

	public void setDestination(Country destination) {
		_destination = destination;
	}

	public Country getSource() {
		return _source;
	}

	public void setSource(Country source) {
		_source = source;
	}

}
