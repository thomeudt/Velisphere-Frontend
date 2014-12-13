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
		//try {
			
			// Start AMQP Listener
			ServerParameters.my_queue_name = "26999b45-f90f-4f3e-8e83-457ccdb1298d";
			
			
			
			sc = new SimConnect("AIList");
				
			
			
			sc.addToDataDefinition(dataDefID, "STRUCT LATLONALT", null, SimConnectDataType.LATLONALT);
			sc.addToDataDefinition(dataDefID, "ATC TYPE", null, SimConnectDataType.STRING32);
			sc.addToDataDefinition(dataDefID, "ATC ID", null, SimConnectDataType.STRING32);
			sc.addToDataDefinition(dataDefID, "PLANE ALTITUDE", null, SimConnectDataType.FLOAT32);
			sc.addToDataDefinition(dataDefID, "GPS GROUND SPEED", null, SimConnectDataType.FLOAT32);
			sc.addToDataDefinition(dataDefID, "STALL WARNING", null, SimConnectDataType.INT32);
			sc.addToDataDefinition(dataDefID, "OVERSPEED WARNING", null, SimConnectDataType.INT32);
			sc.addToDataDefinition(dataDefID, "INCIDENCE ALPHA", null, SimConnectDataType.FLOAT32);		
			sc.addToDataDefinition(dataDefID, "PLANE LATITUDE", null, SimConnectDataType.FLOAT32);
			sc.addToDataDefinition(dataDefID, "PLANE LONGITUDE", null, SimConnectDataType.FLOAT32);
						
			
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
					Float lat = (float) ((e.getDataFloat32()*180)/Math.PI);
					Float lon = (float) ((e.getDataFloat32()*180)/Math.PI);
					
					// print to users
					System.out.println("Plane id#" + e.getObjectID() + " no " + e.getEntryNumber() + "/" + e.getOutOf());
					System.out.println("\tPosition: " + position.toString());
					System.out.println("\tType/ID: " + atcType + " " + atcID);
					System.out.println("\tAltitude: " + altitude.toString());
					System.out.println("\tGround Speed: " + groundspeed.toString());
					System.out.println("\tStall: " + stall.toString());
					System.out.println("\tOverspeed: " + overspeed.toString());
					System.out.println("\tAoA: " + aoA.toString());
					System.out.println("\tLatitude: " + lat.toString());
					System.out.println("\tLogitude: " + lon.toString());
					// line separator
					if (e.getEntryNumber() == e.getOutOf()) System.out.println();
					
					// send to Veli
					
					
					
						
						HashMap<String, String> messageHash = new HashMap<String, String>();
						
						// messageHash.put("4dbfba15-7297-4349-b152-b32ca81b2bd4", position.toString());
						messageHash.put("713524b6-d77a-45e3-aead-82e786ac2f15", String.valueOf(altitude));
						messageHash.put("725bd22e-c912-4d54-8c42-09c4935611e5", String.valueOf(atcType));
						messageHash.put("c5994de5-9762-4df4-840e-a30109784c8a", String.valueOf(atcID));
						messageHash.put("acd4fc06-d01b-4ec0-a17c-13d2d1d89a2a", String.valueOf(groundspeed));
						messageHash.put("229da09b-8b1c-46a4-8ada-3f72f30a7b19", String.valueOf(stall));
						messageHash.put("1fb601dc-f3c2-4f12-8dd0-8f6a590681f6", String.valueOf(overspeed));
						messageHash.put("224365cc-5973-4510-bb8a-8bd7b249e451", String.valueOf(aoA));
						messageHash.put("fbafb133-9b4a-4104-a140-0eecb99e11f0", String.valueOf(lat));
						messageHash.put("a9225f76-b326-48b4-93e3-b878ca4a8ad1", String.valueOf(lon));
						messageHash.put("3ddb8d2e-75dc-4375-8ba1-7d9c0694487e", "{"+String.valueOf(lat)+"}"+"["+String.valueOf(lon)+"]");
						
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
			

			
			
			/*
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
				
	}
	
	
	
	

}
