package de.client.gui.calendar;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JFrame;
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
	public static JPanel createPanel(JFrame parent, String username,String pass){
		
		ArrayList<CalendarEntry> entrylist = new ArrayList<CalendarEntry>();
		
		
		entrylist.add(new CalendarEntry("2013-12-24T18:00:00.000+07:00", "blablala thats a description"));
		
		//Set up the client
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		//res=client.resource(uri).path("Userinfo").path("GetUsersGender"); GetCalendarNewTry
		
		//Get all user Calendar
		res=client.resource(uri).path("Calendar").path("GetUsersCalendar");
		
		System.out.println("---------------------------");
		System.out.println();
	

	    JSONArray usernameAndID = new JSONArray();
	    usernameAndID.put(username);
	    usernameAndID.put(pass);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
		String responsestring=resp.getEntity(String.class);
		
		//Json is easier to parse
        org.json.JSONObject calendJson = XML.toJSONObject(responsestring);
        
        //ged an object out of the feed, that also gives access to the entries
        GoogleCalendarEntryFeed feed = new GoogleCalendarEntryFeed(calendJson);
     //   String jsonCalendar = calendJson.toString(4);
        
        entrylist=feed.getEntries();
//        System.out.println("json" + jsonCalendar);
	    
		//System.out.println("resp " + responsestring);
		//panel.add(new JLabel("hello world"+gender));
		

		//build panel
		panel = new CalendarPanel(parent,entrylist);
		
		panel.setVisible(true);
		return panel;
	}
	
}
