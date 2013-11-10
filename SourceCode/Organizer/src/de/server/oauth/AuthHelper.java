package de.server.oauth;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;


public class AuthHelper {
	
	static GoogleAuthorizationCodeFlow flow;
    private JsonFactory JSON_FACTORY = new JacksonFactory();
    private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private Credential credential;
    
    public AuthHelper()
    {
    	flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                JSON_FACTORY, ServerAuthProperties.CLIENT_ID, ServerAuthProperties.CLIENT_SECRET, ServerAuthProperties.SCOPE).build();
    }
	
	public GoogleAuthorizationCodeFlow getFlow()
	{
		
		return flow;
	}
	
	public Credential initCredential(String code)
	{
		

		try
		{
		  final GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(ServerAuthProperties.CALLBACK_URI).execute();
           credential = flow.createAndStoreCredential(response, null);
          return credential;
		}catch(Exception e)
		{e.printStackTrace();return null;}
		
	}
	
	public Credential getCredential()
	{
		return this.credential;
	}

	public GoogleAuthorizationCodeRequestUrl getAuthURL()
	{
		GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(ServerAuthProperties.CALLBACK_URI);
		
		return authorizationUrl;
	}
}
