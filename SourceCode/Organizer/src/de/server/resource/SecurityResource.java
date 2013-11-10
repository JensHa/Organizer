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




@Path("/Security")
public class SecurityResource {
	
	//private static Inventorylist inventorylist=new Inventorylist();
	
	@Context
	UriInfo uriInfo;
	AuthHelper authHelper=new AuthHelper();
	Object[] credentialsRow=new Object[5];
	static ArrayList<Object[]> userCredentials= new ArrayList<>();
	

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/CreateAuthURL")
	public String getSome()
	{
		System.out.println("##Server##: A AuthUrl will be generated");
		String result="";
		authHelper = new AuthHelper();
		result = authHelper.getAuthURL().toString();


		return result;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/CreateCredentials")
	public Response postItemXML(@Context HttpServletRequest request, JSONArray usernameAndPass)
	{
		int pos=0;
		String validCred="false";
		//authHelper.initCredential(code);
		try{
			System.out.println("Received Accesscode: " + usernameAndPass.getString(1));
			
			for(int i=0;i<userCredentials.size();i++)
			{
				if(userCredentials.get(i)[0].equals(usernameAndPass.getString(0)))
				{
					userCredentials.get(i)[4]=authHelper.initCredential(usernameAndPass.getString(1));
					pos=i;
				}
			}
			
			  //TODO: Bad solution... Better use JSON
		      final HttpRequestFactory requestFactory =  new NetHttpTransport().createRequestFactory((Credential) userCredentials.get(pos)[4]);
		      final GenericUrl url = new GenericUrl(ServerAuthProperties.USER_INFO_URL);
		      final HttpRequest userinfoRequest = requestFactory.buildGetRequest(url);
		      userinfoRequest.getHeaders().setContentType("application/json");
		      final String jsonIdentity = userinfoRequest.execute().parseAsString();
		      userCredentials.get(pos)[2]=jsonIdentity.substring(jsonIdentity.indexOf("email")+9, jsonIdentity.indexOf("\"", jsonIdentity.indexOf("email")+10));
		        
		 
			
			if(((String)userCredentials.get(pos)[2]).contains("@")&&((Credential)userCredentials.get(pos)[4]).getExpirationTimeMilliseconds()>0)
			{
				validCred="true";
			}
			

            
            }catch(Exception e){e.printStackTrace();}
		Response response = Response.ok(validCred).build();
		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/CheckUser")
	public Response checkIfUserExits(JSONArray userData)
	{
		
		//TODO: Handle primative datatypes?
		String userExits="false";
		String hasValidCredentials="false";
		try
		{
		System.out.println("##Server##: User tries to login: " + userData.getString(0) + " - " + userData.getString(1));
		
		for(int i=0; i<userCredentials.size();i++)
		{
			
			//Username in Server == Username send from user? 
			if(userCredentials.get(i)[0].equals(userData.getString(0)))
			{
				//Passowrd of user in server == Password send from user?
				if(userCredentials.get(i)[1].equals(userData.getString(1)))
				{
					//User with this login data exists
					userExits="true";

					//has he or she also a (valid) credential?
					if(((Credential)userCredentials.get(i)[4])!=null)
					{
						hasValidCredentials="true";
					}
				}
			}
		}
		
		}catch(Exception e){e.printStackTrace();}
		
		Response response = Response.ok(userExits + ";" + hasValidCredentials).build();
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/CreateUser")
	public Response createANewUser(JSONArray userData)
	{
		
		//TODO: Handle primative datatypes?
		String userCreated="false";
		String userExits="false";
		try
		{
		System.out.println("##Server##: User will be created: " + userData.getString(0) + " - " + userData.getString(1));
		
		for(int i=0; i<userCredentials.size();i++)
		{
			
			//Has anybody else this username?
			if(userCredentials.get(i)[0].equals(userData.getString(0)))
			{
				
					userExits="true";
				
			}
		}
		
		//User doesnt exits and will be added to the userlist => credentials and email are null
		if(userExits.equals("false"))
		{
			//Username
			credentialsRow[0]=userData.getString(0);
			//Password
			credentialsRow[1]=userData.getString(1);
			//Email
			credentialsRow[2]=null;
			//SessionID
			credentialsRow[3]=null;
			//Credentials
			credentialsRow[4]=null;
			
			userCredentials.add(credentialsRow);
			userCreated="true";
		}
		
		}catch(Exception e){e.printStackTrace();}
		
		Response response = Response.ok(userCreated).build();
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/SetSessionID")
	public Response setASessionID(JSONArray nameAndID)
	{
		try
		{
			for(int i=0;i<userCredentials.size();i++)
			{
				if(nameAndID.getString(0).equals(userCredentials.get(i)[0]))
				{
				    SecureRandom sr1 = new SecureRandom();
				    userCredentials.get(i)[3]=nameAndID.getString(1)+"/"+sr1.nextInt();
				    
					Response response = Response.ok(userCredentials.get(i)[3]).build();
					return response;
				}
			}
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
//	@POST
//	@Consumes(MediaType.APPLICATION_XML)
//	public Response postItemXML(JAXBElement<Item> jax,@Context HttpServletRequest request)
//	{
//		
//		Item item = (Item)jax.getValue();
//		
//		inventorylist.getItemlist().add(item);
//		
//		UriBuilder builder = uriInfo.getAbsolutePathBuilder();
//		URI uri = builder.path( ""+item.getId() ).build();
//		Response response = Response.created(uri).build();
//		System.out.println("in PostXML");
//		System.out.println(inventorylist.getItemlist().size());
//		for(int i=0;i<inventorylist.getItemlist().size();i++)
//		{
//			System.out.println(inventorylist.getItemlist().get(i).getId() + "; " + inventorylist.getItemlist().get(i).getDescription());
//		}
//		return response; 
//	}
}
