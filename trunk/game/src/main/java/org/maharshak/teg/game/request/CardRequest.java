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
@XmlRootElement
public class CardRequest extends PlayerRequest {



  private Card _card;

	public CardRequest() {
		super(null);
	}

  /**
   * @param p
   */
	public CardRequest(Person p) {
    super(p);
  }

  /**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
  @Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctGameState = p.getState().atLeast(PLAYER_STATE.TURNOSTART)
        && p.getState().lessThen(PLAYER_STATE.TURN_END);

		boolean underMaxCardsUserCanHold = !(p.getGame().getCards(p).size() >= p
				.getGame().getConf().getMaxCardsToHold());
		boolean hasConquestsThisTurn = p.getGame().getTurnData()
				.getCountriesConqueredThisTurn() > 0;
		boolean hadMoreThenThreeExchangesAndConqueredMoreThenTwo = (p
				.getExchangesSoFar() > 3) ? p.getGame().getTurnData()
        .getCountriesConqueredThisTurn() < 2 : true;
    boolean valid = correctGameState && underMaxCardsUserCanHold && hasConquestsThisTurn
        && hadMoreThenThreeExchangesAndConqueredMoreThenTwo;
    return valid;


  }

  /**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#perform()
	 */
  @Override
	public void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		if (_card == null) {// for debug request
			_card = p.getGame().findRandomFreeCard();
    }

		p.setState(PLAYER_STATE.REQUEST_CARD);
		p.getGame().giveCardToPlayer(_card, p);

  }

  @Override
	public String whyNotValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		boolean correctGameState = !(p.getState().lessThen(PLAYER_STATE.TURNOSTART) || p.getState().atLeast(
        PLAYER_STATE.REQUEST_CARD));

		boolean underMaxCardsUserCanHold = !(p.getGame().getCards(p)
.size() >= p
				.getGame().getConf().getMaxCardsToHold());
		boolean hasConquestsThisTurn = p.getGame().getTurnData()
				.getCountriesConqueredThisTurn() > 0;
		boolean hadMoreThenThreeExchangesAndConqueredMoreThenTwo = (p
				.getExchangesSoFar() > 3) ? p.getGame().getTurnData()
        .getCountriesConqueredThisTurn() < 2 : true;
    boolean valid = correctGameState && underMaxCardsUserCanHold && hasConquestsThisTurn
        && hadMoreThenThreeExchangesAndConqueredMoreThenTwo;
    if (valid)
			return super.whyNotValid(g);
    if (!correctGameState)
      return "Wrong game state " + p.getState();
    if (!underMaxCardsUserCanHold) {
      return "Not  underMaxCardsUserCanHold";
    }
    if (!hasConquestsThisTurn) {
      return "No conquests this turn";
    }
    if (!hadMoreThenThreeExchangesAndConqueredMoreThenTwo)
      return "!hadMoreThenThreeExchangesAndConqueredMoreThenTwo";
		return super.whyNotValid(g);
  }


  @Override
  public String human() {
    return "Requested card: " + _card;
  }

  public Card getCard() {
    return _card;
  }

	public void setCard(Card card) {
		_card = card;
	}

}
