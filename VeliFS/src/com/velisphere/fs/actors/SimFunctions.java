package com.velisphere.fs.actors;

import java.io.IOException;
import java.util.HashMap;
import com.velisphere.fs.sensorsAndConfigs.Constants;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.config.ConfigurationNotFoundException;

public class SimFunctions {

	public static void pause() throws IOException, ConfigurationNotFoundException
	{
		ExecuteSimConnect.sendEvent("PAUSE_TOGGLE", "0", "0");
	}
	
	public static void setFlightNumber(String flightNumber) throws IOException, ConfigurationNotFoundException
	{
		// set value
		
		ExecuteSimConnect.setVariable(1, "ATC FLIGHT NUMBER", "", SimConnectDataType.STRING8, flightNumber);
		System.out.println(" [ IN ] ATC Flight Number set to " + flightNumber);
		
	
	}
	
}
