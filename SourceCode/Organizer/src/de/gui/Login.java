package de.gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.codehaus.jettison.json.JSONArray;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.awt.Color;
import javax.swing.JPasswordField;

public class Login extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnLogin;
	private JButton btnCreateAccount;
	private JLabel lblNewLabel;
	private JLabel lblLogin;
	private static Login frame;
	
	private Client client;
	private String serverURI="http://localhost/";
	private int port=8080;
	private UriBuilder builder;
	private URI uri;
	private WebResource res;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					 frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null,
						    "Error",
						    "Cant connect to servr",
						    JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
	
		client = Client.create();
		client.getProperties().put(ClientConfig.PROPERTY_FOLLOW_REDIRECTS, true);
		builder = UriBuilder.fromUri(serverURI).port(port);
		uri = builder.build();
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 300, 194);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(10, 51, 77, 14);
		contentPane.add(lblUsername);
		
		lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(10, 76, 77, 14);
		contentPane.add(lblPassword);
		
		textField = new JTextField();
		textField.setBounds(79, 48, 195, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(79, 73, 195, 20);
		contentPane.add(passwordField);
		passwordField.setColumns(10);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
				if(textField.getText().equals("")||new String(passwordField.getPassword()).equals(""))
				{
					JOptionPane.showMessageDialog(null,
						    "Please enter a username and password to login",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}else
				{
				res=client.resource(uri).path("Security").path("CheckUser");

				//TODO: Maybe a better solution? A fixed array of size 2 would be better... But Jersey dosent support arrays of primitive data types => messagebody writer/reader required
			    JSONArray userData = new JSONArray();
			    userData.put(textField.getText());
			    userData.put(new String(passwordField.getPassword()));


				
				ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,userData);
				//Status: Password/Username incorrect - No valid credentials
				String status=resp.getEntity(String.class);
				switch (status) {
				case "false;false":
					lblNewLabel.setText("Wrong Username or Password");
					break;
					
				case "true;false":
					lblNewLabel.setText("You have no valid credentials. Please verify yourself!");
					GetCredential getCredential=new GetCredential(textField.getText(),frame);
					getCredential.setVisible(true);
					break;
				
				case "true;true":
					lblNewLabel.setText("Everything fine!");
					break;

				default:
					break;
				}
			

				
				res=null;
				resp=null;
				

				
				}
				}catch(Exception e1){e1.printStackTrace(); JOptionPane.showMessageDialog(null,
						"Cant connect to server",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);}}
		});
		btnLogin.setBounds(10, 104, 127, 23);
		contentPane.add(btnLogin);
		
		btnCreateAccount = new JButton("Create Account");
		btnCreateAccount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				try{
				if(textField.getText().equals("")||new String(passwordField.getPassword()).equals(""))
				{
					JOptionPane.showMessageDialog(null,
						    "Please enter a username and password to create a account",
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
				}else
				{
				
				res=client.resource(uri).path("Security").path("CreateUser");
				System.out.println(res.getURI());
			    JSONArray userData = new JSONArray();
			    userData.put(textField.getText());
			    userData.put(new String(passwordField.getPassword()));
				ClientResponse resp = res.type(MediaType.APPLICATION_JSON).post(ClientResponse.class,userData);

				if(resp.getEntity(String.class).equals("true"))
				{
					lblNewLabel.setText("User Created. You can now login");
				}else
				{
					lblNewLabel.setText("User already exits! Please choose another username");
				}
			}
				}catch(Exception e1){e1.printStackTrace(); JOptionPane.showMessageDialog(null,
						"Cant connect to server",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);}
			}
		});
		btnCreateAccount.setBounds(147, 104, 127, 23);
		contentPane.add(btnCreateAccount);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 9));
		lblNewLabel.setForeground(Color.RED);
		lblNewLabel.setBounds(10, 135, 264, 14);
		contentPane.add(lblNewLabel);
		
		lblLogin = new JLabel("Login");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblLogin.setBounds(10, 11, 264, 29);
		contentPane.add(lblLogin);
		

	}
	
	public  void setLabel(String text)
	{
		lblNewLabel.setText(text);
	}
}
