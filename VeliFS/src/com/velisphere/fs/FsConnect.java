package com.velisphere.fs;



import java.io.IOException;
import java.util.HashMap;




import flightsim.simconnect.SimConnect;
import flightsim.simconnect.SimConnectDataType;
import flightsim.simconnect.SimObjectType;
import flightsim.simconnect.data.LatLonAlt;
import flightsim.simconnect.recv.DispatcherTask;
import flightsim.simconnect.recv.EventHandler;
import flightsim.simconnect.recv.OpenHandler;
import flightsim.simconnect.recv.RecvEvent;
import flightsim.simconnect.recv.RecvOpen;
import flightsim.simconnect.recv.RecvSimObjectDataByType;
import flightsim.simconnect.recv.SimObjectDataTypeHandler;



public class FsConnect {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		final int dataDefID = 1;
		final int requestID = 1;
		final int fourSeceventID = 1;
		// connect to simconnect
		SimConnect sc;
		try {
			
			// Start AMQP Listener
			ServerParameters.my_queue_name = "9f3ddf0a-4202-4d2c-84ad-6608ba777485";
			
			
			
			sc = new SimConnect("AIList");
				
			
			
			sc.addToDataDefinition(dataDefID, "STRUCT LATLONALT", null, SimConnectDataType.LATLONALT);
			sc.addToDataDefinition(dataDefID, "ATC TYPE", null, SimConnectDataType.STRING32);
			sc.addToDataDefinition(dataDefID, "ATC ID", null, SimConnectDataType.STRING32);
			sc.addToDataDefinition(dataDefID, "PLANE ALTITUDE", null, SimConnectDataType.FLOAT32);
			sc.addToDataDefinition(dataDefID, "GPS GROUND SPEED", null, SimConnectDataType.FLOAT32);
			sc.addToDataDefinition(dataDefID, "STALL WARNING", null, SimConnectDataType.INT32);
			sc.addToDataDefinition(dataDefID, "OVERSPEED WARNING", null, SimConnectDataType.INT32);
			sc.addToDataDefinition(dataDefID, "INCIDENCE ALPHA", null, SimConnectDataType.FLOAT32);		
						
			
			// get warned every 4 seconds when in sim mode
			sc.subscribeToSystemEvent(fourSeceventID, "4sec");
			DispatcherTask dt = new DispatcherTask(sc);
			// just for esthetic purposes
			dt.addOpenHandler(new OpenHandler() {
				
				@Override
				public void handleOpen(SimConnect sender, RecvOpen e) {
					// TODO Auto-generated method stub
					
					System.out.println("Connected to " + e.getApplicationName());
					
				}});
			// add an event handler to receive events every 4 seconds
			dt.addEventHandler(new EventHandler() {
			
				@Override
				public void handleEvent(SimConnect sender, RecvEvent e) {
					// TODO Auto-generated method stub
					if (e.getEventID() == fourSeceventID) {
						// request data for all aircrafts in the sim
						try {
							sender.requestDataOnSimObjectType(requestID, dataDefID, 100*1000, SimObjectType.USER);
						} catch (IOException ioe) {}
					}
				}});
			// handler called when received data requested by the call to
			// requestDataOnSimObjectType
			dt.addSimObjectDataTypeHandler(new SimObjectDataTypeHandler() {
				@Override
				public void handleSimObjectType(SimConnect sender,
						RecvSimObjectDataByType e) {
					// TODO Auto-generated method stub
					
					// retrieve data structures from packet
					LatLonAlt position = e.getLatLonAlt();
					String atcType = e.getDataString32();
					String atcID = e.getDataString32();
					Float altitude = e.getDataFloat32();
					Float groundspeed = (float) (e.getDataFloat32() * 3.6);
					Integer stall = e.getDataInt32();
					Integer overspeed = e.getDataInt32();
					Float aoA = (float) (e.getDataFloat32() * 57.2957795);
					// print to users
					System.out.println("Plane id#" + e.getObjectID() + " no " + e.getEntryNumber() + "/" + e.getOutOf());
					System.out.println("\tPosition: " + position.toString());
					System.out.println("\tType/ID: " + atcType + " " + atcID);
					System.out.println("\tAltitude: " + altitude.toString());
					System.out.println("\tGround Speed: " + groundspeed.toString());
					System.out.println("\tStall: " + stall.toString());
					System.out.println("\tOverspeed: " + overspeed.toString());
					System.out.println("\tAoA: " + aoA.toString());
					// line separator
					if (e.getEntryNumber() == e.getOutOf()) System.out.println();
					
					// send to Veli
					
					
					
						
						HashMap<String, String> messageHash = new HashMap<String, String>();
						
						messageHash.put("c1c7d783-7dff-40ec-bb15-4c27e0de1a92", position.toString());
						messageHash.put("46014829-2a8c-4bea-b83e-3dc67668f4bf", String.valueOf(altitude));
						messageHash.put("44651f79-bdcd-43db-89fe-70b8bad940b1", String.valueOf(groundspeed));
						messageHash.put("e32f37e2-f062-4b24-bd4a-d21a55af14b4", String.valueOf(stall));
						messageHash.put("f124b4b1-e4eb-45ec-9a70-5bff490fe9b4", String.valueOf(overspeed));
						messageHash.put("eab47d76-4065-471b-9bb1-922e62acb902", String.valueOf(aoA));
						
						System.out.println("Message Hash Sent to Controller: " + messageHash);
						
						
						try {
							Send.sendHashTable(messageHash, "controller");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
								
					
					
					
					
				
						
						} 
					}); 
				 
					
		
					
					
					
				
			// spawn receiver thread
			 
			 	 
			
			new Thread(dt).start();
			

			
			
			
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
				
	}
	
	
	
	

}
