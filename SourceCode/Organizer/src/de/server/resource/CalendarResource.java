package de.server.resource;

import java.util.ArrayList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;

import com.google.gdata.client.calendar.CalendarService;

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
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gson.JsonArray;
import com.sun.xml.internal.bind.v2.TODO;

import de.server.oauth.AuthHelper;
import de.server.oauth.ServerAuthProperties;


@Path("/Calendar")
public class CalendarResource {
	
	/**
	 * An arraylist that contains one array for every user.
	 * the array for every user contains name, password, ID and credential in that order.
	 */
	ArrayList<Object[]> userCredentials=SecurityResource.userCredentials;
	
	/**
	 * Gets the list of all Calendars of the user from google and returns it.
	 * @param nameAndID must provide a valid session id
	 * @return xml representation of the users calendars (CalendarFeed object) 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	@Path("/GetUsersCalendarList")
	public Response getUserCalendarList(JSONArray nameAndID)
	{
		try
		{
		System.out.println("##Server##: A user's calendar list was requested");
		
		for(int i=0;i<userCredentials.size();i++)
		{	
			Object[] userArray = userCredentials.get(i);
			if(nameAndID.getString(0).equals(userArray[0])&&nameAndID.getString(1).equals(userArray[3]))
			{	
				
				//using feed
				
				//build the request with the users credentials
			    final HttpRequestFactory requestFactory =  new NetHttpTransport().createRequestFactory((Credential) userArray[4]);
			    final GenericUrl url = new GenericUrl(ServerAuthProperties.USER_CALENDAR_URL);
			    final HttpRequest userinfoRequest = requestFactory.buildGetRequest(url);
			    userinfoRequest.getHeaders().setContentType("application/json");
			    

			    //Parse the  answer
			    String reply = "r";
			    reply = userinfoRequest.execute().parseAsString();
			    /*
			    CalendarFeed resultFeed = userinfoRequest.execute().parseAs(CalendarFeed.class);

			    System.out.println("Your calendars:");
		        System.out.println();

		        for (int i1 = 0; i1 < resultFeed.getEntries().size(); i1++) {
		          CalendarEntry entry = resultFeed.getEntries().get(i1);
		          System.out.println("\t" + entry.getTitle().getPlainText());
		        }
//	*/			
			    //Return an XML string representing an Google Calendar Feed object
				Response response = Response.ok(reply).build();
				return response;
				
				/*			api 3.0 versuch
			//    Calendar service = new Calendar(httpTransport, accessProtectedResource, jsonFactory);
			//    service.setApplicationName("YOUR_APPLICATION_NAME");
				

		        CalendarService myService = new CalendarService("exampleCo-exampleApp-1.0");
		       // myService.setUserCredentials("root@gmail.com", "pa$$word");
		        myService.setOAuth2Credentials((Credential) userArray[3]);

		        URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
		        CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);

		        System.out.println("Your calendars:");
		        System.out.println();

		        for (int i1 = 0; i1 < resultFeed.getEntries().size(); i1++) {
		          CalendarEntry entry = resultFeed.getEntries().get(i1);
		          System.out.println("\t" + entry.getTitle().getPlainText());
		        }
		        */
			}
			
		}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
		

	/**
	 * Gets the users calendar from google and returns it.
	 * @param nameAndID must provide a valid session id
	 * @return xml representation of the users calendars (CalendarFeed object) 
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_XML)
	@Path("/GetUsersCalendar")
	public Response getUserPrivateCalendar(JSONArray nameAndID)
	{
		try
		{
		System.out.println("##Server##: A user's private calendar was requested");
		
		for(int i=0;i<userCredentials.size();i++)
		{	
			Object[] userArray = userCredentials.get(i);
			if(nameAndID.getString(0).equals(userArray[0])&&nameAndID.getString(1).equals(userArray[3]))
			{	
				
				//using feed
				
				//build the request with the users credentials
			    final HttpRequestFactory requestFactory =  new NetHttpTransport().createRequestFactory((Credential) userArray[4]);
			    final GenericUrl url = new GenericUrl(ServerAuthProperties.USER_PRIVATE_CALENDAR_URL);
			    final HttpRequest userinfoRequest = requestFactory.buildGetRequest(url);
			    userinfoRequest.getHeaders().setContentType("application/json");
			    

			    //Parse the  answer
			    String reply = "r";
			    reply = userinfoRequest.execute().parseAsString();
			    //Return an XML string representing an Google Calendar Feed object
				Response response = Response.ok(reply).build();
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
