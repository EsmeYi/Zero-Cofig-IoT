package serviceDiscovery;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.apple.dnssd.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.*;
import java.nio.channels.SocketChannel;

public class ControllLight extends JPanel implements ResolveListener, Runnable{

	private String requestInfo;
	private String username;
	private String host;
	private int port;
	SocketChannel channel;

	public ControllLight(){
		initPanel();
	}
	
	public ControllLight(String us, ServiceDiscovery sd, SocketChannel c) {
		initPanel();
		sd.setControllPane(this);
		username = us;
		channel = c;
		if (channel != null)
			new Thread(this).start();
	}
	
	@Override
	public void run() {
		
	}

	public void serviceResolved(DNSSDService resolver, int flags, int ifIndex, String fullName, String theHost,
			int thePort, TXTRecord txtRecord) {
		host = theHost;
		port = thePort;
		resolver.stop();
	}

	public void sendRequestInfo(String requestInfo) {
		try {
			InetSocketAddress socketAddress = new InetSocketAddress(host, port);
			channel = SocketChannel.open(socketAddress);
			ByteBuffer buffer = ByteBuffer.allocate(4 + 128);
			CharBuffer charBuffer = buffer.asCharBuffer();
			buffer.putInt(0, requestInfo.length());
			charBuffer.position(2);
			charBuffer.put(requestInfo);
			System.out.println( " connection begin: " + System.currentTimeMillis());
			channel.write(buffer);
			new Thread().start();
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}

	@Override
	public void operationFailed(DNSSDService arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void initPanel(){
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
		this.setLayout(new BorderLayout(0, 0));
		JPanel lightPane = new JPanel(new GridLayout(2, 2, 30, 30));
		lightPane.setBackground(UIManager.getColor("CheckBox.background"));
		this.add(lightPane, BorderLayout.CENTER);

		JButton off = new JButton(new ImageIcon("src/image/lightoff.png"));
		lightPane.add(off);
		off.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {	
				requestInfo = username + " turn light off";
				sendRequestInfo(requestInfo);
			}

		});

		JButton on = new JButton(new ImageIcon("src/image/lightOn.png"));
		lightPane.add(on);
		on.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				requestInfo = username + " turn light on";
				sendRequestInfo(requestInfo);
			}

		});

		JButton up = new JButton(new ImageIcon("src/image/lightUp.png"));
		lightPane.add(up);
		up.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				requestInfo = username + " turn light up";
				sendRequestInfo(requestInfo);
			}

		});

		JButton down = new JButton(new ImageIcon("src/image/lightDown.png"));
		lightPane.add(down);
		down.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				requestInfo = username + " turn light down";
				sendRequestInfo(requestInfo);
			}

		});

	}
}
