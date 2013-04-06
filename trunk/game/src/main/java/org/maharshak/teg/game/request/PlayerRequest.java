/**
 * 
 */
package org.maharshak.teg.game.request;

import javax.xml.bind.annotation.XmlElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;


/**
 * @author uztnus
 *
 */
public abstract class PlayerRequest {


	public static enum RequestState {
		CREATED, VALIDATED, PROCESS_STARTED, FINISHED, NOT_VALID, ERROR;

		public boolean atLeast(RequestState other) {
			return this.ordinal() >= other.ordinal();
		}
	}



	private volatile PlayerRequest.RequestState _state;
	private Person _owner;

	public PlayerRequest() {

	}

	protected PlayerRequest(Person p) {
		_owner = p;
		setState(PlayerRequest.RequestState.CREATED);
	}

	public void setState(PlayerRequest.RequestState state) {
		_state = state;
	}


	/**
	 * @return
	 */
	@XmlElement
	public final String getName() {
		String cName = this.getClass().getName();
		String[] split = cName.split("\\.");
		String name = (split.length > 0) ? split[split.length - 1] : cName;
		return name;
	}

	/**
	 * A blocking method that allows the thread to stop till the requested is
	 * played.
	 */
	public void waitTillProccessed() {
		while (true) {
			if (getState().ordinal() >= PlayerRequest.RequestState.FINISHED.ordinal())
				return;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	public String whyNotValid(TegGame g) {
		return "Don't know";

	}

	public abstract String human();


	@Override
	public String toString() {
		String name = getName();
		return "Command "+name+" ";
	}

	public PlayerRequest.RequestState getState() {
		return _state;
	}


	/**
	 * 
	 * @return true if this request is valid
	 */
	protected abstract boolean isValid(TegGame g);

	public boolean validate(TegGame g) {
		Player p = g.getPlayer(_owner);
		if (p == null) {
			setState(PlayerRequest.RequestState.NOT_VALID);
			return false;
		}
		boolean res = isValid(g);
		setState((res) ? PlayerRequest.RequestState.VALIDATED : PlayerRequest.RequestState.NOT_VALID);
		return res;
	}

	/**
	 * performs the request
	 * 
	 * @throws TegRequestException
	 *           in case of an error
	 */
	public final void perform(TegGame g) throws TegRequestException {
		try {
			setState(PlayerRequest.RequestState.PROCESS_STARTED);
			runCommand(g);
			setState(PlayerRequest.RequestState.FINISHED);
		} catch (TegRequestException e) {
			setState(PlayerRequest.RequestState.ERROR);
		}
	}

	protected abstract void runCommand(TegGame g) throws TegRequestException;

	@XmlElement
	public Person getOwner() {
		return _owner;
	}

	public void setOwner(Person owner) {
		_owner = owner;
	}
}
