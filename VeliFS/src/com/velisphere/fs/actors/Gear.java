package com.velisphere.fs.actors;

import java.io.IOException;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.recv.DispatcherTask;
import flightsim.simconnect.recv.ExceptionHandler;
import flightsim.simconnect.recv.OpenHandler;
import flightsim.simconnect.recv.RecvException;
import flightsim.simconnect.recv.RecvOpen;
import flightsim.simconnect.wrappers.DataWrapper;

public class Gear {
	
	public static void up() throws IOException{
		ExecuteSimConnect.setVariable(1, "GEAR HANDLE POSITION", "", SimConnectDataType.INT32, 0);
		
	}
	
	public static void down() throws IOException{
		ExecuteSimConnect.setVariable(1, "GEAR HANDLE POSITION", "", SimConnectDataType.INT32, 1);
		
	}

}
