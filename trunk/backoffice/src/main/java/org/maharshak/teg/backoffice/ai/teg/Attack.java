package org.maharshak.teg.backoffice.ai.teg;

import org.maharshak.teg.backoffice.ai.Pair;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.game.TegGame;
import org.maharshak.teg.player.PLAYER_STATE;
import org.maharshak.teg.player.Player;


public class Attack {

	
	/**
	 * @param game 
	 * @param attackDestination 
	 * @param attackPoints 
	 * @fn int ai_puntaje_attack( int p )
	 * Evalua los puntos que tiene el country para atacar
	 * y dice al country que convendria atacar
	 * Evaluate the points you have to attack the country
	 * And tell the country that it would attack
	 */
  public static Pair<Integer, Country> calculateAttackPoints(Country c, TegGame game)
	{

		int p=0;
		int p_tmp;
		Country dst=null;

		for (Country other : game.getBoard().getCountries()) {
			if( game.getBoard().haveBorders(c,other) && game.getOwner(other) !=game.getOwner(c) ) {
				p_tmp = calculateAttackPointsPerCountry( c, other ,game);

				/* These values ​​are negative, so high */
				/* son negativos estos valores, por eso sumo */
				p_tmp += getAttackWarningPoints(c,game);

				if( p_tmp > p ) {
					p = p_tmp;
					dst = other;
				}
			}

		}
		/* return the best candidate */
		return new Pair<Integer, Country>(p, dst);
	}

	/**
	 * @param game 
	 * @fn int ai_puntaje_atacar_warning( int src )
	 * Resta puntos si existe un country limitrofe a 'src' que puede atacarlo antes
	 * de que se acabe el turno
	 * Subtract points if a country bordering on 'src' that can attack before
	 * Runs out of turn
	 * 
	 * ai score attack warning
	 */
  public static int getAttackWarningPoints(Country curr, TegGame game)
	{
		int p=0;
		int i;
		for (Country other : game.getBoard().getCountries()) {
			if( game.getBoard().haveBorders(curr,other) &&
					game.getOwner(curr) != game.getOwner(other) &&
					isDangerousCountry(curr,other,game)) {
				p += (game.getArmies(other) - game.getArmies(curr));
			}

		}
		return -(5+2*p);
	}

	/**
	 * @param game 
	 * @fn boolean ai_is_country_peligroso( int src, int dst )
	 * Dice si un country el peligroso, dependiendo si este lo puede
	 * atacar antes de que se acabe el turno
	 * * Tells if a dangerous country, depending on whether this can
	 * Attack before the turn is over

	 */
  public static boolean isDangerousCountry(Country src, Country dst, TegGame game)
	{
		Player owner =  game.getOwner(dst);
		if(owner.getState().atLeast(PLAYER_STATE.REGROUP))//Already played
			return false;

		int tmp = (game.getArmies(src) > 3) ? 3: game.getArmies(src)-1;
		if( game.getArmies(dst) > (game.getArmies(src)-tmp) )
			return true;
		return false;
	}

	/**
	 * @param game 
	 * @fn int ai_puntaje_atacar_country( int src, int dst )
	 * Dice los puntos que tiene de atacar src a dst
	 * Tells the points you have to attack src to dst
	 */
  public static int calculateAttackPointsPerCountry(Country src, Country dst, TegGame game)
	{
		int p=0;

		if( game.getArmies(src) > game.getArmies(dst) ) {

			if( src.getContinent()== dst.getContinent())
				p++;

			p += 10 + game.getArmies(src) - game.getArmies(dst);

		} else {

			int r = (int) (Math.random()*10);

			if((r>7) && ( game.getArmies(src) > 7 ))
				p++;
		}

		return p;
	}

}
