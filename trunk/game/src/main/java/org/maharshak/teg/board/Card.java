package org.maharshak.teg.board;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;



/**
 * Card
 * @author Owner
 *
 */
@XmlRootElement
public class Card implements Serializable{

	//Card type
	public static enum CardType{
		KNIGHT(1),		/**< valor del galeon */
		CANNON (4),		/**< valor del canion */
		SOLDIER (8),			/**< valor del globo */
		WILDCARD (32),		/**< valor del comodin */
		;
		private int _type;

		private CardType(int b){
			_type=b;
		}

		public int getType() {
			return _type;
		}
	}

	private CardType _type;	
	private Country _country;		

	public Card() {
		
	}
	public Card(CardType type, Country c) {
		_type = type;
		_country=c;
	}

	@XmlElement
	public Country getCountry() {
		return _country;
	}

	@XmlAttribute
	public CardType getType() {
		return _type;
	}

	/**
	 * @fn BOOLEAN tarjeta_puedocanje( int numjug, int t1, int t2, int t3 )
	 * Dice si es correcto el canje con las tarjetas t1,t2 y t3
	 * Says if it is correct for player numjug to exchange the cards t1, t2 and t3
	 */
	public static boolean canExchangeBePerformed(  Card t1, Card t2, Card t3 )
	{
		int result;

		/* chequear que las tarjetas existan */
		if( t1 ==null||t2==null||t3==null&&t1!=t2&&t2!=t3&&t3!=t1)
			return false;

		result = t1._type.getType() 
				+t2._type .getType()+ 
				t3._type.getType() ;

		boolean gotAWildCard = result > CardType.WILDCARD.getType();
		boolean gotAStreak = result==CardType.CANNON.getType() + CardType.SOLDIER.getType() + CardType.KNIGHT .getType();
		boolean gotCannons = result==CardType.CANNON.getType() * 3;
		boolean gotKnights = result==CardType.SOLDIER.getType()* 3;
		boolean gotSoldiers = result==CardType.KNIGHT.getType() * 3;
		return ( gotAWildCard || 
				gotAStreak||
				gotCannons ||
				gotKnights ||
				gotSoldiers );
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Card [" + _type + ", " + _country + "]";
	}


	public void setType(CardType type) {
		_type = type;
	}

	public void setCountry(Country country) {
		_country = country;
	}
}
