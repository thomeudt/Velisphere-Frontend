package com.velisphere.fs;

import java.util.HashMap;

public class SenderTask implements Runnable {


	HashMap<String, String> messageHash;
	String controller;

	


	public SenderTask(HashMap<String, String> messageHash, String controller) {
		this.controller = controller;
		this.messageHash = messageHash;
	}

	
		
		
	

	@Override
	public void run() {
		try {
			Send.sendHashTable(messageHash, controller);
			System.out.println("Message Hash Sent to Controller: " + messageHash);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}