package org.maharshak.teg.common;

import java.util.Comparator;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.player.Player;


public class TegProtocol {
	private static String [] _countriesByName;
	private static String [] _contsByName;
	static{init();}
	
	 
	public static int getCountryId(Country c){
		return getCountryId(c.getName());
	}
	
	public static int getContId(Continent c){
		return getContId(c.getName());
	}
	
	private static int getContId(String name) {
		for (int i = 0; i < _contsByName.length; i++) {
			if(_contsByName[i].compareTo(name)==0)
				return i;
		}
		return -1;
	}

	public static int getCountryId(String name) {
		for (int i = 0; i < _countriesByName.length; i++) {
			if(_countriesByName[i].compareTo(name)==0)
				return i;
		}
		return -1;
	}
	
	public static Country getCountry(int i , Board b){
		if(i<0||i>_countriesByName.length-1)
			return null;
		String name=_countriesByName[i];
		for (Country c : b.getCountries()) {
			if(c.getName().compareTo(name)==0)
				return c;
		}
		return null;
	}
	
	private static void init() {
		_countriesByName= new String[]{
				/* AMERICA DEL SUR */
				"Argentina",
				"Brazil",
				"Chile",
				"Colombia",
				"Peru",
				"Uruguay",

				/* AMERICA DEL NORTE */
				"Mexico",
				"California",
				"Oregon",
				"New York",
				"Alaska",
				"Yukon",
				"Canada",
				"Terranova",
				"Labrador",
				"Greenland",

				/* AFRICA */
				"Sahara",
				"Zaire",
				"Ethiopia",
				"Egypt",
				"Madagascar",
				"South Africa",

				/* OCEANIA */
				"Australia",
				"Borneo",
				"Java",
				"Sumatra",

				/* EUROPA */
				"Spain",
				"France",
				"Germany",
				"Italy",
				"Poland",
				"Russia",
				"Sweden",
				"Great Britain",
				"Iceland",

				/* ASIA */
				"Arabia",
				"Israel",
				"Turkey",
				"India",
				"Malaysia",
				"Iran",
				"Gobi",
				"China",
				"Mongolia",
				"Siberia",
				"Aral",
				"Tartary",
				"Taymir",
				"Kamchatka",
				"Japan"
		};
		_contsByName=new String[]{
				 "South America",
				"North America",
				"Africa", 
				"Oceania",
				"Europe", 
				"Asia"
		};
	}

	public static Country getCountry(Player player,int index ) {
		return getCountry(index,  player.getGame().getBoard());
	}

	 public static final Comparator<Country> COMP = new Comparator<Country>() {
		  
     @Override
     public int compare(Country o1, Country o2) {
         return getCountryId(o1)-getCountryId(o2);
     }
 };

}
