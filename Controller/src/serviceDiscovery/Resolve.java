package serviceDiscovery;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;

import com.apple.dnssd.*;

public class Resolve implements ResolveListener {

	private String host, username;
	private int port;
	SocketChannel channel;

	public Resolve(String name) {
		username = name;
	}

	public void operationFailed(DNSSDService arg0, int arg1) {

	}

	public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, String fullName, String theHost,
			int thePort, TXTRecord txtRecord) {
		host = theHost;
		port = thePort;
		ByteBuffer buffer = ByteBuffer.allocate(4 + 128);
		CharBuffer charBuffer = buffer.asCharBuffer();
		buffer.putInt(0, username.length());
		charBuffer.position(2);
		charBuffer.put(username);
		try {
			InetSocketAddress socketAddress = new InetSocketAddress(host, port);
			channel = SocketChannel.open(socketAddress);
			channel.write(buffer);
			new Thread().start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		resolver.stop();
	}

}
