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
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.io.IOException;

import javax.swing.ImageIcon;


public class MainScreen {

	public  JFrame frmBlubberMessenger;
	public JTextField txtSendToQueue;
	private static String messageHistory;
	public JTextField txtMyQueue;
	public static JTextArea txtHistory = new JTextArea();
	private static JScrollPane scpHistory = new JScrollPane();
	private Thread t;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainScreen window = new MainScreen();
					window.frmBlubberMessenger.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainScreen() {
		initialize();
		
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlubberMessenger = new JFrame();
		frmBlubberMessenger.getContentPane().setBackground(new Color(230, 230, 250));
		frmBlubberMessenger.setTitle("Blubber Messenger - Containing DNA of the Internet of Things");
		frmBlubberMessenger.setBounds(100, 100, 666, 447);
		frmBlubberMessenger.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		messageHistory = "";
		frmBlubberMessenger.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Blubber is an experimental messenger using VeliX");
		lblNewLabel.setBounds(10, 353, 414, 20);
		frmBlubberMessenger.getContentPane().add(lblNewLabel);
		
		txtSendToQueue = new JTextField();
		txtSendToQueue.setBounds(129, 49, 271, 20);
		frmBlubberMessenger.getContentPane().add(txtSendToQueue);
		txtSendToQueue.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Chat with Queue:");
		lblNewLabel_1.setBounds(10, 52, 109, 14);
		lblNewLabel_1.setLabelFor(txtSendToQueue);
		frmBlubberMessenger.getContentPane().add(lblNewLabel_1);
		
		final JTextArea txtMessageToSend = new JTextArea();
		txtMessageToSend.setBounds(129, 193, 271, 41);
		txtMessageToSend.setForeground(Color.BLUE);
		txtMessageToSend.setLineWrap(true);
		frmBlubberMessenger.getContentPane().add(txtMessageToSend);
		
		JLabel lblNewLabel_2 = new JLabel("Message:");
		lblNewLabel_2.setBounds(10, 198, 76, 14);
		frmBlubberMessenger.getContentPane().add(lblNewLabel_2);
	
		JScrollPane scpHistory = new JScrollPane();
		scpHistory.setBounds(129, 80, 271, 102);
		scpHistory.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		frmBlubberMessenger.getContentPane().add(scpHistory);
		
		// txtHistory is already declared in the header to make it available to other methods
		scpHistory.setViewportView(txtHistory);
		txtHistory.setWrapStyleWord(true);
		txtHistory.setLineWrap(true);
		txtHistory.setEditable(false);
		
		
		final JButton btnSendMessage = new JButton("Send");
		btnSendMessage.setEnabled(false);
		btnSendMessage.setBounds(126, 245, 89, 23);
		btnSendMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					
					updateHistory(txtMessageToSend.getText());
					Send.main(txtMessageToSend.getText(), txtSendToQueue.getText());
					
					txtMessageToSend.setText("");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		frmBlubberMessenger.getContentPane().add(btnSendMessage);
		
		
		JLabel lblHistory = new JLabel("History:");
		lblHistory.setBounds(10, 86, 46, 14);
		frmBlubberMessenger.getContentPane().add(lblHistory);
		
		JLabel lblcTm = new JLabel("Blubber v0.0.1 for Desktop Devices - (C) 2013 Thorsten Meudt");
		lblcTm.setBounds(10, 384, 387, 14);
		frmBlubberMessenger.getContentPane().add(lblcTm);
		
		JPanel panel = new JPanel();
		panel.setBounds(10, 279, 625, 56);
		frmBlubberMessenger.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblYourQueue = new JLabel("My Queue:");
		lblYourQueue.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblYourQueue.setBounds(10, 20, 69, 14);
		panel.add(lblYourQueue);
		
		txtMyQueue = new JTextField();
		txtMyQueue.setBounds(89, 17, 272, 20);
		panel.add(txtMyQueue);
		txtMyQueue.setColumns(10);
		
		JButton btnLogOn = new JButton("Log on");
		btnLogOn.setBounds(371, 16, 124, 23);
		panel.add(btnLogOn);
		
		btnLogOn.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
			
				try {
					ServerParameters.my_queue_name = txtMyQueue.getText();
					
					 
					
							
					
					t = new Thread(new Recv(), "listener");
					t.start();
					btnSendMessage.setEnabled(true);
			
					// Connect user queue to blubber.all fanout exchange for broadcasts.
					// QueueMgmt.bindQueueFanout(ServerParameters.my_queue_name, "blubber.all");
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		JButton btnLogOff = new JButton("Log off");
		btnLogOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t.interrupt();
				btnSendMessage.setEnabled(false);	
			}
		});
		
		btnLogOff.setBounds(504, 16, 111, 23);
		panel.add(btnLogOff);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 650, 21);
		frmBlubberMessenger.getContentPane().add(menuBar);
		
		JMenu mnBlubber = new JMenu("Blubber");
		menuBar.add(mnBlubber);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Exit");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					QueueMgmt.deleteQueue(ServerParameters.my_queue_name);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.exit(0);
			
			}
		});
		
		mnBlubber.add(mntmNewMenuItem);
		
		JLabel lblNewLabel_3 = new JLabel("New label");
		lblNewLabel_3.setIcon(new ImageIcon(MainScreen.class.getResource("/resources/blubber.jpg")));
		lblNewLabel_3.setBounds(410, 49, 225, 186);
		frmBlubberMessenger.getContentPane().add(lblNewLabel_3);
		
		
		
	}


	public static void updateHistory(String toconcat)
	{	
			messageHistory = txtHistory.getText();
			messageHistory = messageHistory.concat(toconcat);
			messageHistory = messageHistory.concat("\r\n");
			txtHistory.setText(messageHistory);
			// JScrollBar vertical = scpHistory.getVerticalScrollBar();
			// vertical.setValue( vertical.getMaximum());
			txtHistory.setCaretPosition(txtHistory.getDocument().getLength());

	}
	
	
}



