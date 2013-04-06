package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.player.Player;


public class StatsAction extends AbstractTokenAction <Data> {

	private static TEG_STATUS doStats(Player pJ) {
		//					Stat.stats_score( pJ.getPlayer_stats() );
					System.out.println(
							String.format(" %i   %-4i  [ %-3u   %-3u ] - [ %-3u  %-3u ]  %-15s %s\n",
									pJ.getId(),
									pJ.getPlayer_stats().getScore(),
									pJ.getPlayer_stats().getCountries_won(),
									0,
									pJ.getPlayer_stats().getArmies_killed(),
									0,
									pJ.getName(),
									(pJ.isHuman()) ? "yes" : "no"
									));
					return TEG_STATUS.TEG_STATUS_SUCCESS;
	}		


	//		{ ,		con_stats,	N_() },
	@Override
	public TEG_STATUS act(Data d) {
    // System.out.println(String.format("Number Score - [Countries: Won  Lost] - [Armies: Won  Lost]  Name Human\n"));
    //
    // for (Player p : ((GameManager)d.getGame()).getPlayerList()) {
    // doStats(p);
    // }
			return TEG_STATUS.TEG_STATUS_SUCCESS;

	}

	public StatsAction() {
		super(TOKEN.TOKEN_STATS, "show players statistics");
    throw new UnsupportedOperationException("Currently not supported");
	}

}
