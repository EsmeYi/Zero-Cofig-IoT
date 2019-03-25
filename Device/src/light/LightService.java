package light;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Properties;

import com.apple.dnssd.*;

public class LightService implements RegisterListener, Runnable {

	private Properties p;
	private ServerSocketChannel listentingChannel;
	private int listentingPort;
	private static String serviceName;

	// Do the registration
	public LightService() throws DNSSDException, InterruptedException {

		// Load the description file
		InputStream in = LightService.class.getResourceAsStream("description.properties");
		p = new Properties();
		if (in != null) {
			try {
				// Get device properties
				p.load(in);
				String serviceType = p.getProperty("serviceType");
				String deviceName = p.getProperty("deviceName");
				String descriptionVers = p.getProperty("descriptionVers");
				TXTRecord txtRecord = new TXTRecord();
				txtRecord.set("txtvers", "1");
				txtRecord.set("descriptionvers", descriptionVers);
				// Make listening socket and advertise it
				// Binds the ServerSocket to a specific address (IP address and
				// port number).
				// If the address is null, then the system will pick up an
				// ephemeral port
				// and a valid local address to bind the socket.
				listentingChannel = ServerSocketChannel.open();
				listentingChannel.socket().bind(new InetSocketAddress(0));
				listentingPort = listentingChannel.socket().getLocalPort();
				System.out.println( " register begin: " + System.currentTimeMillis());
				DNSSDRegistration r = DNSSD.register(0, DNSSD.ALL_INTERFACES, deviceName, serviceType, null, null,
						listentingPort, txtRecord, this);
				
				new Thread(this).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Properties file not found!");
		}
	}

	// Display error message on failure
	public void operationFailed(DNSSDService service, int errorCode) {
		System.out.println("Services Registration failed " + errorCode);
	}

	// Display registered name on success
	public void serviceRegistered(DNSSDRegistration registration, int flags, String serviceName, String regType,
			String domain) {
		this.serviceName = serviceName;
		System.out.println("           #The service has registered");
		System.out.println("           Name  : " + serviceName);
		System.out.println("           Type  : " + regType);
		System.out.println("           Domain: " + domain);
		System.out.println( " register complete: " + System.currentTimeMillis());
	}

	public static void main(String[] args) {

		try {
			new LightService();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

	}

	// run() method sits and waits for incoming connection and simulate the
	// service
	public void run() {
		try {
			while (true) {
				SocketChannel sc = listentingChannel.accept();
				if (sc != null) {
					new Thread(new SimulateService(sc)).start();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// When the service receive an incoming connection, SimulateService reads
	// the
	// peer name from the connection and then
	private class SimulateService implements Runnable {

		private SocketChannel sc;

		public SimulateService(SocketChannel s) {
			sc = s;
		}

		public void run() {

			try {
				ByteBuffer buffer = ByteBuffer.allocate(4 + 128);
				CharBuffer charBuffer = buffer.asCharBuffer();
				sc.read(buffer);
				int length = buffer.getInt(0);
				char[] c = new char[length];
				charBuffer.position(2);
				charBuffer.get(c, 0, length);
				String requestInfo = new String(c);
				System.out.println( " connection established: " + System.currentTimeMillis());
				System.out.println(serviceName+" receives a command: "+requestInfo);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
