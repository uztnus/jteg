package org.maharshak.teg.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.board.Continent;
import org.maharshak.teg.board.Country;
import org.maharshak.teg.util.BoardAdapter.AdaptedBoard;

public class BoardAdapter extends XmlAdapter<AdaptedBoard, Board> {

	@XmlRootElement
	public static class AdaptedBoard {

		private Board _board;

		public AdaptedBoard() {

		}

		public AdaptedBoard(Board v) {
			_board = v;
		}

		@XmlAttribute
		public int getId() {
			return _board.getId();
		}

		public void setId(int id) {
			_board = Board.get(id);
		}

		@XmlElement
		@XmlJavaTypeAdapter(CardAdapter.class)
		public List<Card> getCards() {
			return _board.getDeck();
		}

		@XmlElement
		public List<ContData> getContinents() {
			List<ContData> res = new ArrayList<BoardAdapter.ContData>();
			for (Continent c : _board.getConts()) {
				res.add(new ContData(c, _board.getCountries(c)));
			}
			return res;
		}

	}

	@XmlRootElement
	public static class ContData {

		private String _name;
		private List<Country> _countries;

		public ContData() {
		}

		public ContData(Continent c, List<Country> countries) {
			_name = c.getName();
			_countries = countries;
		}

		@XmlAttribute
		public String getName() {
			return _name;
		}

		@XmlElement
		public List<Country> getCountries() {
			return _countries;
		}

	}

	@Override
	public Board unmarshal(AdaptedBoard v) throws Exception {
		return Board.get(v.getId());
	}

	@Override
	public AdaptedBoard marshal(Board v) throws Exception {
		return new AdaptedBoard(v);
	}

}
