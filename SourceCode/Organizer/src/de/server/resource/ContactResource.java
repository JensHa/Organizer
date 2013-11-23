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
import com.google.gdata.data.contacts.ContactFeed;
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
	
	
    
//    de.server.entities.contacts.ContactFeed resultFeedOwn= new de.server.entities.contacts.ContactFeed();
//    
//    for(int i1 =0; i1<resultFeed.getEntries().size();i1++)
//    {
//    	Contact con=new Contact();
//    	con.setTitle(resultFeed.getEntries().get(i1).getTitle().getPlainText());
//    	con.setFirstName(resultFeed.getEntries().get(i1).getName().getGivenName().getValue());
//    	con.setLastName(resultFeed.getEntries().get(i1).getName().getFamilyName().getValue());
////    	con.setPhoneNumber(resultFeed.getEntries().get(i1).getPhoneNumbers().get(0).getPhoneNumber());
////    	con.seteMail(resultFeed.getEntries().get(i1).getEmailAddresses().get(0).getAddress());
//    	resultFeedOwn.addContact(con);
//    }
//	JAXBContext jaxbContext = JAXBContext.newInstance(de.server.entities.contacts.ContactFeed.class);
//	Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//	// output pretty printed
//	jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//
//	jaxbMarshaller.marshal(resultFeedOwn, System.out);
	
	
//    
//    for (ContactEntry entry : resultFeed.getEntries()) 
//    {
//   	 System.out.println(entry.getName().getFullName().getValue());
//    }
//    
//    System.out.println(resultFeed.getTitle().getPlainText());
//    System.out.println(resultFeed.getEntries().size());
}
