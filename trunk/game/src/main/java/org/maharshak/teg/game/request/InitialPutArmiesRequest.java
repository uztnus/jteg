/**
 * 
 */
package org.maharshak.teg.game.request;

import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
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
public class InitialPutArmiesRequest extends AbstractPutArmyRequest {


	private PLAYER_STATE _playerState;

	public InitialPutArmiesRequest() {
		// TODO Auto-generated constructor stub
	}

	public InitialPutArmiesRequest(Person p, Map<Country, Integer> armies,
			PLAYER_STATE state) {
		super(p, armies);
		_playerState=state;

	}




	/**
	 * @param g
	 * @throws TegRequestException
	 */
	protected void postPut(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		p.setState(p.getState().next());
		p.getGame().turnFinished();
	}



	@Override
	protected boolean validatePlayerStatus(TegGame g) {
		Player p = g.getPlayer(getOwner());
		return p.isAtLeastAt(_playerState);
	}



	@XmlAttribute
	public PLAYER_STATE getPlayerState() {
		return _playerState;
	}



	@Override
	protected int getHowManyToPut(TegGame g) {

		switch (_playerState) {
		case FIRST_PUT:
			return g.getConf().getArmiesInFirstRound();
		case SECOND_PUT:
			return g.getConf().getArmiesInSecondRound();
		default:
			return -1;
		}

	}

	public void setPlayerState(PLAYER_STATE playerState) {
		_playerState = playerState;
	}	


}
