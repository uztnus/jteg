package org.maharshak.teg.common;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.maharshak.teg.common.Protocol.TOKEN;
import org.maharshak.teg.common.ref.Data;
import org.maharshak.teg.common.ref.TokenAction;


public class TokenActionFinder<T extends Data>{
	private Map<TOKEN,TokenAction> _actions=new HashMap<Protocol.TOKEN, TokenAction>();
	private Map<String, TOKEN> _maps=new HashMap<String, Protocol.TOKEN>();
 

	public TokenActionFinder(TokenAction[] tokenActions) {
		for (TokenAction ta: tokenActions) {
			add(ta.getToken(), ta);
		}
	}

	private void add(TOKEN t,TokenAction ta){
		_actions.put(t,ta);
		_maps.put(t.getToken(),t);
	}

	public TokenAction<T> find(String token){
		TOKEN t = _maps.get(token);
		if(t==null)
			return null;
		return _actions.get(t);
	}


	public Collection<TokenAction> getActions() {
		return _actions.values();
	}

}
