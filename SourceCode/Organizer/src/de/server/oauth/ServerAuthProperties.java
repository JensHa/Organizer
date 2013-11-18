package de.server.oauth;

import java.util.Arrays;
import java.util.Collection;

public class ServerAuthProperties {
	
	/**
	 * This ID identifies the application. It is set up at https://code.google.com/apis/console/ 
	 */
	public static final String CLIENT_ID="132303716062-vvvs438v6vb0etju6dqfljdnhqd38js8.apps.googleusercontent.com";
	
	public static final String CLIENT_SECRET="j-dIwL9p17lG3PndCCwgF_jy";
	
    public static final Collection<String> SCOPE = Arrays.asList(("https://www.googleapis.com/auth/userinfo.email;"
    				+"https://www.googleapis.com/auth/userinfo.profile;"
    				+"http://www.google.com/calendar/feeds/default/allcalendars/full;" //deprecated api 2.0
    				+"https://www.googleapis.com/auth/calendar;"
    				+"https://www.google.com/m8/feeds").split(";"));

    public static final String CALLBACK_URI="urn:ietf:wg:oauth:2.0:oob";	
    
    public static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    
    public static final String USER_CALENDAR_URL = "http://www.google.com/calendar/feeds/default/allcalendars/full";
    
    
	

}
