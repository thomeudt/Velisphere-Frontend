package com.velisphere.fs.actors;

import java.io.IOException;

import flightsim.simconnect.SimConnectDataType;

public class Flaps {
	
	public static void up() throws IOException{
		ExecuteSimConnect.setVariable(1, "FLAPS HANDLE INDEX", "", SimConnectDataType.INT32, 1);
		
	}
	
	public static void one() throws IOException{
		ExecuteSimConnect.setVariable(1, "FLAPS HANDLE INDEX", "", SimConnectDataType.INT32, 2);
		
	}
	
	public static void two() throws IOException{
		ExecuteSimConnect.setVariable(1, "FLAPS HANDLE INDEX", "", SimConnectDataType.INT32, 3);
		
	}

	public static void three() throws IOException{
		ExecuteSimConnect.setVariable(1, "FLAPS HANDLE INDEX", "", SimConnectDataType.INT32, 4);
		
	}
	
	public static void full() throws IOException{
		ExecuteSimConnect.setVariable(1, "FLAPS HANDLE INDEX", "", SimConnectDataType.INT32, 5);
		
	}


}
