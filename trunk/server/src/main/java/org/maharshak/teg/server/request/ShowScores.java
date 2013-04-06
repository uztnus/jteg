package org.maharshak.teg.server.request;

import javax.xml.bind.annotation.XmlRootElement;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;

@XmlRootElement
public class ShowScores extends ReadOnlyRequest {

	public ShowScores(Person p) {
    super(p);
  }

  @Override
  public String human() {
    return "Show scores";
  }

  @Override
	protected void runCommand(TegGame g) throws TegRequestException {
    String strout = null;
    if (strout == null) {
      throw new TegRequestException("No scores....");
    }

		Net.net_printf(getOwner(), "%s=%s\n", TOKEN.TOKEN_SCORES, strout);
  }

}
