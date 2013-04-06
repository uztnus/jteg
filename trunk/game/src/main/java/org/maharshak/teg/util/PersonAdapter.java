package org.maharshak.teg.util;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.maharshak.teg.player.Person;
import org.maharshak.teg.util.PersonAdapter.AdaptedPerson;

public class PersonAdapter extends XmlAdapter<AdaptedPerson, Person> {

	public static class AdaptedPerson {
		private String _uuid;

		public AdaptedPerson() {
		}
		public AdaptedPerson(String uuid) {
			super();
			_uuid = uuid;
		}

		public void setUuid(String uuid) {
			_uuid = uuid;
		}

		@XmlAttribute
		public String getUuid() {
			return _uuid;
		}
	}

	@Override
	public Person unmarshal(AdaptedPerson v) throws Exception {
		return new Person(v.getUuid());
	}

	@Override
	public AdaptedPerson marshal(Person v) throws Exception {
		return new AdaptedPerson(v.getId());
	}
	
}
