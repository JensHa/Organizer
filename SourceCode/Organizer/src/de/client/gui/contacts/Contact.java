package de.client.gui.contacts;

import java.net.URL;

import javax.swing.JPanel;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;

import de.server.oauth.AuthHelper;

public class Contact extends JPanel {
	private JEditorPane editorPane;
	private JScrollPane scrollPane;

	/**
	 * Create the panel.
	 */
	public Contact(String username, String sessionID) {
		setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 430, 278);
		add(scrollPane);
		
		editorPane = new JEditorPane();
		scrollPane.setViewportView(editorPane);
//		
//		 try{
//             ContactsService conserv= new ContactsService("Project Default Service Account");
//             conserv.setOAuth2Credentials(authHelper.getCredential());
//             URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/jenshaussmanndeveloper@gmail.com/full");
//             ContactFeed resultFeed = conserv.getFeed(feedUrl, ContactFeed.class);
//             
//             
//             for (ContactEntry entry : resultFeed.getEntries()) {
//            	 System.out.println(entry.getName().getFullName().getValue());
//             }
//             
//             System.out.println(resultFeed.getTitle().getPlainText());
//             System.out.println(resultFeed.getEntries().size());
//             
//             
//             }catch(Exception e){e.printStackTrace();}

	}
}
