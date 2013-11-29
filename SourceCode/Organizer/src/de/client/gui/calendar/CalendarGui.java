package de.client.gui.calendar;

import java.net.URI;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.json.XML;

import com.google.api.client.googleapis.auth.clientlogin.ClientLogin.Response;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;

import de.client.ClientAuthProperties;

public class CalendarGui{// extends JPanel {

	private static Client client;
	private static String serverURI=ClientAuthProperties.ServerURI;
	private static int port=ClientAuthProperties.ServerPort;
	private static UriBuilder builder;
	private static URI uri;
	private static WebResource res;
	private static JPanel panel;
	
	/**
	 * Builds a new Calendar gui panel
	 * @param username the username of the user who is logged in
	 * @param pass the password of the session
	 * @author adrian
	 */
	public static JPanel createPanel(String username,String pass){
		
		LinkedList<CalendarEntry> entrylist = new LinkedList<CalendarEntry>();
		entrylist.add(new CalendarEntry(null, "blablala thats a description"));
		panel = new CalendarPanel(entrylist);
		//Set up the client
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		//res=client.resource(uri).path("Userinfo").path("GetUsersGender"); GetCalendarNewTry
		
		//Get all available Calendars
		res=client.resource(uri).path("Calendar").path("GetUsersCalendar");
		
		System.out.println("---------------------------");
		System.out.println();
	

	    JSONArray usernameAndID = new JSONArray();
	    usernameAndID.put(username);
	    usernameAndID.put(pass);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
		String responsestring=resp.getEntity(String.class);
	//	System.out.println("re1 " + responsestring);
		
		//Get private Calendar new try
//		res=client.resource(uri).path("Calendar").path("GetCalendarNewTry");
//	    resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
//	    String privatexml = resp.getEntity(String.class);
//	    System.out.println("re2 " + privatexml);

        org.json.JSONObject calendJson = XML.toJSONObject(responsestring);
        org.json.JSONArray entries = calendJson.getJSONArray("entry");
        
        Object entry = entries.get(0);
        if(entry != null)
        	System.out.println(entry.toString());
        else
        	System.out.println("entry is null");
        
        String jsonCalendar = calendJson.toString(4);
        
        System.out.println("json" + jsonCalendar);
	    
		//System.out.println("resp " + responsestring);
		//panel.add(new JLabel("hello world"+gender));
		
		
		
		panel.setVisible(true);
		return panel;
	}
	
}
