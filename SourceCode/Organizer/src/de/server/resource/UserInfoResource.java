package de.server.resource;

import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
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
	public Response getUserMail(JSONArray nameAndPass)
	{
		try
		{
		System.out.println("##Server##: A user requests his/her mail");
		
		for(int i=0;i<userCredentials.size();i++)
		{
			
			if(nameAndPass.getString(0).equals(userCredentials.get(i)[0])&&nameAndPass.getString(1).equals(userCredentials.get(i)[1]))
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
	public Response getUserGender(JSONArray nameAndPass)
	{
		try
		{
		System.out.println("##Server##: A user's gender was requested");
		
		for(int i=0;i<userCredentials.size();i++)
		{	
			Object[] userArray = userCredentials.get(i);
			if(nameAndPass.getString(0).equals(userArray[0])&&nameAndPass.getString(1).equals(userArray[3]))
			{	
				//We found the user
				
				//Send the request with the users credential
			    final HttpRequestFactory requestFactory =  new NetHttpTransport().createRequestFactory((Credential) userArray[3]);
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
