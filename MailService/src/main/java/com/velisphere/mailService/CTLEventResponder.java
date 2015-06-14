package com.velisphere.mailService;

import java.io.IOException;
import java.util.HashMap;


import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import com.velisphere.fs.sdk.CTLListener;
import com.velisphere.fs.sdk.MessageFabrik;
import com.velisphere.fs.sdk.Server;
import com.velisphere.fs.sdk.config.ConfigData;

import flightsim.simconnect.config.ConfigurationNotFoundException;
public class CTLEventResponder implements CTLListener {

	@Override
	public void isAliveRequested() {

		System.out.println(" [MS] IsAlive Requested...");
		
		HashMap<String, String> messageHash = new HashMap<String, String>();
		messageHash.put("setState", "REACHABLE");
		
		try {
			Server.sendHashTable(messageHash, ConfigData.epid, "CTL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void allPropertiesRequested() {

		System.out.println(" [MS] AllProperties Requested, but not supported by Simple Mail Service");
		
		
	}

	@Override
	public void cnfMessage(String message) {
		System.out.println(" [MS] CnfMessage Received");
		
		
	}

	@Override
	public void regMessage(String message) {
		// TODO Auto-generated method stub
		
	System.out.println(" [MS] RegMessage Received");
	
	
	// unpack message
	
	String emailContent;
	
	try {
		emailContent = MessageFabrik.extractProperty(message,
				"c22251bd-accc-43d8-a732-8dae3c4781cc");
		
		if (!emailContent.isEmpty())
		{
	
			
			// send to mail
			
			
			final String username = "bepart@connectedthingslab.com";
			final String password = "1Suplies!";
	 
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
	 
			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			  });
	 
			try {
	 
				Message emailMessage = new MimeMessage(session);
				emailMessage.setFrom(new InternetAddress("bepart@connectedthingslab.com"));
				
				emailMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse("thorsten@thorsten-meudt.de"));
				emailMessage.setSubject("Testing Subject");
				emailMessage.setText("Dear Mail Crawler,"
					+ "\n\n No spam to my email, please! Content was " + emailContent);
	 
				Transport.send(emailMessage);
	 
				System.out.println(" [MS] E-Mail sent.");
	 
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
			
			
			
		}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	
	}
		
	
	
		
		
	}
	
	
	
}