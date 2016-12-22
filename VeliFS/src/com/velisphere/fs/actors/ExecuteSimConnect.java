package com.velisphere.fs.actors;

import java.io.IOException;

import flightsim.simconnect.NotificationPriority;
import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectConstants;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.config.ConfigurationNotFoundException;
import flightsim.simconnect.recv.DispatcherTask;
import flightsim.simconnect.recv.ExceptionHandler;
import flightsim.simconnect.recv.OpenHandler;
import flightsim.simconnect.recv.RecvEvent;
import flightsim.simconnect.recv.RecvException;
import flightsim.simconnect.recv.RecvOpen;
import flightsim.simconnect.wrappers.DataWrapper;

public class ExecuteSimConnect {
	
		
	public static void setVariable(int dataDefintionID, String datumName, String unitsName, SimConnectDataType dataType, int targetValue) throws IOException {
		SimConnect sc = new SimConnect("VeliFS");
		
		sc.addToDataDefinition(dataDefintionID, datumName, unitsName, dataType);
		DataWrapper dw = new DataWrapper(4);
		dw.putInt32(targetValue);
		sc.setDataOnSimObject(dataDefintionID, 1, false, 1,  dw);
		
		DispatcherTask dt = new DispatcherTask(sc);
		dt.addOpenHandler(new OpenHandler() {
			public void handleOpen(SimConnect sender, RecvOpen e) {
				System.out.println("Connected : " + e.toString());
			}

			});
		dt.addExceptionHandler(new ExceptionHandler() {
			public void handleException(SimConnect sender, RecvException e) {
				System.out.println("Error: " + e.getException() + " packet " + e.getSendID() + " arg "+ e.getIndex());
			}
			});
		new Thread(dt).start();
		
	}
	
	public static void setVariable(int dataDefintionID, String datumName, String unitsName, SimConnectDataType dataType, String targetValue) throws IOException {
		SimConnect sc = new SimConnect("VeliFS");
		
		sc.addToDataDefinition(dataDefintionID, datumName, unitsName, dataType);
		DataWrapper dw = new DataWrapper(8);
	
		dw.putString8(targetValue);
		sc.setDataOnSimObject(dataDefintionID, 1, false, 1,  dw);
		
		DispatcherTask dt = new DispatcherTask(sc);
		dt.addOpenHandler(new OpenHandler() {
			public void handleOpen(SimConnect sender, RecvOpen e) {
				System.out.println("Connected : " + e.toString());
			}

			});
		dt.addExceptionHandler(new ExceptionHandler() {
			public void handleException(SimConnect sender, RecvException e) {
				System.out.println("Error: " + e.getException() + " packet " + e.getSendID() + " arg "+ e.getIndex());
			}
			});
		new Thread(dt).start();
		
	}
	
	public static void sendEvent(String EventName, String requestCid, String requestParam) throws IOException, ConfigurationNotFoundException {
		
		
		SimConnect sc = new SimConnect("SendEvent", 0);
		
		sc.mapClientEventToSimEvent(1, EventName);
		int cid = 0;
		int param = 0;
		if (!requestCid.isEmpty()) {
			cid = Integer.parseInt(requestCid, 16);
		}
		if (!requestParam.isEmpty()) {
			param = Integer.parseInt(requestParam);
		}
		System.out.println("Sending to " + Integer.toHexString(cid));
		sc.transmitClientEvent(cid, 1, param, NotificationPriority.HIGHEST.ordinal(), SimConnectConstants.EVENT_FLAG_GROUPID_IS_PRIORITY);
		
		DispatcherTask dt = new DispatcherTask(sc);
		dt.addOpenHandler(new OpenHandler(){
			public void handleOpen(SimConnect sender, RecvOpen e) {
				System.out.println("Connected");
				
			}
		});
		dt.addExceptionHandler(new ExceptionHandler(){
			public void handleException(SimConnect sender, RecvException e) {
				System.out.println("Exception (" + e.getException() +") packet " + e.getSendID());
			}
		});
		
		sc.callDispatch(dt);
		
		
	}
	
		
}
