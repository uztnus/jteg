package org.maharshak.teg.util;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.util.ContinentAdapter.AdaptedContinent;

public class ContinentAdapter extends XmlAdapter<AdaptedContinent, Continent> {
	private static final String SEP = "_";

	@XmlRootElement
	public static class AdaptedContinent {

		private String _name;

		public AdaptedContinent(String name, int boardId) {
			super();

			_name = name + SEP + boardId;

		}

		public AdaptedContinent() {
		}

		@XmlAttribute
		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}

	}

	@Override
	public Continent unmarshal(AdaptedContinent v) throws Exception {
		String name = v.getName();
		String[] re = name.split(SEP);
		List<Continent> conts = Board.get(Integer.parseInt(re[1])).getConts();
		for (Continent continent : conts) {
			if (continent.getName().compareTo(re[0]) == 0)
				return continent;
		}
		return null;
	}

	@Override
	public AdaptedContinent marshal(Continent v) throws Exception {
		return new AdaptedContinent(v.getName(), v.getBoardId());
	}

}
