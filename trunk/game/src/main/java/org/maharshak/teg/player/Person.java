package org.maharshak.teg.player;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.maharshak.teg.util.PersonAdapter;


@XmlJavaTypeAdapter(PersonAdapter.class)
public class Person implements Serializable {

	private final UUID _pId;
	private String _addr;		/**< internet address */
	private String _fullAddr;
	
	public Person() {
		_pId=UUID.randomUUID();
	}

	public Person(String uuid) {
		_pId=UUID.fromString(uuid);
	}
	
	
	public Person(SocketChannel socketChannel) {
		this();
		setIPAddress(socketChannel);
	}

	public UUID getpId() {
		return _pId;
	}
	
	@XmlAttribute
	public String getId(){
		return getpId().toString();
	}


	
	/* find the internet address of a player */
	private void setIPAddress( SocketChannel sc )
	{
		_addr="Unknown";
		if(sc==null)
			return;
		InetSocketAddress remoteSocketAddress = (InetSocketAddress) sc.socket().getRemoteSocketAddress();
		_fullAddr=remoteSocketAddress.toString();
		_addr=remoteSocketAddress. getHostName().toString();
	}

	public String getAddr() {
		return _addr;
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((_pId == null) ? 0 : _pId.hashCode());
		return result;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (_pId == null) {
			if (other._pId != null)
				return false;
		} else if (!_pId.equals(other._pId))
			return false;
		return true;
	}

  /**
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "Person [" + _pId + "| " + _fullAddr + "]";
  }
	

	
}
