package org.maharshak.teg.board;

import java.io.Serializable;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.util.ContinentAdapter;





@XmlJavaTypeAdapter(ContinentAdapter.class)
public class Continent implements Serializable{

	private final String _name;
	private final Board _board;	


	
	public Continent(String name,Board b) {
		_name = name;
		_board=b;
	}


	public int getChips() {
		return _board.getChipsPerContinent(this);
	}


	public String getName() {
		return _name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format(
				"Cont [_name=%s,  fichas_x_cont=%s]", _name,
				getChips());
	}

	public int getBoardId() {
		return _board.getId();
	}
}
