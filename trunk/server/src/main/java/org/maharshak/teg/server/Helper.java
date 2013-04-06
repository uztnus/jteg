package org.maharshak.teg.server;


import org.maharshak.teg.game.GameDefinition;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.ChipsColor;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Player;
import org.maharshak.teg.player.PlayerToBeData;



public class Helper {



	/* generates the status in a given array */
  public static String aux_token_stasta(TegGame game) {
		StringBuilder sb=new StringBuilder(); 
		for (Player j : game.getPlayerList()) {

      ChipsColor color = (j.getChipsColor() == null) ? ChipsColor.NA : j.getChipsColor();
			
				sb.append(String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s/"
						,j.getName(),
						ChipsColor.getId(color),
						j.getPlayer_stats().getScore(),
						j.getId(),
						j.getState().ordinal(),
						(! game.getConf().isFogOfWar()) ? game.countriesOf(j).size()  : -1,
								(! game.getConf(). isFogOfWar()) ? game.armiesHas(j) : -1,
										game.getCards(j).size(),
										(game.getRoundStarter() !=null&&(game.getRoundStarter().getId()==j.getId())) ? 1:0,
												(j.isHuman())?1:0,
														j.getPerson().getAddr()
						));
		}

		return sb.substring(0, sb.length()-1);
	}


  /* generates the status in a given array */
  public static String aux_token_stasta(GameDefinition def) {
    StringBuilder sb = new StringBuilder();
    for (PlayerToBeData j : def.getPlayersToBe()) {

      ChipsColor color = (j.getColor() == null) ? ChipsColor.NA : j.getColor();

      sb.append(String.format("%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%s/", j.getName(), ChipsColor.getId(color), 0, def.getPID(j),
          (j.getColor() != ChipsColor.NA) ? PLAYER_STATE.ENABLED.ordinal() : PLAYER_STATE.CONNECTED.ordinal(), 0, 0, 0, 0,
          (j.isHuman()) ? 1 : 0, j.getPerson().getAddr()));
    }

    return sb.substring(0, sb.length() - 1);
  }
}
