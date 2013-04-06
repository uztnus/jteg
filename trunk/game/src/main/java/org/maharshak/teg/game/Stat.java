package org.maharshak.teg.game;

import java.io.Serializable;

public class Stat implements Serializable{


	private int countries_won;		/**< number of countries won */
	private int countries_lost;		/**< number of countries lost */
	private int armies_killed;		/**< number of armies killed */
	private int armies_lost;		/**< number of armies lost */
	private int continents_turn[];	/**< number of turns that the conq was defended */
	private int players_killed;		/**< quantity of players killed */
	private int score;		/**< score, based on the stats */

	public Stat() {
//		setContinents_turn(new int[Cont.g_conts.length]);
	}

	public static void stats_init( Stat s )
	{
		s.countries_lost=0;
//		s.setContinents_turn(new int[Cont.g_conts.length]);
		s.setArmies_killed(0);
		s.armies_lost=0;
		s.players_killed=0;
		s.score=0;
	}
	
//public static	TEG_STATUS stats_score( Stat s )
//	{
//		
//
//		/* points for armies, countries */
//		s.setScore(s.getCountries_won() * 5 + s.getArmies_killed() * 2 - s.getCountries_won() * 2 - s.getArmies_killed());
//
//		/* points for defending continents */
//		for(int i=0;i<Cont.g_conts.length;i++)
//			s.setScore(s.getScore() + (s.getContinents_turn()[i] * Cont.g_conts[i].getFichas_x_cont()));
//		return TEG_STATUS.TEG_STATUS_SUCCESS;
//	}

public void setScore(int score) {
	this.score = score;
}

public int getScore() {
	return score;
}

public void setCountries_won(int countries_won) {
	this.countries_won = countries_won;
}

public int getCountries_won() {
	return countries_won;
}

public void setArmies_killed(int armies_killed) {
	this.armies_killed = armies_killed;
}

public int getArmies_killed() {
	return armies_killed;
}

public int[] getContinents_turn() {
	return continents_turn;
}

public void setContinents_turn(int continents_turn[]) {
	this.continents_turn = continents_turn;
}

}
