package org.maharshak.teg.server;

import org.maharshak.teg.board.Country;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TegProtocol;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Player;


public class Fow {



	/* returns treu if player pJ can see country pP */
  public static boolean canSeeCountry(Player pJ, Country pP) {
		if(  pJ==null  ||  pP==null )
			return false;

    for (Country c : pJ.getGame().countriesOf(pJ)) {
      if (pJ.getGame().getBoard().haveBorders(pP, c))
        return true;
    }

		return false;
	}

	/* fills in buffer the boundaries countries of 'country' */
	public static String fow_fill_with_boundaries( Country country,Player p )
	{

    TegGame game = p.getGame();
		if(!game.getConf().isFogOfWar())
			return null;
		

		String buf="";
		for (Country other : game.getBoard().getCountries()) {
			if(other!=country
					&&  game.getOwner(other)!=p
					&& game.getBoard().haveBorders( country, other )){ 
					buf+=String.format(";%s=%d,%d,%d",TOKEN.TOKEN_COUNTRY, TegProtocol.getCountryId(other) , (game.getOwner(other)).getId(),game.getArmies(other) );
			}
		}

		/* dont send, since I didnt modify the buffer */
    if (buf.isEmpty())
			return null;

    return buf.substring(1);
	}


}
