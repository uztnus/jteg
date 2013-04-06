package org.maharshak.teg.common;

import java.util.ArrayList;
import java.util.List;

import org.maharshak.teg.game.Stat;


public class Score {

	public interface ScoresMapFucn{
		public TEG_STATUS map(Score s);
	}

	/* Top Ten only */
	public static final int  SCORES_MAX	=(10);

	private String name;	/**< name of the player */
	private int color;			/**< color used */
	private String date;	/**< date of the game */
	private boolean human;			/**< human or robot */

	private Stat stats;		/**< stats of the game */

	private static List<Score> g_list_scores;



	/* ownership is yielded */
	public static TEG_STATUS scores_insert_score( Score pS_new )
	{

		g_list_scores.add(pS_new);
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public static TEG_STATUS scores_map( ScoresMapFucn func )
	{
		assert func!=null;
		for (Score s : g_list_scores) {
			func.map(s);
		}
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public static TEG_STATUS scores_init()
	{
		g_list_scores=new ArrayList<Score>();
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	public static TEG_STATUS scores_flush()
	{
		g_list_scores.clear();
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	List<Score> scores_get_list()
	{
		return g_list_scores;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setStats(Stat stats) {
		this.stats = stats;
	}

	public Stat getStats() {
		return stats;
	}

	public void setHuman(boolean human) {
		this.human = human;
	}

	public boolean isHuman() {
		return human;
	}



}
