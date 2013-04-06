package org.maharshak.teg.common;





import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.maharshak.teg.player.Person;

public class Net {
	private static Logger _logger=Logger.getLogger("net");
	private static CharsetDecoder cd=Charset.defaultCharset().newDecoder();
	private static Map<Person,SocketChannel> _personToSocets=new HashMap<Person, SocketChannel>();
	private static Selector _sel;
	private static ServerSocketChannel _serverChannel;	

	private static int writen(Person p, ByteBuffer data) throws IOException{
		if(p==null)
			return -1;
		SocketChannel skt = _personToSocets.get(p);
		int end,start=data.position();
    end = start;

    if (skt != null) {

		skt.write(data);
		end=data.position();
    }
		return  end-start;
	}


	public static int net_readline( Person p, ByteBuffer buf, int maxlen)
	{
		SocketChannel skt = _personToSocets.get(p);
		int read;
		try {
			read = skt.read(buf);
			buf.position(0);
			if(read==-1)
				return read;
			buf.limit(read);
			CharBuffer decode = cd.decode(buf);
			String string = decode.toString();
			_logger.debug("From "+p+":"+string.replace("\n", "#"));
			buf.position(0);
		} catch (IOException e) {
			_logger.error("Error in reading from client "+p, e);
			return -1;
		}
		return read ;
	}


	public static void accept(SelectionKey selKey) throws IOException {

		// For an accept to be pending the channel must be a server socket channel.
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selKey.channel();

		// Accept the connection and make it non-blocking
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.configureBlocking(false);

		// Register the new SocketChannel with our Selector, indicating
		// we'd like to be notified when there's data waiting to be read
		socketChannel.register(_sel, SelectionKey.OP_READ);
		Person p=addSocket(socketChannel);
		_logger.info("Accepted new client  "+p+" from "+socketChannel.socket().getRemoteSocketAddress());
	}


	public static int	net_printf(Person p, String format, Object ... data)
	{
		return net_print(p,String.format(format, data));
	}

	public static int net_print(Person p, String msg)
	{

		try {
			_logger.debug("To   "+p+":"+msg.replace("\n", "#"));
			return writen(p,ByteBuffer.wrap(msg.getBytes()));
		} catch (IOException e) {
			_logger.error("Error in writing to client "+p, e);
			return -1;
		}
	}




	public static void net_close( Person p ) throws IOException
	{
		if( p!=null ){

			SocketChannel socketChannel = _personToSocets.get(p);
			if(socketChannel!=null){
				socketChannel.close();
				_logger.debug("Closed connection to "+p+" "+socketChannel.socket().getRemoteSocketAddress());
			}
		}
	}


	public static SocketChannel getSocket(Person p) {
		return _personToSocets.get(p);
	}




	public static Person getPerson(SocketChannel socket) {
		for (Person p : _personToSocets.keySet()) {
			if(_personToSocets.get(p)==socket)
				return p;
		}
		return null;
	}

	private static Person addSocket(SocketChannel socketChannel) {

		Person p=new Person(socketChannel);
		_personToSocets.put(p, socketChannel);
		return p;
	}





	public static void createSelector() throws IOException {
		_sel = Selector.open();

	}
	public static void createServerSocket(String host, int port)
			throws IOException {
		_serverChannel = ServerSocketChannel.open();
		_serverChannel.configureBlocking(false);

		// Bind the server socket to the specified address and port
		InetSocketAddress isa = new InetSocketAddress(host, port);
		_serverChannel.socket().bind(isa);
		_serverChannel.register(_sel, SelectionKey.OP_ACCEPT);
	}


	public static Iterator<SelectionKey> listen(int i) throws IOException {
		int got=_sel.select(i);
		return _sel.selectedKeys().iterator();
	}



	public static List<String> decodeMessageToStrings(ByteBuffer b) {
		List<String> lines=new ArrayList<String>();
		try {
			CharsetDecoder cd = Net.cd;
			CharBuffer decode = cd.decode(b);
			String string = decode.toString();
			lines.addAll(Arrays.asList(string.split("\n")));
		} catch (CharacterCodingException e) {
			_logger.error("failed to parse",e);
		}
		return lines;
	}

	public static String returnCorrectSecondValue(String[] tmp) {
		String value;
		switch (tmp.length) {
			case 2:
				value=tmp[1];
				break;

			case 3:
				value=tmp[1]+"="+tmp[2];
				break;	
			default:
				value=null;;
		}
		return value;
	}


}
