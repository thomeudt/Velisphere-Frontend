package com.velisphere.fs;

import java.io.IOException;
import java.util.HashMap;

import com.velisphere.fs.actors.Flaps;
import com.velisphere.fs.actors.Gear;
import com.velisphere.fs.actors.SimFunctions;
import com.velisphere.milk.amqpClient.AmqpClient;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.SimObjectType;
import flightsim.simconnect.config.ConfigurationNotFoundException;
import flightsim.simconnect.data.LatLonAlt;
import flightsim.simconnect.recv.DispatcherTask;
import flightsim.simconnect.recv.EventHandler;
import flightsim.simconnect.recv.OpenHandler;
import flightsim.simconnect.recv.RecvEvent;
import flightsim.simconnect.recv.RecvOpen;
import flightsim.simconnect.recv.RecvSimObjectDataByType;
import flightsim.simconnect.recv.SimObjectDataTypeHandler;

public class FsConnect {

	static DispatcherTask dispatcherTask;
	static SimConnect simConnect;

	public static void main(String[] args) throws IOException  {
		// TODO Auto-generated method stub

		final int dataDefID = 1;
		final int requestID = 1;
		final int fourSeceventID = 1;

		
		// Activate Event Responders

		FsEventResponder eventResponder = new FsEventResponder();
		
		FsEventInitiator initiator = new FsEventInitiator();
		initiator.addListener(eventResponder);

		// Start Server

		AmqpClient.startClient(initiator);

		// Start SimConnect

		//Gear.down();
		//Flaps.full();
		
		
		simConnect = new SimConnect("VeliFS Listener");

		simConnect.addToDataDefinition(dataDefID, "STRUCT LATLONALT", null,
				SimConnectDataType.LATLONALT);
		simConnect.addToDataDefinition(dataDefID, "ATC TYPE", null,
				SimConnectDataType.STRING32);
		simConnect.addToDataDefinition(dataDefID, "ATC ID", null,
				SimConnectDataType.STRING32);
		simConnect.addToDataDefinition(dataDefID, "PLANE ALTITUDE", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "GPS GROUND SPEED", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "STALL WARNING", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "OVERSPEED WARNING", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "INCIDENCE ALPHA", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "PLANE LATITUDE", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "PLANE LONGITUDE", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "FLAPS HANDLE INDEX", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "SPOILERS LEFT POSITION", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "SPOILERS ARMED", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "AMBIENT WIND VELOCITY", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "AMBIENT WIND DIRECTION", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "PLANE PITCH DEGREES", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "PLANE BANK DEGREES", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "SIM ON GROUND", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "NAV GLIDE SLOPE ERROR:index", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "GPS ETE", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "TOTAL AIR TEMPERATURE", null,
				SimConnectDataType.FLOAT32);
		simConnect.addToDataDefinition(dataDefID, "TOTAL WEIGHT", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "AUTO BRAKE SWITCH CB", null,
				SimConnectDataType.INT32);
		simConnect.addToDataDefinition(dataDefID, "ATC FLIGHT NUMBER", null,
				SimConnectDataType.STRING8);
		
		
		// get warned every 4 seconds when in sim mode
		
		simConnect.subscribeToSystemEvent(fourSeceventID, "4sec");
				
		dispatcherTask = new DispatcherTask(simConnect);

		//	just for esthetic purposes
		dispatcherTask.addOpenHandler(new OpenHandler() {

			@Override
			public void handleOpen(SimConnect sender, RecvOpen e) {
				// TODO Auto-generated method stub

				System.out.println(" [IN] Connected to " + e.getApplicationName());

			}
		});
		// add an event handler to receive events every 4 seconds
		dispatcherTask.addEventHandler(new EventHandler() {

			@Override
			public void handleEvent(SimConnect sender, RecvEvent e) {
				// TODO Auto-generated method stub
				if (e.getEventID() == fourSeceventID) {
					// request data for all aircrafts in the sim
					try {
						sender.requestDataOnSimObjectType(requestID, dataDefID,
								100 * 1000, SimObjectType.USER);
					} catch (IOException ioe) {
					}
				}
			}
		});
		// handler called when received data requested by the call to
		// requestDataOnSimObjectType
		dispatcherTask.addSimObjectDataTypeHandler(new SimObjectDataTypeHandler() {
			@Override
			public void handleSimObjectType(SimConnect sender,
					RecvSimObjectDataByType e) {


					SendCommands.sendAllProperties(sender,	e);
				}
		});

		// spawn receiver thread

		new Thread(dispatcherTask).start();

		
		
		/*
		 * 
		 * } catch (IOException e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); }
		 */

	}
	
	public static DispatcherTask getDispatcherTask() {
		return dispatcherTask;
	}
	
	public static SimConnect getSimConnect() {
		return simConnect;
	}


}
