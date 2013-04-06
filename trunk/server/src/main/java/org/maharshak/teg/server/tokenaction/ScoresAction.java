package org.maharshak.teg.server.tokenaction;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.Score.ScoresMapFucn;
import org.maharshak.teg.common.Score;
import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.ref.AbstractTokenAction;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.player.ChipsColor;


public class ScoresAction extends AbstractTokenAction<Data>  {

	@Override
	public TEG_STATUS act(Data d) {
		System.out.println(String.format("  score  date       time    name              color    human\n"));
		Score.scores_map( new ScoresMapFucn() {
			
			@Override
			public TEG_STATUS map(Score pS) {
				int color;
				color = ( ( pS.getColor() >= 6 || pS.getColor() < 0 ) ?6: pS.getColor() );
				System.out.println(String.format("  %4d   %s   %-15s   %-8s %s\n",
						pS.getStats().getScore(),
						pS.getDate(),
						pS.getName(),
						ChipsColor.getColour(color).getName(),
						(pS.isHuman()) ?"yes" : "no"
						));

				return TEG_STATUS.TEG_STATUS_SUCCESS;

			}
		});
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public ScoresAction() {
		super(TOKEN.TOKEN_SCORES,"show all-time high scores");
	}
	

}
