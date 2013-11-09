package de.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;

import de.gui.conntacts.Contact;
import de.oauth.AuthHelper;

public class Overview extends JFrame {

	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	private AuthHelper authHelper;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					Overview frame = new Overview();
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
	public Overview(AuthHelper authHelper) {
		this.authHelper=authHelper;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 59, 434, 202);
		tabbedPane.addTab("Contacts", new Contact(authHelper));
		tabbedPane.addTab("test1", new JPanel());

		contentPane.add(tabbedPane);
		
	}
}
