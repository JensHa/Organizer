package de.client.gui.contacts;


import java.net.URI;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import de.client.contacts.UserContacts;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ContactGUI extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private JScrollPane scrollPane_1;
	private JLabel lblCreateANew;
	private JLabel lblGivenName;
	private JLabel lblFamilyName;
	private JTextField tfGivenName;
	private JTextField tfFamilyName;
	private JLabel lblEmail;
	private JTextField tfEmail;
	private JTextField tfPhone;
	private JLabel lblPhone;
	private JButton btnCreate;
	private JButton btnDelete;
	private JPanel panel;
	private JButton btnRefresh;
	private UserContacts userContacts;
	private JButton btnEdit;

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
		
		setLayout(null);
		 
		 panel = new JPanel();
		 panel.setBounds(0, 215, 450, 117);
		 add(panel);
		 panel.setLayout(null);
		 
		 lblCreateANew = new JLabel("Create/ Edit/ Delete a contact");
		 lblCreateANew.setBounds(10, 11, 246, 16);
		 panel.add(lblCreateANew);
		 lblCreateANew.setFont(new Font("Tahoma", Font.BOLD, 13));
		 
		 lblGivenName = new JLabel("Given name:");
		 lblGivenName.setBounds(10, 38, 83, 14);
		 panel.add(lblGivenName);
		 
		 lblFamilyName = new JLabel("Family name:");
		 lblFamilyName.setBounds(10, 63, 83, 14);
		 panel.add(lblFamilyName);
		 
		 btnCreate = new JButton("Create");
		 btnCreate.addActionListener(new ActionListener() {
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
			//	    System.out.println(resp.getStatus());
				    
				} catch (JSONException e1) {e1.printStackTrace();}
		 		
				refreshTable(username, pass, client, uri);
				
				clearAll();

		 	}
		 });
		 btnCreate.setBounds(10, 88, 101, 23);
		 panel.add(btnCreate);
		 
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
		 
		 btnDelete = new JButton("Delete");
		 btnDelete.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		if(table.getSelectedRow()>-1)
		 		{
		 		JSONObject usernamePassAndID=new JSONObject();
		 		try {
		 			usernamePassAndID.put("username", username);
		 			usernamePassAndID.put("password", pass);
		 			usernamePassAndID.put("id", userContacts.getUserContacts().get(table.getSelectedRow()).getId());


					WebResource res=client.resource(uri).path("Contact").path("DeleteContactJSON");

				    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernamePassAndID);
			//	    System.out.println(resp.getStatus());
				    
				} catch (JSONException e1) {e1.printStackTrace();}
		 		
				refreshTable(username, pass, client, uri);
				clearAll();
		 		}
		 	}
		 });
		 btnDelete.setBounds(230, 88, 101, 23);
	 	 btnDelete.setEnabled(false);
		 panel.add(btnDelete);
		 
		 
		 btnRefresh = new JButton("Refresh");
		 btnRefresh.setBounds(339, 88, 101, 23);
		 btnRefresh.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
				refreshTable(username, pass, client, uri);
		 	}
		 });
		 panel.add(btnRefresh);
		 
		 btnEdit = new JButton("Edit");
		 btnEdit.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		
		 		JSONObject usernamePassIDContact=new JSONObject();
		 		try {
		 			usernamePassIDContact.put("username", username);
		 			usernamePassIDContact.put("password", pass);
		 			usernamePassIDContact.put("id", userContacts.getUserContacts().get(table.getSelectedRow()).getId());
		 			usernamePassIDContact.put("givenName", tfGivenName.getText());
		 			usernamePassIDContact.put("familyName",tfFamilyName.getText());
		 			usernamePassIDContact.put("email", tfEmail.getText());
		 			usernamePassIDContact.put("phone", tfPhone.getText());




					WebResource res=client.resource(uri).path("Contact").path("UpdateContactJSON");

				    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernamePassIDContact);
			//	    System.out.println(resp.getStatus());
				    
				} catch (JSONException e1) {e1.printStackTrace();}
		 		
				refreshTable(username, pass, client, uri);
				clearAll();
		 	}
		 });
		 btnEdit.setBounds(120, 88, 101, 23);
 		 btnEdit.setEnabled(false);
		 panel.add(btnEdit);

		table = new JTable(); 
		refreshTable(username, pass, client, uri);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(
            new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
            	
            	//Needed because valueChanged "fires" two times => Mouse pressed AND released
            	if(table.getSelectedRow()>-1)
            	{
            		btnDelete.setEnabled(true);
            		btnEdit.setEnabled(true);

	            	if (! e.getValueIsAdjusting())
	            	{
	                	System.out.println(table.getSelectedRow());
	                	
	                	tfGivenName.setText(userContacts.getUserContacts().get(table.getSelectedRow()).getGivenName());
	                	tfFamilyName.setText(userContacts.getUserContacts().get(table.getSelectedRow()).getFamilyName());
	                	tfEmail.setText(userContacts.getUserContacts().get(table.getSelectedRow()).getEmail());
	                	tfPhone.setText(userContacts.getUserContacts().get(table.getSelectedRow()).getPhone());
	
	            	}
            	}else
            	{
            		btnDelete.setEnabled(false);
            		btnEdit.setEnabled(false);
            	}
            }
        });
		scrollPane_1 = new JScrollPane( table );
		scrollPane_1.setBounds(0, 0, 450, 214);
		add(scrollPane_1);




	}
	
	public void refreshTable(String username, String pass, Client client, URI uri)
	{
	
		DefaultTableModel model = new DefaultTableModel(); 
		

		// Create a couple of columns 
		model.addColumn("Given Name"); 
		model.addColumn("Family Name"); 
		model.addColumn("E-Mail");
		model.addColumn("Phone");
		
		WebResource res=client.resource(uri).path("Contact").path("GetContactJSON");
		
		
	    JSONArray nameAndPass = new JSONArray();
	    nameAndPass.put(username);
	    nameAndPass.put(pass);
	    
	    ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,nameAndPass);
		JSONObject userContactsInJSON = resp.getEntity(JSONObject.class);
		try {
			userContactsInJSON.toString(4);
	//		System.out.println(userContactsInJSON.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userContacts = new UserContacts(userContactsInJSON);
		
		for(int i=0;i<userContacts.getUserContacts().size();i++)
		{
			System.out.println("vorname: " + userContacts.getUserContacts().get(i).getGivenName() + " nachname: "+ userContacts.getUserContacts().get(i).getFamilyName() + " email: "+userContacts.getUserContacts().get(i).getEmail() + " phone: " + userContacts.getUserContacts().get(i).getPhone());
			model.addRow(new Object[]{userContacts.getUserContacts().get(i).getGivenName(), userContacts.getUserContacts().get(i).getFamilyName(), userContacts.getUserContacts().get(i).getEmail(), userContacts.getUserContacts().get(i).getPhone()});
		}
	
		table.setModel(model);

	}

	public void clearAll()
	{
		tfGivenName.setText("");
		tfFamilyName.setText("");
		tfEmail.setText("");
		tfPhone.setText("");
	}
}

