package org.maharshak.teg.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.Mission;
import org.maharshak.teg.game.Stat;
import org.maharshak.teg.game.TegGame;


public class Player implements Serializable{



  private transient final TegGame _game;
	/* server player */


  private final String _name;
  /** < name */
	

	private final Person _person;					/**< file descriptor */
  private final boolean _human;
  private final ChipsColor _color;
  private final Mission _mission;
	private Stat player_stats;
	


  public Player(TegGame g, PlayerToBeData data) {
    this(g, data.getColor(), data.getName(), data.getPerson(), data.isHuman(), data.getMission());
    init();
  }



  public Player(TegGame g, ChipsColor c, String name, Person p, boolean isHuman, Mission mission) {
    _game = g;
    _person = p;
    _name = name;
    _color = c;
    _human = isHuman;
    _mission = mission;
  }

	public void init() {
		player_stats=new Stat();
		Stat.stats_init( player_stats );
	}





	/* quantity of countries per contient that a player has */
	public static Map<Continent,List<Country>> countryPerContinent( Player pJ )
	{

		assert( pJ !=null);

		Map<Continent, List<Country>> res=new HashMap<Continent, List<Country>>();
		for (Continent co:  pJ.getGame().getBoard().getConts() ) {
			res.put(co, new ArrayList<Country>());
		}

		for (Country c : pJ.getGame().countriesOf(pJ) ) {
			res.get(c.getContinent()).add(c);
		}	

		return res;
	}

	/* quantity of continents that a player has */
	public List<Continent> continentsOwned(  )
	{
		List<Continent> ret=new ArrayList<Continent>();
		Map<Continent, List<Country>> cPerContinent = countryPerContinent( this);
		if(  !cPerContinent.isEmpty()) {
			for (Continent cont : cPerContinent.keySet()) {
				if(getGame().getBoard().getCountries(cont).size()==cPerContinent.get(cont).size())
					ret.add(cont);
			}
		}
		return ret;
	}







	public void setPlayer_stats(Stat player_stats) {
		this.player_stats = player_stats;
	}

	public Stat getPlayer_stats() {
		return player_stats;
	}


	public int getId() {
		return getGame().getId(this);
	}



	public String getName() {
		return _name;
	}


	public boolean isHuman() {
		return _human;
	}



	public Person getPerson() {
		return _person;
	}


	public int getExchangesSoFar() {
		return _game.getExchangesPerformed(this);
	}

	

	public void setState(PLAYER_STATE state) {
		getGame().setState(this,state);
	}

	public PLAYER_STATE getState() {
		return getGame().getPlayerState(this);
	}

	public boolean isAtLeastAt(PLAYER_STATE state){
		return getState().ordinal() >= state.ordinal();
	}

	public Mission getMission() {
    return _mission;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
    return String.format("[{%s}%s(%d)]", _color, _name, getId());
	}

  public TegGame getGame() {
		return _game;
	}

  public ChipsColor getChipsColor() {
    return _color;
  }



	public int calculateArmiesToPutNoConts() {
		int total_armies = getGame().getConf().getRuleBook()
				.amountOfArmiesBasedOnOwnedCountries(this);
	
		if (getGame().getTurnData().isExchangePerformed())
			total_armies += getGame().getConf().getRuleBook()
					.armiesPerExchange(getExchangesSoFar());
		return total_armies;
	}


}


