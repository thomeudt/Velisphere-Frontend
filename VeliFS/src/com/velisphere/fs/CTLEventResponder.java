package com.velisphere.fs;

import java.io.IOException;
import java.util.HashMap;

import com.velisphere.fs.sdk.CTLListener;
import com.velisphere.fs.sdk.Server;

public class CTLEventResponder implements CTLListener {
   
	@Override
	public void isAliveRequested() {

		System.out.println("IsAlive Requested...");
		
		HashMap<String, String> messageHash = new HashMap<String, String>();
		messageHash.put("STATE", "REACHABLE");
		
		try {
			Server.sendHashTable(messageHash, "controller");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void allPropertiesRequested() {

		System.out.println("AllProperties Requested, but not supported by VeliFS");
		
		
	}
}