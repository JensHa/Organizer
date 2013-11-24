package de.server.resource;

import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.json.JSONException;
import org.json.XML;

import com.google.api.client.auth.oauth2.Credential;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.contacts.GroupMembershipInfo;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.data.extensions.FamilyName;
import com.google.gdata.data.extensions.GivenName;
import com.google.gdata.data.extensions.Name;
import com.google.gdata.data.extensions.PhoneNumber;
import com.google.gdata.util.common.xml.XmlWriter;


//TODO: add the other get methods similar to getUserGender if needed
/**
 * This resource provides the basic google user informations
 * Gender, Email, intrested in, name, full name etc
 *
 */
@Path("/Contact")
public class ContactResource {
	
	/**
	 * An arraylist that contains one array for every user.
	 * The array for every user contains name, password, email, SessionID and credential in that order.
	 */
	ArrayList<Object[]> userCredentials=SecurityResource.userCredentials;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/GetContactXML")
	public Response getContactsXML(JSONArray nameAndPass)
	{
		System.out.println("##Server##: User tries to get contacts in XML");
		ContactFeed resultFeed;
		Response response=null;
		try
		{
		for(int i=0;i<userCredentials.size();i++)
		{
			
			if(nameAndPass.getString(0).equals(userCredentials.get(i)[0])&&nameAndPass.getString(1).equals(userCredentials.get(i)[1]))
			{

				ContactsService conserv= new ContactsService("Project Default Service Account");
	            conserv.setOAuth2Credentials((Credential) userCredentials.get(i)[3]);
	            URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/"+userCredentials.get(i)[2]+"/full");
	            resultFeed = conserv.getFeed(feedUrl, ContactFeed.class);
	            
	            
	            //make a XML file out of the ContactFeed (provided by google)
	            StringWriter stringWriter = new StringWriter();
	            String contactsInXML = "";
	            XmlWriter xmlWriter = new XmlWriter(stringWriter, "UTF-8");
	            resultFeed.generate(xmlWriter, conserv.getExtensionProfile());
	            contactsInXML = stringWriter.toString();
	            
	            
	            response = Response.ok(contactsInXML).build();
	 
			}
		}
		}catch(Exception e){e.printStackTrace();}
		return response;
		

	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/GetContactJSON")
	public Response getContactsJSON(JSONArray nameAndPass)
	{
		System.out.println("##Server##: User tries to get contacts in JSON");
		ContactFeed resultFeed;
		Response response=null;
		try
		{
		for(int i=0;i<userCredentials.size();i++)
		{
			
			if(nameAndPass.getString(0).equals(userCredentials.get(i)[0])&&nameAndPass.getString(1).equals(userCredentials.get(i)[1]))
			{

				ContactsService conserv= new ContactsService("Project Default Service Account");
	            conserv.setOAuth2Credentials((Credential) userCredentials.get(i)[3]);
	            URL feedUrl = new URL("https://www.google.com/m8/feeds/contacts/"+userCredentials.get(i)[2]+"/full");
	            resultFeed = conserv.getFeed(feedUrl, ContactFeed.class);
	            
	          
	            
	            //make a XML file out of the ContactFeed (provided by google)
	            StringWriter stringWriter = new StringWriter();
	            String contactsInXML = "";
	            XmlWriter xmlWriter = new XmlWriter(stringWriter, "UTF-8");
	            resultFeed.generate(xmlWriter, conserv.getExtensionProfile());
	            contactsInXML = stringWriter.toString();
	            
	            

	            org.json.JSONObject xmlJSONObj = null;
	            JSONObject jsonObjectForJetty = null;
	            try {
	            	//xmlJSONObj cant be send through jetty... 
	            	//...so we need a JSONObject which is supported by jetty(from codehaus.jettison)
	                xmlJSONObj = XML.toJSONObject(contactsInXML);
	                String jsonPrettyPrintString = xmlJSONObj.toString();
	                System.out.println("****"+ jsonPrettyPrintString);
		            jsonObjectForJetty = new JSONObject(jsonPrettyPrintString); 

	            } catch (JSONException je) {
	                System.out.println(je.toString());
	            }
	            
	            response = Response.ok(jsonObjectForJetty).build();
	 
			}
		}
		}catch(Exception e){e.printStackTrace();}
		return response;
		

	}
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/CreateContactJSON")
	public Response CreateContactsJSON(JSONObject usernamePassAndContact)
	{
		System.out.println("##Server##: User tries to create a contact in JSON");

		Response response=null;

		try{
			for(int i=0; i<userCredentials.size();i++)
			{
				if(usernamePassAndContact.get("username").equals(userCredentials.get(i)[0])&&usernamePassAndContact.get("password").equals(userCredentials.get(i)[1]))
				{
					
						  ContactEntry contact = new ContactEntry();
						  Name name = new Name();
						  if(!usernamePassAndContact.getString("givenName").equals(""))
						  {
						  name.setGivenName(new GivenName(usernamePassAndContact.getString("givenName"), null));
						  contact.setName(name);
						  }
						  if(!usernamePassAndContact.getString("familyName").equals(""))
						  {
						  name.setFamilyName(new FamilyName(usernamePassAndContact.getString("familyName"), null));
						  contact.setName(name);
						  }
						  
						  
						  Email primaryMail = new Email();
						  if(!usernamePassAndContact.getString("email").equals(""))
						  {
						  primaryMail.setAddress(usernamePassAndContact.getString("email"));
						  primaryMail.setRel("http://schemas.google.com/g/2005#home");
						  contact.addEmailAddress(primaryMail);

						  }
			
						  GroupMembershipInfo groupMembershipInfo = new GroupMembershipInfo();
						  groupMembershipInfo.setHref("http://www.google.com/m8/feeds/groups/jenshaussmanndeveloper@gmail.com/base/6");
						  contact.addGroupMembershipInfo(groupMembershipInfo);
						  
						  PhoneNumber primaryPhoneNumber = new PhoneNumber();
						  if(!usernamePassAndContact.getString("phone").equals(""))
						  {
						  primaryPhoneNumber.setPhoneNumber(usernamePassAndContact.getString("phone"));
						  primaryPhoneNumber.setRel("http://schemas.google.com/g/2005#home");
						  contact.addPhoneNumber(primaryPhoneNumber);

						  }
						  
						  URL postUrl = new URL("https://www.google.com/m8/feeds/contacts/jenshaussmanndeveloper@gmail.com/full");
						  ContactsService conserv= new ContactsService("Project Default Service Account");
				            conserv.setOAuth2Credentials((Credential) userCredentials.get(0)[3]);
			
						  ContactEntry createdContact = conserv.insert(postUrl, contact);
			
			
			
				
				}
			}
		}catch(Exception e){e.printStackTrace();}
		return response;
	}
}
