package com.velisphere.fs.actors;

import java.io.IOException;

import flightsim.simconnect.config.ConfigurationNotFoundException;

public class AutoBrake {
	
	public static void increment() throws IOException, ConfigurationNotFoundException
	{
		ExecuteSimConnect.sendEvent("INCREASE_AUTOBRAKE_CONTROL", "0", "0");
	}
	
	public static void decrement() throws IOException, ConfigurationNotFoundException
	{
		ExecuteSimConnect.sendEvent("DECREASE_AUTOBRAKE_CONTROL", "0", "0");
	}


}
