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

public class GPWS {

	public static void inhibitGPWS() throws IOException {
		
		ExecuteSimConnect.setVariable(1, "GPWS SYSTEM ACTIVE", "", SimConnectDataType.INT32, 1);
	}

}