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

		System.out
				.println(" [MS] AllProperties Requested, but not supported by Simple Mail Service");

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
					"b32e3663-2e9b-4acc-8c4a-4eb6dc47ae2a");

			if (!emailContent.isEmpty()) {

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
								return new PasswordAuthentication(username,
										password);
							}
						});

				try {

					Message emailMessage = new MimeMessage(session);
					emailMessage.setFrom(new InternetAddress(
							"bepart@connectedthingslab.com"));

					emailMessage
							.setRecipients(
									Message.RecipientType.TO,
									InternetAddress
											.parse(MessageFabrik.extractProperty(emailContent, "recipient")));
					emailMessage.setSubject(MessageFabrik.extractProperty(emailContent, "subject"));
					emailMessage.setContent(MessageFabrik.extractProperty(emailContent, "text"), "text/html; charset=utf-8");
					

					Transport.send(emailMessage);

					System.out.println(" [MS] E-Mail sent.");

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}

			}
			else System.out.println(" [MS] No E-Mail sent.");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}

	}
	
	

}