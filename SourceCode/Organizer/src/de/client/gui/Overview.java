package de.client.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;

import de.client.ClientAuthProperties;
import de.client.gui.contacts.ContactGUI;
import java.awt.Font;

public class Overview extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	 * Create the frame.
	 */
	public Overview(String username,String sessionID) {

		
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				
				int width=(int) e.getComponent().getSize().getWidth();
				int height=(int) e.getComponent().getSize().getHeight();

				tabbedPane.setSize(width,(int) (height-tabbedPane.getY()));
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 51, 434, 210);
		tabbedPane.addTab("Contacts", new ContactGUI(username,sessionID,client,uri));
		tabbedPane.addTab("test", new JPanel());
		//Add the Calendar tabb
//		JPanel calendar = CalendarGui.createPanel(username, sessionID);
//		tabbedPane.addTab("Calendar", calendar);

		contentPane.add(tabbedPane);
		
		res=client.resource(uri).path("Userinfo").path("GetUserMail");
		
	    JSONArray usernameAndID = new JSONArray();
	    usernameAndID.put(username);
	    usernameAndID.put(sessionID);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
		String userMail=resp.getEntity(String.class);
		
		lblNewLabel = new JLabel("Welcome " + username + "<" + userMail + ">");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(0, 11, 455, 14);
		
		contentPane.add(lblNewLabel);
	}
}
