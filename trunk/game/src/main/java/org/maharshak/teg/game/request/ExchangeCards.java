/**
 * 
 */
package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
@XmlRootElement
public class ExchangeCards extends PlayerRequest {


	@XmlElement(name = "c1")
	private final Card _card1;
	@XmlElement(name = "c2")
	private final Card _card2;
	@XmlElement(name = "c3")
	private final Card _card3;
	
	@XmlElement(name = "armies")
	private int _armies=-1;

	public ExchangeCards() {
		_card1 = _card2 = _card3 = null;
	}

	/**
	 * @param p
	 * @param result 
	 */
	public ExchangeCards(Person p, Card c1, Card c2, Card c3) {
		super(p);
		_card1=c1;
		_card2=c2;
		_card3=c3;
	}


	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctStatus = p.getState() == PLAYER_STATE.PUT_ARMIES;
		boolean doOwn = p.getGame().doOwnCards(p, _card1, _card2, _card3);
		boolean canExcahnge=Card.canExchangeBePerformed( _card1,_card2, _card3);
		return correctStatus&&canExcahnge&&doOwn;
	}




	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		p.setState(PLAYER_STATE.EXCHANGE);
		_armies = p.getGame().performExchange(p, _card3, _card2, _card1);
	}

	@Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctStatus = p.getState() == PLAYER_STATE.PUT_ARMIES;
		boolean canExcahnge=Card.canExchangeBePerformed( _card1,_card2, _card3);
		if(!correctStatus)
			return "Wrong state (!fichasC)" + p.getState();
		if(canExcahnge)
			return "Cant perform the actuall exchange";
		return super.whyNotValid(g);
	}



	@Override
	public String human() {
		return "Performed exchange of "+_card1 +","+_card2 +","+_card3 +" for "+_armies+" armies";
		
	}

}
