package com.velisphere.fs.actors;

import java.io.IOException;

import flightsim.simconnect.config.ConfigurationNotFoundException;

public class SimFunctions {

	public static void pause() throws IOException, ConfigurationNotFoundException
	{
		ExecuteSimConnect.sendEvent("PAUSE_TOGGLE", "0", "0");
	}
	
}
