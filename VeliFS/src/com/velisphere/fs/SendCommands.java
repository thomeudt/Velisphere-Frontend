package com.velisphere.fs;

import java.util.HashMap;

import com.velisphere.fs.sdk.Server;
import com.velisphere.fs.sdk.ServerParameters;

import flightsim.simconnect.SimConnect;
import flightsim.simconnect.data.LatLonAlt;
import flightsim.simconnect.recv.RecvSimObjectDataByType;

public class SendCommands {

	
	public static void sendAllProperties(SimConnect sender,	RecvSimObjectDataByType e)
	{
	// retrieve data structures from packet
	LatLonAlt position = e.getLatLonAlt();
	String atcType = e.getDataString32();
	String atcID = e.getDataString32();
	Float altitude = e.getDataFloat32();
	Float groundspeed = (float) (e.getDataFloat32() * 3.6);
	Integer stall = e.getDataInt32();
	Integer overspeed = e.getDataInt32();
	Float aoA = (float) (e.getDataFloat32() * 57.2957795);
	Float lat = (float) ((e.getDataFloat32() * 180) / Math.PI);
	Float lon = (float) ((e.getDataFloat32() * 180) / Math.PI);

	// print to users
	System.out.println("Plane id#" + e.getObjectID() + " no "
			+ e.getEntryNumber() + "/" + e.getOutOf());
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
	if (e.getEntryNumber() == e.getOutOf())
		System.out.println();

	// send to Veli

	HashMap<String, String> messageHash = new HashMap<String, String>();

	// messageHash.put("4dbfba15-7297-4349-b152-b32ca81b2bd4",
	// position.toString());
	messageHash.put("1000a75c-ca98-479c-bbf1-3b00d71f99c5",
			String.valueOf(altitude));
	messageHash.put("aa89dd01-29ea-4bad-9bd3-0b2a2fa394ed",
			String.valueOf(atcType));
	messageHash.put("15dcc770-674a-4e02-90ca-2417625180bb",
			String.valueOf(atcID));
	messageHash.put("247771e7-65e5-4fe5-9ac5-3efb21d0ae1d",
			String.valueOf(groundspeed));
	messageHash.put("5c651829-20d9-4f21-a096-e1cba9fd349a",
			String.valueOf(stall));
	messageHash.put("7f714d19-ff38-45df-9ef3-47ce772f87b6",
			String.valueOf(overspeed));
	messageHash.put("6d9ef23a-1fd9-44e9-9ce2-7a7ed1014f45",
			String.valueOf(aoA));
	messageHash.put("2d677e0f-794a-48e3-8751-2ab521d37571",
			String.valueOf(lat));
	messageHash.put("9897e7b4-8bcf-4022-aa20-f037f3859f91",
			String.valueOf(lon));
	messageHash.put("5d2b15b6-7587-4055-abeb-2de246125ef4", "{"
			+ String.valueOf(lat) + "}" + "[" + String.valueOf(lon)
			+ "]");

	System.out.println("Message Hash Sent to Controller: "
			+ messageHash);

	try {
		Server.sendHashTable(messageHash, ServerParameters.my_queue_name, "REG");
	} catch (Exception e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

}

}
