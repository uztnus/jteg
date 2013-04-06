package org.maharshak.teg.util;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.maharshak.teg.game.Mission;

public class MissionAdapter extends XmlAdapter<String, Mission> {

	@Override
	public Mission unmarshal(String v) throws Exception {
		for (Mission m : Mission.g_missions) {
			if (m.getName().compareTo(v) == 0)
				return m;
		}
		return null;
	}

	@Override
	public String marshal(Mission v) throws Exception {
		return v.getName();
	}

	public static void main(String[] args) {
		System.out.println(String.format("--%06d---", 213));
	}
}
