package org.maharshak.teg.server.request;

import org.maharshak.teg.TegRequestException;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Person;
import org.maharshak.teg.player.Player;

public class ShowCards extends ReadOnlyRequest {

  public ShowCards(Person p) {
    super(p);
  }

  @Override
  public String human() {
    return "show player's cards";
  }

  @Override
	protected void runCommand(TegGame g) throws TegRequestException {
    boolean first;
    String buffer = "";
    first = true;
		Player p = g.getPlayer(getOwner());
		for (Card t : p.getGame().getCards(p)) {
      if (first) {
				buffer += String.format("%d:%d", t.getCountry(), p.getGame().isUsed(t));
        first = false;
      } else {
				buffer += String
						.format(",%d:%d", t.getCountry(), p.getGame().isUsed(t));
      }
    }
		Net.net_printf(getOwner(), "%s=%s\n", TOKEN.TOKEN_ENUM_CARDS, buffer);
  }

}
