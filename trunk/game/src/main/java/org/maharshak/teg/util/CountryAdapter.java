package org.maharshak.teg.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.util.CountryAdapter.AdaptedCountry;

public class CountryAdapter extends XmlAdapter<AdaptedCountry, Country> {
	// private static final String SEP = "_";
	@XmlRootElement
	public static class AdaptedCountry {

		private String _name;
		private int _bid;

		public AdaptedCountry(String name, int boardId) {
			_name = name;
			_bid = boardId;
		}

		public AdaptedCountry() {
		}

		@XmlAttribute
		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}

		@XmlAttribute
		public int getBid() {
			return _bid;
		}

		public void setBid(int bid) {
			_bid = bid;
		}




	}

	@Override
	public Country unmarshal(AdaptedCountry v) throws Exception {
		final Country country = Board.get(v.getBid()).getCountry(v.getName());
		return country;
	}

	@Override
	public AdaptedCountry marshal(Country v) throws Exception {
		return new AdaptedCountry(v.getName(), v.getBoardId());
	}


}
