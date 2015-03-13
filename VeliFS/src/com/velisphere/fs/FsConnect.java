package com.velisphere.fs;



import java.io.IOException;
import java.util.HashMap;
import com.velisphere.fs.sdk.Server;
import com.velisphere.fs.sdk.ServerParameters;
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
			ServerParameters.my_queue_name = "4356e2b5-abd4-40b5-9f9d-cc3b52eb564f";
			
			
			
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
						messageHash.put("d1d620f5-f7be-42bc-8e85-3383a38341aa", String.valueOf(altitude));
						messageHash.put("b3b2d9f4-4824-45ca-b117-313a46b46061", String.valueOf(atcType));
						messageHash.put("d42cd2d9-e2be-4f3f-aa76-562007c2b6d1", String.valueOf(atcID));
						messageHash.put("23ffd953-5b9c-4a71-bb50-c0507e75b04f", String.valueOf(groundspeed));
						messageHash.put("e610a30a-25ef-4119-bdfd-998342883612", String.valueOf(stall));
						messageHash.put("36752e74-81ff-4654-9efe-5b55e2119ff0", String.valueOf(overspeed));
						messageHash.put("c4c30bd5-4329-4ad3-9f86-dcdc33dc2a2d", String.valueOf(aoA));
						messageHash.put("ceff2e16-674b-4ad5-a26a-20a010f7d6cf", String.valueOf(lat));
						messageHash.put("3ba9aa03-264a-4571-ad7a-d1f0f91c22c5", String.valueOf(lon));
						messageHash.put("e9afe92e-d46b-4f6f-b793-e5213d3aef28", "{"+String.valueOf(lat)+"}"+"["+String.valueOf(lon)+"]");
						
						System.out.println("Message Hash Sent to Controller: " + messageHash);
						
						
						try {
							Server.sendHashTable(messageHash, "controller");
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
