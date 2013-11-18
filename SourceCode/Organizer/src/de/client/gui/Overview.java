package de.client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.net.URI;
import java.security.SecureRandom;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import de.client.ClientAuthProperties;
import de.client.gui.calendar.CalendarGui;
import de.client.gui.contacts.Contact;
import de.server.oauth.AuthHelper;
import de.server.resource.SecurityResource;

import javax.swing.JLabel;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;

public class Overview extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private JLabel lblNewLabel;

	private Client client;
	private String serverURI=ClientAuthProperties.ServerURI;
	private int port=ClientAuthProperties.ServerPort;
	private UriBuilder builder;
	private URI uri;
	private WebResource res;


	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Overview frame = new Overview();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public Overview(String username,String sessionID) {
		
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 471, 408);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 54, 455, 324);
		Contact contact = new Contact(username, sessionID);
		tabbedPane.addTab("Contacts", contact);
		contact.setLayout(null);
		tabbedPane.addTab("test1", new JPanel());
		//Add the Calendar tabb
		JPanel calendar = CalendarGui.createPanel(username, sessionID);
		tabbedPane.addTab("Calendar", calendar);

		contentPane.add(tabbedPane);
		
		
		res=client.resource(uri).path("Userinfo").path("GetUserMail");
		
	    JSONArray usernameAndID = new JSONArray();
	    usernameAndID.put(username);
	    usernameAndID.put(sessionID);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
		String userMail=resp.getEntity(String.class);
		
		lblNewLabel = new JLabel("Welcome " + username + "<" + userMail + ">");
		lblNewLabel.setBounds(0, 0, 455, 14);
		
		contentPane.add(lblNewLabel);
		
	}
}
