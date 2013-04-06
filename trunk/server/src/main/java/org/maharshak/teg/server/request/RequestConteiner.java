package org.maharshak.teg.server.request;

import org.maharshak.teg.common.Net;
import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.player.Person;

public class RequestConteiner {

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " [" + _p + "| " + _value + "| " + _token + "]";
	}

	private Person _p;
	private String _value;
	private TOKEN _token;

	private RequestConteiner(Person p) {
		_p=p;
	}

	public static RequestConteiner createRequestConteiner(String line,Person p){
		RequestConteiner res=new RequestConteiner(p);
		
		String[] tmp = line.split("=");
		String token = tmp[0];
		res.setToken(TOKEN.getToken(token));
		res.setValue( Net.returnCorrectSecondValue(tmp));

		return res;
	}

	private void setToken(TOKEN token) {
		_token=token;
	}

	private void setValue(String value) {
		_value=value;
	}

	/**
	 * @return the fd
	 */
	public Person getPerson() {
		return _p;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return _value;
	}

	/**
	 * @return the token
	 */
	public TOKEN getToken() {
		return _token;
	}
	
	
}
