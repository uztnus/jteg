package org.maharshak.teg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.maharshak.teg.board.Country;
import org.maharshak.teg.util.PutAdapter.AdaptedPutMap;

public class PutAdapter extends
		XmlAdapter<AdaptedPutMap, Map<Country, Integer>> {

	@XmlRootElement
	public static class AdaptedPutMap {
		private Map<Country, Integer> _armiesToPut;

		public AdaptedPutMap() {
			_armiesToPut = new HashMap<Country, Integer>();
		}

		public AdaptedPutMap(Map<Country, Integer> toPut) {
			_armiesToPut = toPut;
		}


		public Put[] getPut() {
			List<Put> res = new ArrayList<PutAdapter.AdaptedPutMap.Put>();
			for (Country c : _armiesToPut.keySet()) {
				res.add(new Put(c, _armiesToPut.get(c)));
			}
			return res.toArray(new Put[] {});
		}


		public void setPut(Put[] puts) {
			_armiesToPut = new HashMap<Country, Integer>();
			for (Put put : puts) {
				_armiesToPut.put(put.getCountry(), put.getAmount());
			}
		}

		@XmlRootElement
		@XmlType
		public static class Put {
			private Country _country;
			private int _amount;

			@XmlElement
			public Country getCountry() {
				return _country;
			}

			public void setCountry(Country country) {
				_country = country;
			}

			@XmlAttribute
			public int getAmount() {
				return _amount;
			}

			public void setAmount(int amount) {
				_amount = amount;
			}

			public Put(Country country, int amount) {
				super();
				_country = country;
				_amount = amount;
			}

			public Put() {
			}
		}
	}

	@Override
	public Map<Country, Integer> unmarshal(AdaptedPutMap v) throws Exception {
		return v._armiesToPut;
	}

	@Override
	public AdaptedPutMap marshal(Map<Country, Integer> v) throws Exception {
		return new AdaptedPutMap(v);
	}
}
