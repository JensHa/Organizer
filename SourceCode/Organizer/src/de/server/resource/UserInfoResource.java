package de.server.resource;

import java.net.URI;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gson.JsonArray;
import com.sun.xml.internal.bind.v2.TODO;

import de.server.oauth.AuthHelper;
import de.server.oauth.ServerAuthProperties;


//TODO: add the other get methods similar to getUserGender if needed
/**
 * This resource provides the basic google user informations
 * Gender, Email, intrested in, name, full name etc
 *
 */
@Path("/Userinfo")
public class UserInfoResource {
	
	/**
	 * An arraylist that contains one array for every user.
	 * The array for every user contains name, password, email, SessionID and credential in that order.
	 */
	ArrayList<Object[]> userCredentials=SecurityResource.userCredentials;
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/GetUserMail")
	public Response getUserMail(JSONArray nameAndID)
	{
		try
		{
		System.out.println("##Server##: A user requests his/her mail");
		
		for(int i=0;i<userCredentials.size();i++)
		{
			
			if(nameAndID.getString(0).equals(userCredentials.get(i)[0])&&nameAndID.getString(1).equals(userCredentials.get(i)[3]))
			{
				Response response = Response.ok(userCredentials.get(i)[2]).build();
				return response;
			}
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Gets the users sex from google and returns it.
	 * @param nameAndID must provide a valid session id
	 * @return the users gender as plain text
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/GetUsersGender")
	public Response getUserGender(JSONArray nameAndID)
	{
		try
		{
		System.out.println("##Server##: A user's gender was requested");
		
		for(int i=0;i<userCredentials.size();i++)
		{	
			Object[] userArray = userCredentials.get(i);
			if(nameAndID.getString(0).equals(userArray[0])&&nameAndID.getString(1).equals(userArray[3]))
			{	
				//We found the user
				
				//Send the request with the users credential
			    final HttpRequestFactory requestFactory =  new NetHttpTransport().createRequestFactory((Credential) userArray[4]);
			    final GenericUrl url = new GenericUrl(ServerAuthProperties.USER_INFO_URL);
			    final HttpRequest userinfoRequest = requestFactory.buildGetRequest(url);
			    userinfoRequest.getHeaders().setContentType("application/json");
			    /*
			     one example of an output
			     {
				 "id": "101159048390788323689",
				 "email": "nathrak314@gmail.com",
				 "verified_email": true,
				 "name": "Anakin Skywalker",
				 "given_name": "Anakin",
				 "family_name": "Skywalker",
				 "link": "https://plus.google.com/101159048390788323689",
				 "gender": "other",
				 "locale": "de"
				}
			     */
			    
			    //Parse the JSON answer
			    String reply = userinfoRequest.execute().parseAsString();
				JSONObject todoJson = new JSONObject(reply);  
				
			//	System.out.println(reply);
				
				String gender = (String) todoJson.get("gender");
				
				Response response = Response.ok(gender).build();
				return response;
			}
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
}
