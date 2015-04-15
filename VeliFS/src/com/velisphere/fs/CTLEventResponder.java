package com.velisphere.fs;

import java.io.IOException;
import java.util.HashMap;

import com.velisphere.fs.sdk.CTLListener;
import com.velisphere.fs.sdk.Server;
import com.velisphere.fs.sdk.ServerParameters;
import com.velisphere.fs.sdk.config.ConfigData;

public class CTLEventResponder implements CTLListener {
   
	@Override
	public void isAliveRequested() {

		System.out.println("IsAlive Requested...");
		
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

		System.out.println("AllProperties Requested, but not supported by VeliFS");
		
		
	}
}