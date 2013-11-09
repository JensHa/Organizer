package de.gui;

import java.awt.Desktop;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.Desktop.Action;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.Font;
import java.net.URI;
import javax.swing.SwingConstants;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;

import com.google.api.client.auth.oauth2.Credential;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import de.oauth.AuthHelper;

public class GetCredential extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnNewButton;
	private JTextField textField;
	private JLabel lblCode;
	private JButton btnNewButton_1;
	private JLabel lblNewLabel;
	
	private Client client;
	private String serverURI="http://localhost/";
	private int port=8080;
	private UriBuilder builder;
	private URI uri;
	private WebResource res;
	

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					GetCredential frame = new GetCredential();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public GetCredential(final String username, final Login login) {
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 479, 235);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnNewButton = new JButton("1) Request Code");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				textField.setEditable(true);

				
				
				res=client.resource(uri).path("Security").path("CreateAuthURL");
				ClientResponse resp = res.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class);	
				
				if(resp.getStatus()!=200)
				{
					System.out.println("Fehler!");
				}else
				{
					try{
						
						
					    if (Desktop.isDesktopSupported()&&Desktop.getDesktop().isSupported(Action.BROWSE)) 
					    {
					     Desktop.getDesktop().browse(URI.create(resp.getEntity(String.class)));
					    }
					    else 
					    {
					      System.out.println("Open the following address in your favorite browser:");
					      System.out.println("  " + resp.getEntity(String.class));
					    }
					}catch(Exception e1){e1.printStackTrace();}
				}
				res=null;
			}
		});
		btnNewButton.setBounds(10, 50, 453, 51);
		contentPane.add(btnNewButton);
		
		textField = new JTextField();
		textField.setBounds(97, 109, 366, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		lblCode = new JLabel("2) Enter Code:");
		lblCode.setBounds(10, 112, 114, 14);
		contentPane.add(lblCode);
		
		btnNewButton_1 = new JButton("3) Get Access Token");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				

			    JSONArray usernameAndPassword = new JSONArray();
			    usernameAndPassword.put(username);
			    usernameAndPassword.put(textField.getText());
				res=client.resource(uri).path("Security").path("CreateCredentials");
				ClientResponse resp=res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,usernameAndPassword);
				String validCred=resp.getEntity(String.class);
				if(validCred.equals("true"))
				{
				setVisible(false);
				login.toFront();
				login.setLabel("You can now login");
				}
				res=null;
				resp=null;
				System.out.println("test");
				
				
			}
		});
		btnNewButton_1.setBounds(10, 140, 453, 51);
		contentPane.add(btnNewButton_1);
		
		lblNewLabel = new JLabel("Authorization");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(10, 11, 414, 28);
		contentPane.add(lblNewLabel);
	}
	


}
