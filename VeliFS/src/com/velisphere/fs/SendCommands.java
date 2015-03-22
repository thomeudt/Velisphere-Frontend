package com.velisphere.fs;

import java.util.HashMap;

import sensors.Constants;

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
	Integer flap = e.getDataInt32();
	Integer spoiler = e.getDataInt32();
	Integer spoilerArmed = e.getDataInt32();
	Integer windSpeed = e.getDataInt32();
	Integer windDirect = e.getDataInt32();
	Float pitch = (float) (e.getDataFloat32() * 57.2957795);
	Float bank = (float) (e.getDataFloat32() * 57.2957795);
	Integer onGround = e.getDataInt32();
	Integer glideslopeError = e.getDataInt32();
	Integer gpsETE = e.getDataInt32();
	Integer temperatureTAT = e.getDataInt32();
	Integer totalWeight = e.getDataInt32();
	Integer autoBrake = e.getDataInt32();
	
	
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
	messageHash.put(Constants.altitude,
			String.valueOf(altitude));
	messageHash.put(Constants.atcType,
			String.valueOf(atcType));
	messageHash.put(Constants.atcID,
			String.valueOf(atcID));
	messageHash.put(Constants.groundspeed,
			String.valueOf(groundspeed));
	messageHash.put(Constants.stall,
			String.valueOf(stall));
	messageHash.put(Constants.overspeed,
			String.valueOf(overspeed));
	messageHash.put(Constants.overspeed,
			String.valueOf(aoA));
	messageHash.put(Constants.lat,
			String.valueOf(lat));
	messageHash.put(Constants.lon,
			String.valueOf(lon));
	messageHash.put(Constants.pos, "{"
			+ String.valueOf(lat) + "}" + "[" + String.valueOf(lon)
			+ "]");
	messageHash.put(Constants.flap,
			String.valueOf(flap));
	messageHash.put(Constants.spoiler,
			String.valueOf(spoiler));
	messageHash.put(Constants.spoilerArmed,
			String.valueOf(spoilerArmed));
	messageHash.put(Constants.windSpeed,
			String.valueOf(windSpeed));
	messageHash.put(Constants.windDirect,
			String.valueOf(windDirect));
	messageHash.put(Constants.pitch,
			String.valueOf(pitch));
	messageHash.put(Constants.bank,
			String.valueOf(bank));
	messageHash.put(Constants.onGround,
			String.valueOf(onGround));
	messageHash.put(Constants.glideslopeError,
			String.valueOf(glideslopeError));
	messageHash.put(Constants.gpsETE,
			String.valueOf(gpsETE));
	messageHash.put(Constants.temperatureTAT,
			String.valueOf(temperatureTAT));
	messageHash.put(Constants.totalWeight,
			String.valueOf(totalWeight));
	messageHash.put(Constants.autoBrake,
			String.valueOf(autoBrake));
	
	
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
