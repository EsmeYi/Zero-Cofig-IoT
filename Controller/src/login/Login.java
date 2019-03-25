package login;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Properties;

public class Login {
	private String username;
	private String password;
	private String secretKey;
	private static String result;

	public static String getResult() {
		return result;
	}

	public static void setResult(String result) {
		Login.result = result;
	}

	public Login(String un, String ps) {
		username = un;
		password = ps;
		String userInfo = username + "-" + password;
		secretKey = getKeyFromProperties();
		String encryptUser = new AES().encrypt(userInfo, secretKey);

		try {
			Socket socket = new Socket("127.0.0.1", 56569);
			OutputStream outputStream = socket.getOutputStream();
			PrintWriter printWriter = new PrintWriter(outputStream);
			printWriter.write(encryptUser);
			printWriter.flush();
			socket.shutdownOutput();

			InputStream inputStream = socket.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String infoFromServer = null;
			while ((infoFromServer = bufferedReader.readLine()) != null) {
				System.out.println(NowTime.now() + " Client receives server information: " + infoFromServer);
				result = infoFromServer;
			}
			socket.shutdownInput();

			bufferedReader.close();
			inputStream.close();
			printWriter.close();
			outputStream.close();
			socket.close();
		} catch (UnknownHostException e1) {
			result = "conError";
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	}

	public String getKeyFromProperties() {
		// load userInfo.properties file
		InputStream in = Login.class.getResourceAsStream("sys-conf.properties");
		Properties p = new Properties();
		if (in != null) {
			try {
				p.load(in);
				String key = p.getProperty("secretKey");
				return key;
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Properties file not found!");
		}
		return null;
	}

}
