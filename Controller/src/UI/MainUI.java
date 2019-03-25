package UI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import serviceDiscovery.ServiceDiscovery;

public class MainUI extends JFrame {

	private JPanel contentPane;
	private JButton lightButton;
	private JButton coffeeButton;
	private JButton printerButton;
	private JButton washerButton;
	private JButton tvButton;
	private String username;

	public MainUI(String username) {
		this.username = username;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(700, 450);
		setTitle("Devices");
		setResizable(false);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel devicePanel = new JPanel(new GridLayout(3, 3, 30, 30));
		devicePanel.setBackground(UIManager.getColor("CheckBox.background"));
		contentPane.add(devicePanel, BorderLayout.CENTER);

		// clock
		JButton clockButton = new JButton(new ImageIcon("src/image/clock.png"));
		devicePanel.add(clockButton);
		clockButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ServiceDiscovery("_clock._tcp", "Clock", username).setVisible(true);
			}
		});

		// light
		lightButton = new JButton(new ImageIcon("src/image/light.png"));
		devicePanel.add(lightButton);
		lightButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ServiceDiscovery("_light._tcp", "Light", username).setVisible(true);
			}

		});

		// coffee
		coffeeButton = new JButton(new ImageIcon("src/image/coffee.png"));
		devicePanel.add(coffeeButton);
		coffeeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ServiceDiscovery("_coffee._tcp", "Coffee Maker", username).setVisible(true);
			}

		});

		// printer
		printerButton = new JButton(new ImageIcon("src/image/printer.png"));
		devicePanel.add(printerButton);
		printerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ServiceDiscovery("_printer._tcp", "Printer", username).setVisible(true);
			}

		});

		// washer
		washerButton = new JButton(new ImageIcon("src/image/washer.png"));
		devicePanel.add(washerButton);
		washerButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ServiceDiscovery("_washer._tcp", "Washer", username).setVisible(true);
			}

		});

		// tv
		tvButton = new JButton(new ImageIcon("src/image/tv.png"));
		devicePanel.add(tvButton);
		tvButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				new ServiceDiscovery("_tv._tcp", "TV", username).setVisible(true);
			}

		});

		JPanel northPanel = new JPanel();
		contentPane.add(northPanel, BorderLayout.NORTH);

		JPanel westPanel = new JPanel();
		contentPane.add(westPanel, BorderLayout.WEST);

		JPanel eastPanel = new JPanel();
		contentPane.add(eastPanel, BorderLayout.EAST);

		JPanel southPanel = new JPanel();
		contentPane.add(southPanel, BorderLayout.SOUTH);
	}

}
