package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;


public class StatusAction extends AbstractTokenAction<Data>  {

	public StatusAction() {
		super(TOKEN.TOKEN_STATUS, "shows status of players");
    throw new UnsupportedOperationException("Currently not supported");
	}

	@Override
	public TEG_STATUS act(Data d) {

    // GameManager game = ((GameManager)d.getGame());
    // Net.net_printf(d.getPerson(),String.format("players:%d, connections:%d, game number:%d, round:%d, mission:%s\n",
    // game.getStillPlaying(), game.getStillPlaying(),
    // game.getGameNumber(),
    // game.getRoundNumber(),
    // (game.getConf().hasSecretMission() ? "TRUE"
    // : "FALSE")
    // ));
    // Net.net_printf(d.getPerson(),String.format("fd, number, countries, armies, cards, exch, name, human, color, status, address\n"));
    //
    // for (Player p : game.getPlayerList()) {
    // ChipsColor color = p.getChipsColor();
    //
    // Net.net_printf(d.getPerson(),String.format("%-3d %d  %-3u  %-3u  %d  %d  %-15s  %s  %s  %s  %s\n",
    // p.getId(),
    // p.getId(),
    // game.countriesOf(p).size() ,
    // game.armiesHas(p),
    // game.getCards(p).size(),
    // p.getExchangesSoFar(),
    // p.getName(),
    // (p.isHuman()) ? "yes" : "no",
    // color.getName(),
    // Common.g_estados[p.getState().ordinal()],
    // p.getPerson().getAddr()
    // ));
    //
    //
    //
    // }
		return TEG_STATUS.TEG_STATUS_SUCCESS;

	}


}
