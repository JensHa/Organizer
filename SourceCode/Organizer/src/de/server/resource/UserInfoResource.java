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




@Path("/Userinfo")
public class UserInfoResource {

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
	
}
