package org.maharshak.teg.board;


import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.util.CountryAdapter;


@XmlJavaTypeAdapter(CountryAdapter.class)
public class Country {

	private final Board _b;
	private final String _name;

	public Country( String name,  Board b) {
		_name =name;
		_b=b;
	}

	public Continent getContinent() {
		return _b.getContinet(this);
	}

	public String getName() {
		return _name;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Country [" + _name + "| " + getContinent() + "]";
	}

	public int getBoardId() {
		return _b.getId();
	}

}
