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
			ServerParameters.my_queue_name = "cd24cbbc-f163-4e80-8004-2ff626042cc0";
			
			
			
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
						// messageHash.put("b0e66a0c-2ebe-4f36-961a-9810bb26957e", String.valueOf(altitude));
						messageHash.put("b13dbdbb-e355-467e-8c13-0ade7d6b5d93", String.valueOf(atcType));
						//messageHash.put("a078b27f-57f1-4bf8-8106-d063718cafae", String.valueOf(atcID));
						messageHash.put("7e4da755-77de-4943-95d9-f15e778da8e5", String.valueOf(groundspeed));
						//messageHash.put("68fbe9fd-8377-4dc2-836b-a96ef28d9047", String.valueOf(stall));
						//messageHash.put("7db9deb1-caee-4dfc-8066-620a3a66b71f", String.valueOf(overspeed));
						//messageHash.put("009ce1d1-ef2d-4e42-9d45-204a6ff2109d", String.valueOf(aoA));
						messageHash.put("5e9c6361-913a-4b63-a6b9-a5d2b1a530c0", String.valueOf(lat));
						messageHash.put("55167544-c84c-41ad-83fb-9960efdf5a88", String.valueOf(lon));
						
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
