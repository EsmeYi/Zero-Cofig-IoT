package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
	
	private static ServerSocket serverSocket;
	private static String result = null;

	public static void main(String[] args) {

		try {
			// make a server socket to listening on the 56569 port number
			serverSocket = new ServerSocket(56569);
			System.out.println(NowTime.now() + " Server " + serverSocket + " now starts to monitor");
			while(true){
				Socket socket = serverSocket.accept();
				login(socket);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void login(Socket socket) throws IOException {
		
		Properties allUsers = loadProperties("userInfo.properties");
		String key = loadProperties("sys-conf.properties").getProperty("secretKey");
		System.out.println(key);
		InputStream inputStream = socket.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String infoFromClient = null;
		while ((infoFromClient = bufferedReader.readLine()) != null) {
			String userInfo = new AES().decrypt(infoFromClient, key);
			result = (allUsers.containsValue(userInfo))?"pass":"deny";
			System.out.println(NowTime.now() + "  username-password:" + userInfo);
			System.out.println(result);
		}
		socket.shutdownInput();

		OutputStream outputStream = socket.getOutputStream();
		PrintWriter printWriter = new PrintWriter(outputStream);
		printWriter.write(result);
		printWriter.flush();
		socket.shutdownOutput();

		printWriter.close();
		outputStream.close();
		bufferedReader.close();
		inputStreamReader.close();
		inputStream.close();
		socket.close();
	}
	
	public static Properties loadProperties(String fileName){
		//load properties file
		InputStream in = Server.class.getResourceAsStream(fileName);
		Properties p = new Properties();
		if (in != null) {
			try {
				p.load(in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Properties file not found!");
		}
		return p;
	}


}