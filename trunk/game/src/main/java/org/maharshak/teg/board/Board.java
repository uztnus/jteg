package org.maharshak.teg.board;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.util.BoardAdapter;


@XmlJavaTypeAdapter(BoardAdapter.class)
public class Board {


	private static Board _boards[];
	static {
		_boards = new Board[1];
		try {
			_boards[0] = new Board(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Board get(int boardId) {
		return _boards[boardId];
	}
  private final BorderMatrix _border;
	private final List<Card> _deck;


	private final Map<Continent,List<Country>> _continentsToCountries;
	private final List<Country> _countries;
	private final Map<Continent,Integer> _chipsPerContinet;
	private final int _id;

	private Board(int id) throws IOException {
		_continentsToCountries=BoardLoader.initContsToCountries(this);
		_chipsPerContinet=new HashMap<Continent, Integer>();
		Map<String, Integer> tmp = BoardLoader.initConts();
		for (Continent c : _continentsToCountries.keySet()) {
			_chipsPerContinet.put(c, tmp.get(c.getName()));
		}
		_countries=Collections.unmodifiableList(countryList());
		_border=BoardLoader.initBorders(_countries);
		_deck = BoardLoader.initCards(_countries);
		_id = id;
	}


	/**
	 * @return
	 */
	private List<Country> countryList() {
		List<Country> tmp=new ArrayList<Country>();
		for (List<Country> ccs : _continentsToCountries.values()) {
			tmp.addAll(ccs);
		}
		return tmp;
	}


	// @XmlElementWrapper(name = "continents")
	// @XmlElement(name = "continent")
	public List<Continent> getConts(){
		return Collections.unmodifiableList(new ArrayList<Continent>(_continentsToCountries.keySet()));
	}


	/* returns true if country a and b are border */
	public boolean haveBorders( Country a, Country b)
	{
    if (a == b)
      return true;
		return _border.hasBorder(a, b);
	}




	public List<Country> getCountries(Continent cont) {
		return _continentsToCountries.get(cont);
	}

	
	// @XmlElementWrapper(name = "countries")
	// @XmlElement(name = "country")
	public List<Country> getCountries() {
		return _countries;
	}


	public Country getCountry(String name) {
		for (Country c : getCountries()) {
			if(c.getName().compareTo(name)==0)
				return c;
		}
		return null;
	}

	public String serialize(){
		StringBuilder sb=new StringBuilder();
		for (Country c : getCountries()) {
			sb.append(c.getName()).append(",").append(c.getContinent().getName()).append("\n");
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		for (Continent c : _continentsToCountries.keySet()) {
			sb.append(c.getName()).append(",").append(""+c.getChips()).append("\n");
		}

		sb.append("\n");
		sb.append("\n");
		sb.append("\n");

		for (Country c1 : getCountries()) {
			for (Country c2 : getCountries()) {
				if(haveBorders(c1, c2))
					sb.append(c1.getName()).append(",").append(c2.getName()).append("\n");
			}
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		for (Card c : _deck) {
			sb.append(c.getCountry().getName()).append(",").append(c.getType().name()).append("\n");
		}

		return sb.toString();
	}

	public static void main(String[] args) throws IOException {
		// Board b=new Board();
		// System.out.println(b.serialize());
		// double tt = Math.random() * 6;
		// System.out.println(tt);
		// int mm = (int) tt;
		// System.out.println(mm);
	}

	public int getChipsPerContinent(Continent continent) {
		return _chipsPerContinet.get(continent);
	}

	public Continent getContinet(Country c){
		for (Continent t : _continentsToCountries.keySet()) {
			if(_continentsToCountries.get(t).contains(c))
				return t;
		}
		return null;
	}





	public List<List<Country>> splitCountries(int amountOfPlayers) {
	
		List<List<Country>> res=new ArrayList<List<Country>>();
		for (int i = 0; i < amountOfPlayers; i++) {
			res.add(new ArrayList<Country>());
		}
	
	
	
		int countriesAmount = getCountries().size();
		int resto = countriesAmount %amountOfPlayers;
		LinkedList<Country> countries = new LinkedList<Country>(getCountries());
		Collections.shuffle(countries);
		Queue<Country> freeCountries=countries;
	
		for(int i=0,j=0; i <countriesAmount -resto; i++,j=(j+1) % amountOfPlayers) {
			Country randomCountry =freeCountries.poll();
			res.get(j).add(randomCountry);
		}
		if( resto>0 ) {
	
			for(int i=0;i<resto;i++) {
				Country randomCountry = freeCountries.poll();
				int j = (int) (Math.random() * amountOfPlayers);
				res.get(j).add(randomCountry);
			}
		}
		return res;
	}


  public static Map<Continent, List<Country>> split(Collection<Country> keySet) {
  
    Map<Continent, List<Country>> res = new HashMap<Continent, List<Country>>();
    for (Country country : keySet) {
      if (!res.containsKey(country.getContinent()))
        res.put(country.getContinent(), new ArrayList<Country>());
      res.get(country.getContinent()).add(country);
  
    }
    return res;
  }

	public List<Card> getDeck() {
		return _deck;
	}

	public int getId() {
		return _id;
	}

	public Card getCard(Country country) {
		for (Card c : getDeck()) {
			if (c.getCountry() == country)
				return c;
		}
		return null;

	}

}
