package com.velisphere.fs;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import de.newsarea.homecockpit.fsuipc.FSUIPCInterface;
import de.newsarea.homecockpit.fsuipc.domain.ByteArray;
import de.newsarea.homecockpit.fsuipc.domain.OffsetIdent;
import de.newsarea.homecockpit.fsuipc.domain.OffsetItem;
import de.newsarea.homecockpit.fsuipc.event.OffsetEventListener;
import de.newsarea.homecockpit.fsuipc.flightsim.FSUIPCFlightSimInterface;
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

public class FsConnectV2 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		// Start AMQP Listener
		ServerParameters.my_queue_name = "dec188f1-68d7-47b4-9f6c-b6fccfdbe8ec";
					
					
					
		
		final int dataDefID = 1;
		final int requestID = 1;
		final int fourSeceventID = 1;
		// connect to simconnect
		SimConnect sc = new SimConnect("AIList");
		// build data definition
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
			public void handleOpen(SimConnect sender, RecvOpen e) {
				System.out.println("Connected to " + e.getApplicationName());
			}
		});
		// add an event handler to receive events every 4 seconds
		dt.addEventHandler(new EventHandler() {
			public void handleEvent(SimConnect sender, RecvEvent e) {
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
		dt.addSimObjectDataTypeHandler(new SimObjectDataTypeHandler() {
			public void handleSimObjectType(SimConnect sender,
					RecvSimObjectDataByType e) {
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
				
				// push to chai
				
				HashMap<String, String> messageHash = new HashMap<String, String>();
							
				
				// messageHash.put("4dbfba15-7297-4349-b152-b32ca81b2bd4", position.toString());
				messageHash.put("45d97eae-6ec2-41ca-bbc8-0c0a0565aca1", String.valueOf(altitude));
				messageHash.put("d52186f1-e294-495c-867c-a5f7cce0bad5", String.valueOf(atcType));
				messageHash.put("e8689591-2dd5-4e5c-943f-ea6d6a51b0d4", String.valueOf(atcID));
				messageHash.put("05652ea8-aec7-47c1-86ca-3aba685491c2", String.valueOf(groundspeed));
				messageHash.put("d8ff5d02-904f-4f3f-ade6-c21fb45a6977", String.valueOf(stall));
				messageHash.put("8228f974-92fe-44a4-ba84-ae3e1d90b241", String.valueOf(overspeed));
				messageHash.put("0e5b6343-3b3e-4291-be68-179f1dc15744", String.valueOf(aoA));
				messageHash.put("aa1fe0aa-1ce2-433c-91b5-3367a5b1c377", String.valueOf(lat));
				messageHash.put("4d99ee65-b2a3-4057-b006-7acb9b14e9f8", String.valueOf(lon));
				messageHash.put("11f8b715-0311-4329-a478-d78211e55fc6", "{"+String.valueOf(lat)+"}"+"["+String.valueOf(lon)+"]");
				
				
				
				ExecutorService senderService = Executors
						.newFixedThreadPool(10);

				Thread senderThread;
						senderThread = new Thread(new SenderTask(messageHash, "controller"),
								"send");
						senderService.execute(senderThread);
				
				
				
				
				
						
				
			}
		});
		// spawn receiver thread
		new Thread(dt).start();

	}

}
