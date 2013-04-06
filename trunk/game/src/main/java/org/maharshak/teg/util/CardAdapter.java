package org.maharshak.teg.util;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.maharshak.teg.board.Board;
import org.maharshak.teg.board.Card;
import org.maharshak.teg.util.CardAdapter.AdaptedCard;

public class CardAdapter extends XmlAdapter<AdaptedCard, Card> {

	@XmlRootElement
	public static class AdaptedCard {

		private String _type;
		private String _country;


		public AdaptedCard(Card c) {
			_country = c.getCountry().getName();
			_type = c.getType().name();
		}

		public AdaptedCard() {
		}


		public String getType() {
			return _type;
		}

		public void setType(String type) {
			_type = type;
		}


		public String getCountry() {
			return _country;
		}

		public void setCountry(String country) {
			_country = country;
		}


	}

	@Override
	public Card unmarshal(AdaptedCard v) throws Exception {
		final Board board = Board.get(-1);
		Card c = board.getCard(board.getCountry(v.getCountry()));
		return c;
	}

	@Override
	public AdaptedCard marshal(Card v) throws Exception {

		return new AdaptedCard(v);
	}

}
