/**
 * 
 */
package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
@XmlRootElement
public class MessageRequest extends PlayerRequest {

	@XmlElement(name = "message")
	private String _msg;

	public MessageRequest() {

	}

	public MessageRequest(Person p, String msg) {
		super(p);
		_msg=msg;
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#isValid()
	 */
	@Override
	public boolean isValid(TegGame g) {
		Player p = g.getPlayer(getOwner());
		return !_msg.isEmpty() && p != null;
	}

	/**
	 * @param g
	 * @see org.maharshak.teg.game.request.PlayerRequest#runCommand()
	 */
	@Override
	protected void runCommand(TegGame g) throws TegRequestException {
		Player p = g.getPlayer(getOwner());
		p.getGame().sendMessage(p, _msg);

	}

	@Override
	public String human() {
		return "Message "+_msg;
	}
	public String getMsg() {
		return _msg;
	}

	
}
