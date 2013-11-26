package de.client.gui.calendar;

import java.net.URI;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;

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
	 * @param sessionID the id of the session
	 * @author adrian
	 */
	public static JPanel createPanel(String username,String sessionID){
		
		LinkedList<CalendarEntry> entrylist = new LinkedList<CalendarEntry>();
		entrylist.add(new CalendarEntry(null, "blablala thats a description"));
		panel = new CalendarPanel(entrylist);
		//Set up the client
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		//res=client.resource(uri).path("Userinfo").path("GetUsersGender");
		res=client.resource(uri).path("Calendar").path("GetUsersCalendar");
		
	

	    JSONArray usernameAndID = new JSONArray();
	    usernameAndID.put(username);
	    usernameAndID.put(sessionID);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
		String gender=resp.getEntity(String.class);
		System.out.println("resp " + gender);
		//panel.add(new JLabel("hello world"+gender));
		
		
		
		panel.setVisible(true);
		return panel;
	}
	
}
