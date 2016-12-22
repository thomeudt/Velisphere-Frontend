package com.velisphere.fs.actors;

import java.io.IOException;

import flightsim.simconnect.config.ConfigurationNotFoundException;

public class Spoilers {
	
	public static void toggleArmed() throws IOException, ConfigurationNotFoundException
	{
		ExecuteSimConnect.sendEvent("SPOILERS_ARM_TOGGLE", "0", "0");
	}
	
	public static void toggleFull() throws IOException, ConfigurationNotFoundException
	{
		ExecuteSimConnect.sendEvent("SPOILERS_TOGGLE", "0", "0");
	}

}
