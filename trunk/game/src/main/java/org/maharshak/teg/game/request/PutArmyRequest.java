package org.maharshak.teg.game.request;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;

@XmlRootElement
public class PutArmyRequest extends AbstractPutArmyRequest{

	public PutArmyRequest(Person p, Map<Country, Integer> armies) {
		super(p, armies);
	}

	public PutArmyRequest() {
		// TODO Auto-generated constructor stub
	}
	@Override
	protected boolean validatePlayerStatus(TegGame g) {
		Player p = g.getPlayer(getOwner());
		return p.getState() == PLAYER_STATE.PUT_ARMIES
				|| p.getState() == PLAYER_STATE.EXCHANGE;
	}


	@Override
	protected void postPut(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		p.getGame().turnFinished();
		p.getGame().getTurnData().init();
	}
	
	public static int troopsForContinents(List<Continent> conts ){
		int max;
		max = 0;
		for (Continent c : conts) {
			max+=c.getChips();
		}
		return max;
	}



	@Override
	protected int getHowManyToPut(TegGame g) {
		Player p=g.getPlayer(getOwner());
		int res = p.calculateArmiesToPutNoConts()
				+ troopsForContinents(p.continentsOwned());
		return res;
	}

}
