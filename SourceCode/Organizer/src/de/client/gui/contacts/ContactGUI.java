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
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import javax.swing.BoxLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ContactGUI extends JPanel {
	private JEditorPane editorPane;
	private JEditorPane dtrpnEins;
	private JTable table;
	JScrollPane scrollPane_1;
	private JLabel lblCreateANew;
	private JLabel lblGivenName;
	private JLabel lblFamilyName;
	private JTextField tfGivenName;
	private JTextField tfFamilyName;
	private JLabel lblEmail;
	private JTextField tfEmail;
	private JTextField tfPhone;
	private JLabel lblPhone;
	private JButton btnSubmit;
	private JButton btnClear;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public ContactGUI(final String username, final String pass, final Client client, final URI uri) {
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				
				int width=(int) e.getComponent().getSize().getWidth();
				int height=(int) e.getComponent().getSize().getHeight();

				scrollPane_1.setSize(width, height-120);
				panel.setBounds(panel.getX(), scrollPane_1.getSize().height, panel.getWidth(), panel.getHeight());
				System.out.println(scrollPane_1.getSize().height);
			}
		});
		DefaultTableModel model = new DefaultTableModel(); 
		table = new JTable(model); 

		// Create a couple of columns 
		model.addColumn("Given Name"); 
		model.addColumn("Family Name"); 
		model.addColumn("E-Mail");
		model.addColumn("Phone");
		setLayout(null);
		 
		 panel = new JPanel();
		 panel.setBounds(0, 215, 450, 117);
		 add(panel);
		 panel.setLayout(null);
		 
		 lblCreateANew = new JLabel("Create a new contact");
		 lblCreateANew.setBounds(10, 11, 141, 16);
		 panel.add(lblCreateANew);
		 lblCreateANew.setFont(new Font("Tahoma", Font.BOLD, 13));
		 
		 lblGivenName = new JLabel("Given name:");
		 lblGivenName.setBounds(10, 38, 83, 14);
		 panel.add(lblGivenName);
		 
		 lblFamilyName = new JLabel("Family name:");
		 lblFamilyName.setBounds(10, 63, 83, 14);
		 panel.add(lblFamilyName);
		 
		 btnSubmit = new JButton("Submit");
		 btnSubmit.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		JSONObject usernamePassAndContact=new JSONObject();
		 		try {
					usernamePassAndContact.put("username", username);
					usernamePassAndContact.put("password", pass);
					usernamePassAndContact.put("givenName", tfGivenName.getText());
					usernamePassAndContact.put("familyName", tfFamilyName.getText());
					usernamePassAndContact.put("email", tfEmail.getText());
					usernamePassAndContact.put("phone", tfPhone.getText());

					WebResource res=client.resource(uri).path("Contact").path("CreateContactJSON");

				    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernamePassAndContact);
				    System.out.println(resp.getStatus());
				    
				} catch (JSONException e1) {e1.printStackTrace();}
		 		
		 	}
		 });
		 btnSubmit.setBounds(92, 88, 146, 23);
		 panel.add(btnSubmit);
		 
		 tfGivenName = new JTextField();
		 tfGivenName.setBounds(92, 38, 146, 20);
		 panel.add(tfGivenName);
		 tfGivenName.setColumns(10);
		 
		 tfFamilyName = new JTextField();
		 tfFamilyName.setBounds(92, 63, 146, 20);
		 panel.add(tfFamilyName);
		 tfFamilyName.setColumns(10);
		 
		 lblEmail = new JLabel("Email:");
		 lblEmail.setBounds(248, 38, 46, 14);
		 panel.add(lblEmail);
		 
		 lblPhone = new JLabel("Phone:");
		 lblPhone.setBounds(248, 63, 46, 14);
		 panel.add(lblPhone);
		 
		 tfPhone = new JTextField();
		 tfPhone.setBounds(294, 63, 146, 20);
		 panel.add(tfPhone);
		 tfPhone.setColumns(10);
		 
		 tfEmail = new JTextField();
		 tfEmail.setBounds(294, 38, 146, 20);
		 panel.add(tfEmail);
		 tfEmail.setColumns(10);
		 
		 btnClear = new JButton("Clear");
		 btnClear.setBounds(294, 88, 146, 23);
		 panel.add(btnClear);
		 btnClear.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 	}
		 });
		 scrollPane_1 = new JScrollPane( table );
		scrollPane_1.setBounds(0, 0, 450, 214);
		add(scrollPane_1);

		

		WebResource res=client.resource(uri).path("Contact").path("GetContactJSON");
		
		
	    JSONArray nameAndPass = new JSONArray();
	    nameAndPass.put(username);
	    nameAndPass.put(pass);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,nameAndPass);
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
