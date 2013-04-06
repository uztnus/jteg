package org.maharshak.teg.common.ref;

import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.Protocol.TOKEN;

public abstract class AbstractTokenAction<T extends Data> implements TokenAction<T>{

	private TOKEN _token;
	private String _help;

	protected AbstractTokenAction(TOKEN t,String help) {
		_token=t;
		_help=help;
	}
	
	@Override
	public final TOKEN getToken() {
		return _token;
	}
	
	@Override
	public final String help() {
		return _help;
	}
	
	@Override
	public TEG_STATUS actView(T d) {
		return TEG_STATUS.TEG_STATUS_SUCCESS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("AbstractTokenAction [_token=%s, _help=%s]",
				_token, _help);
	}
	
	
}
