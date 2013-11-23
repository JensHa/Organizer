package de.client.gui.contacts;

import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.client.contacts.UserContacts;
import de.server.oauth.AuthHelper;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class ContactGUI extends JPanel {
	private JEditorPane editorPane;
	private JScrollPane scrollPane;
	private JEditorPane dtrpnEins;
	private JTable table;

	/**
	 * Create the panel.
	 */
	public ContactGUI(String username, String sessionID, Client client, URI uri) {
		setLayout(new BorderLayout(0, 0));
		DefaultTableModel model = new DefaultTableModel(); 
		table = new JTable(model); 

		// Create a couple of columns 
		model.addColumn("Col1"); 
		model.addColumn("Col2"); 
		model.addColumn("col3");
		model.addColumn("col4");
		// Append a row 
		model.addRow(new Object[]{"Given Name", "Family Name", "E-Mail", "Phone"});
		add(table, BorderLayout.CENTER);
		
		

		WebResource res=client.resource(uri).path("Contact").path("GetContactJSON");
		
		
	    JSONArray usernameAndID = new JSONArray();
	    usernameAndID.put(username);
	    usernameAndID.put(sessionID);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndID);
		JSONObject userContactsInJSON = resp.getEntity(JSONObject.class);
		try {
			System.out.println(userContactsInJSON.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UserContacts userContacts = new UserContacts(userContactsInJSON);
		
		for(int i=0;i<userContacts.getUserContacts().size();i++)
		{
			System.out.println("vorname: " + userContacts.getUserContacts().get(i).getGivenName() + " nachname: "+ userContacts.getUserContacts().get(i).getFamilyName() + " email: "+userContacts.getUserContacts().get(i).getEmail() + " phone: " + userContacts.getUserContacts().get(i).getPhone());
			model.addRow(new Object[]{userContacts.getUserContacts().get(i).getGivenName(), userContacts.getUserContacts().get(i).getFamilyName(), userContacts.getUserContacts().get(i).getEmail(), userContacts.getUserContacts().get(i).getPhone()});
		}

	}
}
