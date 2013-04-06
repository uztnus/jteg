package org.maharshak.teg.backoffice.ai.teg;

import org.maharshak.teg.backoffice.ai.Pair;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.Player;


public class Regroup {

	/**
	 * @param game 
	 * @fn int ai_many_country_enemigo( int p )
	 * tells the amount of enemies the country has
	 */
	public static int ai_many_country_enemigo( Country c, Player p)
	{

		int enemyCountries=0;
    TegGame game = p.getGame();
		for (Country other : game.getBoard().getCountries()) {
			if( game.getOwner(other) != p && game.getBoard().haveBorders(c,other))
				enemyCountries++;
		}
		return enemyCountries;
	}
	
	public static Pair<Country,Integer> __ai_reagrupe( Country country, int amount,Player p)
	{
    TegGame game = p.getGame();
		if( ai_own_continent( country.getContinent(),p)) {
			 Pair<Country, Integer> tmp = continentBasedRegroup(country, amount, p);
			 if(tmp!=null)
				 return tmp;
		}

		Pair<Country, Integer> tmp = moveToContinentBorders(country, amount, p, game); 
		if(tmp!=null)
			return tmp;
		
		return moveCloserToBorder(country, amount, p);

	}

	/**
	 * @param country
	 * @param amount
	 * @param p
	 * @param game
	 * @return 
	 */
	protected static Pair<Country,Integer> moveCloserToBorder(Country country, int amount,Player p) {
    TegGame game = p.getGame();
		/* ... and if there is no border command at the 1st what is at hand */ 
		/*... y si no hay frontera lo mando al 1ro que tenga a mano */
		for (Country other : game.getBoard().getCountries()) {
			if( game.getOwner(other)== p && game.getBoard().haveBorders(country,other) &&
					other.getContinent()== country.getContinent()) {
				return new Pair<Country, Integer>(other, amount);
			}
		}
		return null;
	}

	/**
	 * @param country
	 * @param amount
	 * @param p
	 * @param game
	 * @return 
	 */
	protected static Pair<Country,Integer> moveToContinentBorders(Country country, int amount,
 Player p, TegGame game) {
		/*... si falla, los muevo a una frontera */
		/* ... if it fails, the move to a border */
		for (Country other : game.getBoard().getCountries()) {
			if( game.getOwner(other) == p && game.getBoard().haveBorders(country,other) &&
					other.getContinent()== country.getContinent()) {

				if( ai_is_country_border(other,game) ) {
					return new Pair<Country, Integer>(other,amount); 
				}
			}
		}
		return null;
	}

	/**
	 * @param country to check
	 * @param amount left for regroup
	 * @param p player
	 * @param game
	 * @return 
	 */
	protected static Pair<Country,Integer> continentBasedRegroup(Country country, int amount,
			Player p) {
		int new_cant = amount;

    TegGame game = p.getGame();
		/* try to defend, undefended frontiers */
		for (Country other : game.getBoard().getCountries()) {
			if( game.getOwner(other) != p &&
					game.getBoard().haveBorders(country,other) &&
				game.getArmies(other)> game.getArmies(country)) {
				return new Pair<Country, Integer>(other, amount);
			}
			
		}
		/* I will leave 3 armies in border country of the continent */
		if (game.getArmies(country) - game.leftForRegruping(country) - amount < 3)
			new_cant -= 2;
		
		if(new_cant>0){
			for (Country other : game.getBoard().getCountries()) {
				if( game.getOwner(other) == p && game.getBoard().haveBorders(country	,other) &&
						other.getContinent()!= country.getContinent()) {
					return new Pair<Country, Integer>(other, new_cant); 
				}
				
			}
		}
		return null;
	}

	/**
	 * @param game 
	 * @fn boolean ai_own_contienent( int c )
	 * Dice si soy owner de un continente
	 */
	public static boolean ai_own_continent( Continent c, Player p )
	{
    TegGame game = p.getGame();
		for (Country co : game.getBoard().getCountries(c)) {
			if(!(game.getOwner(co)==p))
				return false;
		}
		return true;

	}

	/**
	 * @param game 
	 * @fn boolean ai_is_country_border( int p )
	 * Tells if a country border of its continent
	 */
  public static boolean ai_is_country_border(Country p, TegGame game)
	{

		for (Country other : game.getBoard().getCountries()) {
			if( game.getBoard().haveBorders(other,p) && other.getContinent() != p.getContinent() )
				return true;
			
		}
		return false;
	}

}
