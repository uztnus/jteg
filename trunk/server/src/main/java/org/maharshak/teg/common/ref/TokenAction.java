package org.maharshak.teg.common.ref;

import org.maharshak.teg.common.TEG_STATUS;
import org.maharshak.teg.common.Protocol.TOKEN;

public interface TokenAction<T extends Data> {
	public TEG_STATUS act(T data);
	
	public TEG_STATUS actView(T data);
	
	public  TOKEN getToken();
	
	public String help();
}