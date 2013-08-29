/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.chai;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.Color;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JList;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JButton;



public class ControlPanelWindow {

	private JFrame frmVelisphereChaiControl;
	private JTextField txtLdapUrl;
	private JTextField txtLdapPrincipal;
	private JTextField txtLdapPassword;
	private JTextField txtBroadcastText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		/**
		 * Load Config File and set config variables
		 */


		ConfigHandler cfh = new ConfigHandler();
		cfh.loadParamChangesAsXML();



		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ControlPanelWindow window = new ControlPanelWindow();
					window.frmVelisphereChaiControl.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}





	/**
	 * Create the application.
	 */
	public ControlPanelWindow() {

		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmVelisphereChaiControl = new JFrame();
		frmVelisphereChaiControl.setTitle("VeliSphere Chai Control Panel - Containing DNA of the Internet of Things");
		frmVelisphereChaiControl.setBounds(100, 100, 781, 562);
		frmVelisphereChaiControl.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmVelisphereChaiControl.getContentPane().setLayout(null);

		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBackground(Color.LIGHT_GRAY);
		lblNewLabel.setIcon(new ImageIcon(ControlPanelWindow.class.getResource("/chai/Resources/chai.jpg")));
		lblNewLabel.setBounds(676, 35, 79, 69);
		frmVelisphereChaiControl.getContentPane().add(lblNewLabel);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 765, 21);
		frmVelisphereChaiControl.getContentPane().add(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JLabel lblVelisphereChaiControl = new JLabel("VeliSphere Chai Control Panel");
		lblVelisphereChaiControl.setFont(lblVelisphereChaiControl.getFont().deriveFont(lblVelisphereChaiControl.getFont().getStyle() | Font.BOLD));
		lblVelisphereChaiControl.setBounds(10, 34, 218, 14);
		frmVelisphereChaiControl.getContentPane().add(lblVelisphereChaiControl);

		JLabel lblThisApplicationIs = new JLabel("This application is used to control and manage the VeliSphere Controller Engine (Chai)");
		lblThisApplicationIs.setFont(UIManager.getFont("Label.font"));
		lblThisApplicationIs.setBounds(10, 51, 656, 14);
		frmVelisphereChaiControl.getContentPane().add(lblThisApplicationIs);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 108, 303, 130);
		frmVelisphereChaiControl.getContentPane().add(scrollPane);

		String[] ldapMobiles;



		LdapGet ldapPullMobiles = new LdapGet(); 

		ldapMobiles = ldapPullMobiles.getLdapMobiles();



		JList lstDirectory = new JList(ldapMobiles);
		scrollPane.setViewportView(lstDirectory);

		JLabel lblLdapDirectoryContent = new JLabel("LDAP Directory Content:");
		lblLdapDirectoryContent.setBounds(10, 90, 184, 14);
		frmVelisphereChaiControl.getContentPane().add(lblLdapDirectoryContent);

		txtLdapUrl = new JTextField();
		txtLdapUrl.setBounds(129, 249, 184, 20);
		frmVelisphereChaiControl.getContentPane().add(txtLdapUrl);
		txtLdapUrl.setColumns(10);

		JLabel lblLdapServerUrl = new JLabel("LDAP Server URL");
		lblLdapServerUrl.setBounds(10, 249, 109, 14);
		frmVelisphereChaiControl.getContentPane().add(lblLdapServerUrl);

		txtLdapPrincipal = new JTextField();
		txtLdapPrincipal.setColumns(10);
		txtLdapPrincipal.setBounds(129, 278, 184, 20);
		frmVelisphereChaiControl.getContentPane().add(txtLdapPrincipal);

		JLabel lblLdapLogin = new JLabel("LDAP Login");
		lblLdapLogin.setBounds(10, 281, 109, 14);
		frmVelisphereChaiControl.getContentPane().add(lblLdapLogin);

		txtLdapPassword = new JTextField();
		txtLdapPassword.setColumns(10);
		txtLdapPassword.setBounds(129, 309, 184, 20);
		frmVelisphereChaiControl.getContentPane().add(txtLdapPassword);

		JLabel lblLdapPassword = new JLabel("LDAP Password");
		lblLdapPassword.setBounds(10, 312, 109, 14);
		frmVelisphereChaiControl.getContentPane().add(lblLdapPassword);

		/**
		 * Fill LDAP fields
		 */

		txtLdapUrl.setText(ServerParameters.ldapUrl);
		txtLdapPrincipal.setText(ServerParameters.ldapPrincipal);
		txtLdapPassword.setText(ServerParameters.ldapPassword);

		JButton btnBroadcastShutdownWarning = new JButton("Broadcast to Blubber Clients");
		btnBroadcastShutdownWarning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					// Here we are sending a broadcast message to all blubber chat clients using the "blubber.all" exchange
					// Every blubber client should bind to the blubber.all exchange.							

					Broadcast.broadcastToExchange(txtBroadcastText.getText(), "blubber.all");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnBroadcastShutdownWarning.setIcon(new ImageIcon(ControlPanelWindow.class.getResource("/com/sun/java/swing/plaf/windows/icons/Warn.gif")));
		btnBroadcastShutdownWarning.setBounds(344, 138, 218, 41);
		frmVelisphereChaiControl.getContentPane().add(btnBroadcastShutdownWarning);

		txtBroadcastText = new JTextField();
		txtBroadcastText.setText("Enter broadcast text here");
		txtBroadcastText.setBounds(344, 107, 218, 20);
		frmVelisphereChaiControl.getContentPane().add(txtBroadcastText);
		txtBroadcastText.setColumns(10);



	}
}
