package de.oauth;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;


public class AuthHelper {
	
	GoogleAuthorizationCodeFlow flow;
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    
    public AuthHelper()
    {
    	flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                JSON_FACTORY, Properties.CLIENT_ID, Properties.CLIENT_SECRET, Properties.SCOPE).build();
    }
	
	public GoogleAuthorizationCodeFlow getFlow()
	{
		
		return flow;
	}
	
	public Credential getCredential(String code)
	{
		try
		{
		  final GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(Properties.CALLBACK_URI).execute();
          final Credential credential = flow.createAndStoreCredential(response, null);
          return credential;
		}catch(Exception e)
		{e.printStackTrace();return null;}
		
	}

	public GoogleAuthorizationCodeRequestUrl getAuthURL()
	{
		GoogleAuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl();
		authorizationUrl.setRedirectUri(Properties.CALLBACK_URI);
		
		return authorizationUrl;
	}
}
