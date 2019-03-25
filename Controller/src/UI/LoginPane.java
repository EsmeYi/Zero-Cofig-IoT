package UI;

import java.awt.EventQueue;
import java.awt.event.*;
import java.awt.Color;

import javax.swing.*;
import javax.swing.border.*;

import login.Login;

public class LoginPane extends JFrame {

	private JPanel contentPane;
	private JPasswordField passWord;
	private JTextField userName;
	private static String ps;
	private static String us;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginPane frame = new LoginPane();
					frame.setVisible(true);
					frame.setVisible(true);
					frame.setResizable(false);
					frame.setLocationRelativeTo(null);
					frame.setTitle(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public LoginPane() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 430);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel photo = new JLabel("photo");
		photo.setBounds(95, 50, 110, 110);
		photo.setIcon(new ImageIcon(LoginPane.class.getResource("/image/Login.png")));
		contentPane.add(photo);
		
		userName = new JTextField();
		userName.setText("username");
		userName.setBounds(35, 180, 230, 35);
		userName.setBackground(new Color(245, 245, 245));
		userName.setBorder(new EmptyBorder(0, 5, 0, 5));
		contentPane.add(userName);
		userName.setColumns(10);

		passWord = new JPasswordField();
		passWord.setColumns(10);
		passWord.setBackground(new Color(245, 245, 245));
		passWord.setBounds(35, 239, 230, 35);
		passWord.setBorder(new EmptyBorder(0, 5, 0, 5));
		passWord.setText("password");
		contentPane.add(passWord);

		JButton loginBtn = new JButton("LOGIN");
		contentPane.add(loginBtn);
		loginBtn.setForeground(Color.DARK_GRAY);
		loginBtn.setBackground(new Color(255, 255, 255));
		loginBtn.setBounds(35, 313, 230, 35);
		loginBtn.setBorder(new LineBorder(Color.DARK_GRAY));
		
		JLabel note = new JLabel("");
		note.setBounds(35, 361, 230, 16);
		contentPane.add(note);

		loginBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				ps = String.valueOf(passWord.getPassword());
				us = userName.getText();
				new Login(us, ps);
				String result = Login.getResult();
				
				if(result.equals("pass")){
					setVisible(false);
					new MainUI(us).setVisible(true);
					dispose();
				}
				if(result.equals("conError")){
					note.setText("Failed to connect server!");
				}
				else{
					note.setText("Wrong user name or password!");
				}

			}

		});

	}
}
