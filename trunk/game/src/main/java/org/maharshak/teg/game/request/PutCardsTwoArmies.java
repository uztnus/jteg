/**
 * 
 */
package org.maharshak.teg.game.request;

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
@XmlRootElement()
public class PutCardsTwoArmies extends PlayerRequest {

	private Card _card;

	public PutCardsTwoArmies() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param p
	 */
	public PutCardsTwoArmies(Person p, Card t) {
		super(p);
		_card=t;
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean ownsCountry = p.getGame().getOwner(_card.getCountry()) == p;
		boolean ownsCard = p.getGame().getOwner(_card) == p;
		boolean canBeUsed = !p.getGame().isUsed(_card);
		boolean correctGameState = (p.getState().lessThen(PLAYER_STATE.TURNOSTART) || p
				.getState().atLeast(
PLAYER_STATE.REQUEST_CARD));
		return ownsCountry&&canBeUsed&&correctGameState&&ownsCard;


	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		p.getGame().useCard(_card);
	}

	@Override
	public String human() {
		return "Put two armies for card "+_card ;
	}

	public Card getCard() {
		return _card;
	}

	public void setCard(Card card) {
		_card = card;
	}

}
