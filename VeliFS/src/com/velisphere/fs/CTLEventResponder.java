package com.velisphere.fs;

import java.io.IOException;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.velisphere.fs.actors.SimFunctions;
import com.velisphere.fs.sdk.CTLListener;
import com.velisphere.fs.sdk.MessageFabrik;
import com.velisphere.fs.sdk.Server;
import com.velisphere.fs.sdk.ServerParameters;
import com.velisphere.fs.sdk.config.ConfigData;

import flightsim.simconnect.config.ConfigurationNotFoundException;

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

	@Override
	public void cnfMessage(String message) {
		String msgSetFlightNumber;
		try {
			msgSetFlightNumber = MessageFabrik.extractProperty(message,
					"0d5af911-2bff-4bf8-b1f3-132c5671930e");
			if (!msgSetFlightNumber.isEmpty())
			{
				SimFunctions.setFlightNumber(msgSetFlightNumber);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConfigurationNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
	}

	@Override
	public void regMessage(String message) {
		// TODO Auto-generated method stub
		
	}

	
}