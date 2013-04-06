package org.maharshak.teg.board;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.maharshak.teg.board.Card.CardType;



public class BoardLoader {


	private static final String DEFAULT_MAP = "teg_board.csv";
	private static final String DEFAULT_CONTS = "teg_conts.csv";
	private static final String DEFAULT_BORDERS = "teg_borders.csv";
	private static final String DEFAULT_CARDS = "teg_cards.csv";



	public static Map<Continent, List<Country>> initContsToCountries(Board b) throws IOException{
		URL mapurl = BoardLoader.class.getClassLoader().getResource(DEFAULT_MAP);
		return initContsToCountries(mapurl,b);
	}

	public static Map<String, Integer> initConts() throws IOException{
		URL mapurl = BoardLoader.class.getClassLoader().getResource(DEFAULT_CONTS);
		return initConts(mapurl);
	}

	public static Map<String, Integer> initConts(URL mapurl) throws IOException {


		InputStream is = mapurl.openStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br=new BufferedReader(isr);
		Map<String, Integer> res=new HashMap<String, Integer>();
		while(br.ready()){
			String line=br.readLine();
			String[] sp = line.split(",");
			res.put(sp[0], Integer.parseInt(sp[1]));
		}
		return res;

	}
	public static List<Card> initCards(List<Country> countries) throws IOException{
		URL mapurl = BoardLoader.class.getClassLoader().getResource(DEFAULT_CARDS);
		return initCards(mapurl,countries);
	}

	public static List<Card> initCards(URL mapurl, List<Country> countries) throws IOException {


		InputStream is = mapurl.openStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br=new BufferedReader(isr);
		List<Card> res=new ArrayList<Card>();
		while(br.ready()){
			String line=br.readLine();
			String[] sp = line.split(",");
			res.add( new Card(CardType.valueOf(sp[1]) , getCountry(sp[0], countries) ));
		}
		return res;

	}

	public static BorderMatrix initBorders(List<Country> countries) throws IOException{
		URL mapurl = BoardLoader.class.getClassLoader().getResource(DEFAULT_BORDERS);
		return initBorders(mapurl,countries);
	}

	public static BorderMatrix initBorders(URL mapurl,List<Country> countries) throws IOException {
		InputStream is = mapurl.openStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br=new BufferedReader(isr);

		BorderMatrix res=new BorderMatrix(countries);

		while(br.ready()){
			String line=br.readLine();
			String[] sp = line.split(",");
			res.setBorder(getCountry(sp[0],countries), getCountry(sp[1],countries));
		}
		return res;
	}



	private static Country getCountry(String string, List<Country> countries) {
		for (Country country : countries) {
			if(country.getName().compareTo(string)==0)
				return country;
		}
		return null;
	}


	public static Map<Continent, List<Country>> initContsToCountries(URL mapurl, Board b) throws IOException {


		InputStream is = mapurl.openStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br=new BufferedReader(isr);
		Map<String,List<String>> tmp=new HashMap<String, List<String>>();
		while(br.ready()){
			String line=br.readLine();
			String[] sp = line.split(",");
			if(!tmp.containsKey(sp[1]))
				tmp.put(sp[1], new ArrayList<String>());
			tmp.get(sp[1]).add(sp[0]);
		}
		Map<Continent,List< Country>> res=new HashMap<Continent, List<Country>>();

		for (String con : tmp.keySet()) {
			Continent c= new Continent(con, b);
			res.put(c, new ArrayList<Country>());	
			for (String countryName : tmp.get(con)) {
				res.get(c).add(new Country(countryName, b));
			}
			res.put(c, Collections.unmodifiableList(res.get(c)));
		}
		return res;
	}

}
