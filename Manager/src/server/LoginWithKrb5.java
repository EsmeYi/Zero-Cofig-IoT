package server;

import java.util.HashMap;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import com.sun.security.auth.module.Krb5LoginModule;

public class LoginWithKrb5 {
	
	private String user;
	private String pass;
	private Subject subject;
	
	public LoginWithKrb5(String user, String pass){
		this.user = user;
		this.pass = pass;
	}
	
	public boolean login () throws LoginException{
		Krb5LoginModule krb5 = new Krb5LoginModule();
		Map<String, String> map = new HashMap<>();
		Map<String, Object> shared = new HashMap<>();
	    if (pass != null) {
	        map.put("useFirstPass", "true");
	        shared.put("javax.security.auth.login.name", user);
	        shared.put("javax.security.auth.login.password", pass);
	    } else {
	        map.put("doNotPrompt", "true");
	        map.put("useTicketCache", "true");
	        if (user != null) {
	            map.put("principal", user);
	        }
	    }
        map.put("storeKey", "true");
	    krb5.initialize(subject, null, shared, map);
	    krb5.login();
	    krb5.commit();
		return true;
		
	}
	
//	public static Context fromUserPass(Subject s,
//	        String user, char[] pass, boolean storeKey) throws Exception {
//	    Context out = new Context();
//	    out.name = user;
//	    out.s = s;
//	    Krb5LoginModule krb5 = new Krb5LoginModule();
//	    Map<String, String> map = new HashMap<>();
//	    Map<String, Object> shared = new HashMap<>();
//
//	    if (pass != null) {
//	        map.put("useFirstPass", "true");
//	        shared.put("javax.security.auth.login.name", user);
//	        shared.put("javax.security.auth.login.password", pass);
//	    } else {
//	        map.put("doNotPrompt", "true");
//	        map.put("useTicketCache", "true");
//	        if (user != null) {
//	            map.put("principal", user);
//	        }
//	    }
//	    if (storeKey) {
//	        map.put("storeKey", "true");
//	    }
//
//	    krb5.initialize(out.s, null, shared, map);
//	    krb5.login();
//	    krb5.commit();
//	    return out;
//	}

}
