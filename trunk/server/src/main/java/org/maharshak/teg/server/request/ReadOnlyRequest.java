/**
 * 
 */
package org.maharshak.teg.server.request;

import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.game.request.PlayerRequest;
import org.maharshak.teg.player.Person;

/**
 * @author uztnus
 *
 */
public abstract class ReadOnlyRequest extends PlayerRequest {

	protected ReadOnlyRequest(Person p) {
    super(p);
  }


  /**
	 * @param g
	 * @see org.maharshak.teg.game.request.Request#isValid()
	 */
  @Override
	protected boolean isValid(TegGame g) {
		return g.isInPlay();
  }



}
