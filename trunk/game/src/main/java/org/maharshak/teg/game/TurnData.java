package org.maharshak.teg.game;

import java.io.Serializable;

import org.maharshak.teg.board.Country;



public class TurnData implements Serializable{
	/**
	 * 
	 */
	/**
	 * @param player
	 */
	public TurnData() {
		init();
	}

	private int _conqueredCountries;				/**< Countries conquered in the turn.
  																A player must conquer 1 countrie to ask for cards.
  																And 2 countries, after the 3rd exchange */
	
	private Country _srcCountryAfterConquest;				
	private Country _dstCountryAfterConquest;				

	private boolean _exchangePerformed;			
	public int getCountriesConqueredThisTurn() {
		return _conqueredCountries;
	}

	public void addCounqueredCountry(){
		_conqueredCountries ++;
	}


	public boolean isExchangePerformed() {
		return _exchangePerformed;
	}

	public void exchangePerformed() {
		_exchangePerformed = true;
	}
	public Country getAttackDst() {
		return _dstCountryAfterConquest;
	}

	public void setAttackDst(Country dst) {
		this._dstCountryAfterConquest = dst;
	}
	

	public Country getAttackSource() {
		return _srcCountryAfterConquest;
	}

	public void setAttackSource(Country src) {
		this._srcCountryAfterConquest = src;
	}

	public void init() {
		_conqueredCountries=0;
		_exchangePerformed=false;
		_srcCountryAfterConquest=null;
		_dstCountryAfterConquest=null;
	}
}