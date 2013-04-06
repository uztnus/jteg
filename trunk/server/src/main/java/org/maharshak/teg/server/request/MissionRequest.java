/**
 * 
 */
package org.maharshak.teg.server.request;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.Mission;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
@XmlRootElement
public class MissionRequest extends ReadOnlyRequest {

	private static final String MISSION = "MISSION";
	private Mission _myMission;

	public MissionRequest(Person p) {
    super(p);
	}



	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		Net.net_printf(getOwner(), "%s=%d\n", TOKEN.TOKEN_MISSION,
				Mission.indexOf(p.getMission()));
	}



  public static MissionRequest create(Map<String, String> data, GameDefinition g) {
    // MissionRequest res = new MissionRequest(new Person(data.get(OWNER_ID)),
    // g);
		//	FIXME no specific mission
    // return res;
    return null;
	}

  @Override
  public String human() {
    return "Players mission";
  }
}
