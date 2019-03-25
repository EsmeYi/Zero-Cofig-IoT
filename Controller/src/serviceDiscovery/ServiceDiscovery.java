package serviceDiscovery;

import java.awt.*;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.event.*;

import com.apple.dnssd.*;

public class ServiceDiscovery extends JFrame implements Runnable, BrowseListener, ListSelectionListener {

	private String ServiceType;
	private String DeviceName;
	private String UserName;
	private JPanel mainPane;
	private JPanel leftPane;
	private JPanel rightPane;
	private JPanel controllPane = new JPanel();
	
	public JPanel getControllPane() {
		return controllPane;
	}

	public void setControllPane(JPanel conDev) {
		controllPane.removeAll();
		controllPane.add(conDev, BorderLayout.CENTER);
	}

	private JLabel serviceDetail = new JLabel();
	private DefaultListModel<DiscoveredInstance> deviceList;
	private JList<DiscoveredInstance> devices;

	public ServiceDiscovery(String serviceType, String deviceName, String userName) {
		serviceDetail.setForeground(Color.DARK_GRAY);

		try {
			this.ServiceType = serviceType;
			this.DeviceName = deviceName;
			this.UserName=userName;	
			initPanel(deviceDiscovery());
		} catch (DNSSDException e) {
			e.printStackTrace();
		}

	}

	//
	public void operationFailed(DNSSDService arg0, int arg1) {
		System.exit(-1);
	}

	private JList<DiscoveredInstance> deviceDiscovery() throws DNSSDException {

		deviceList = new DefaultListModel<DiscoveredInstance>();
		devices = new JList<DiscoveredInstance>(deviceList);
		devices.setVisibleRowCount(20);
		devices.setFont(new Font("Geneva", Font.BOLD, 13));
		devices.addListSelectionListener(this);
		System.out.println(" begin: " + System.currentTimeMillis());
		DNSSD.browse(ServiceType, this);
		return devices;
	}

	// When the user click on the list, initiate a outgoing connection to the
	// service
	public void valueChanged(ListSelectionEvent e) {
		int selected = devices.getSelectedIndex();

		if (selected != -1) {
			controllPane.removeAll();
			DiscoveredInstance x = (DiscoveredInstance) devices.getSelectedValue();
			x.resolve(new ControllLight(UserName, this, null));
			new Thread(new Runnable() {
				
				public void run() {
					serviceDetail.setText(x.getServiceName());
				}
				
			}).start();

		}
	}

	@Override
	public void serviceFound(DNSSDService browser, int flags, int ind, String name, String type, String domain) {
		System.out.println(" found: " + System.currentTimeMillis());
		DiscoveredInstance x = new DiscoveredInstance(ind, name, domain);
		try {
			SwingUtilities.invokeAndWait(new Adder(x));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void serviceLost(DNSSDService browser, int flags, int ind, String name, String regType, String domain) {
		DiscoveredInstance x = new DiscoveredInstance(ind, name, domain);
		try {
			SwingUtilities.invokeAndWait(new Remover(x));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {

	}

	// The Adder and Remover classes update the list of discovered instances
	private class Adder implements Runnable {
		private DiscoveredInstance add;

		public Adder(DiscoveredInstance a) {
			add = a;
		}

		public void run() {
			deviceList.addElement(add);
		}
	}

	private class Remover implements Runnable {
		private DiscoveredInstance rmv;

		public Remover(DiscoveredInstance r) {
			rmv = r;
		}

		public void run() {
			String name = rmv.toString();
			for (int i = 0; i < deviceList.size(); i++) {
				if (deviceList.getElementAt(i).toString().equals(name)) {
					deviceList.removeElementAt(i);
					return;
				}
			}
		}
	}

	// Our inner class DiscoveredInstance has two special properties
	// It has a custom toString() method to display discovered
	// instances the way we want them to appear, and a resolve()
	// method, which asks it to resolve the named service it represents
	// and pass the result to the specified ResolveListener
	public class DiscoveredInstance {
		private int ind;
		private String name, domain;

		public DiscoveredInstance(int i, String n, String d) {
			ind = i;
			name = n;
			domain = d;
		}

		public void resolve(ResolveListener a) {
			try {
				DNSSD.resolve(0, ind, name, ServiceType, domain, a);
			} catch (DNSSDException e) {
				e.printStackTrace();
			}
		}

		public String toString() {
			String i = DNSSD.getNameForIfIndex(ind);
			return (i + " " + name + " (" + domain + ")");
		}
		 public String getServiceName(){
			 return name;
		 }
	}
	
	// initiate the main panel
	private void initPanel(JList<DiscoveredInstance> devices) {
		setSize(700, 450);
		setTitle(DeviceName);
		setResizable(false);
		setLocationRelativeTo(null);
		mainPane = new JPanel();
		mainPane.setBackground(UIManager.getColor("Button.background"));
		getContentPane().add(mainPane, BorderLayout.CENTER);

		leftPane = new JPanel();
		leftPane.setBackground(new Color(255, 255, 255));

		rightPane = new JPanel();
		rightPane.setBackground(UIManager.getColor("Button.background"));
		GroupLayout gl_mainPane = new GroupLayout(mainPane);
		gl_mainPane.setHorizontalGroup(gl_mainPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPane.createSequentialGroup().addGap(36)
						.addComponent(leftPane, GroupLayout.PREFERRED_SIZE, 227, GroupLayout.PREFERRED_SIZE).addGap(39)
						.addComponent(rightPane, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(48, Short.MAX_VALUE)));
		gl_mainPane.setVerticalGroup(gl_mainPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_mainPane.createSequentialGroup().addGap(44)
						.addGroup(gl_mainPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_mainPane.createSequentialGroup()
										.addComponent(leftPane, GroupLayout.PREFERRED_SIZE, 341,
												GroupLayout.PREFERRED_SIZE)
										.addContainerGap())
								.addGroup(gl_mainPane.createSequentialGroup()
										.addComponent(rightPane, GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
										.addContainerGap(43, Short.MAX_VALUE)))));

		JScrollPane scrollPane = new JScrollPane(devices);
		scrollPane.setPreferredSize(new Dimension(200, 320));
		leftPane.add(scrollPane, BorderLayout.CENTER);
		mainPane.setLayout(gl_mainPane);
		GroupLayout gl_rightPane = new GroupLayout(rightPane);
		gl_rightPane.setHorizontalGroup(gl_rightPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_rightPane.createSequentialGroup()
						.addGroup(gl_rightPane.createParallelGroup(Alignment.LEADING)
								.addComponent(controllPane, GroupLayout.PREFERRED_SIZE, 350, GroupLayout.PREFERRED_SIZE)
								.addGroup(gl_rightPane.createSequentialGroup().addGap(66).addComponent(serviceDetail,
										GroupLayout.PREFERRED_SIZE, 215, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		gl_rightPane.setVerticalGroup(gl_rightPane.createParallelGroup(Alignment.LEADING).addGroup(gl_rightPane
				.createSequentialGroup()
				.addComponent(serviceDetail, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE).addGap(12)
				.addComponent(controllPane, GroupLayout.PREFERRED_SIZE, 285, GroupLayout.PREFERRED_SIZE).addGap(1)));
		controllPane.setLayout(new BorderLayout(0, 0));
		rightPane.setLayout(gl_rightPane);
	}

}
