package PhidgetsExample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.maxmind.geoip.Location;
import com.maxmind.geoip.LookupService;
import com.maxmind.geoip.regionName;
import com.phidgets.*; 
import com.phidgets.event.*; 
import com.velisphere.milk.Configuration.ConfigData;
import com.velisphere.milk.amqpClient.AmqpClient.AmqpClient;
public class PhiTestAdvanced 
{ 
	public static InterfaceKitPhidget ik;
	
	public static final void main(String args[]) throws Exception { 
		 
		
		EventResponder eventResponder = new EventResponder();
		PhidgetEventInitiator initiator = new PhidgetEventInitiator();
		initiator.addListener(eventResponder);
	
		
		AmqpClient.startClient(initiator);
		
		
		System.out.println(Phidget.getLibraryVersion()); 
		ik = new InterfaceKitPhidget(); 
		ik.addAttachListener(new AttachListener() {
			public void attached(AttachEvent ae) { 
				System.out.println("attachment of " + ae); 
			} 
		}); 
		ik.addDetachListener(new DetachListener() { 
			public void detached(DetachEvent ae) { 
				System.out.println("detachment of " + ae); 
			} 
		}); 
		ik.addErrorListener(new ErrorListener() { 
			public void error(ErrorEvent ee) { 
				System.out.println("error event for " + ee); 
			} 
		}); 
		ik.addInputChangeListener(new InputChangeListener() { 
			public void inputChanged(InputChangeEvent oe) { 
				System.out.println(oe); 
			} 
		}); 
		ik.addOutputChangeListener(new OutputChangeListener() { 
			public void outputChanged(OutputChangeEvent oe) { 
				System.out.println(oe);
							} 
		}); 
		ik.addSensorChangeListener(new SensorChangeListener() { 
			public void sensorChanged(SensorChangeEvent se) { 
				System.out.println(se);
				String message = "Neuer Wert des Sensors "+se.getIndex() +" ist:" + se.getValue();

				
				try {
					// send to controller
					
					HashMap<String, String> messageHash = new HashMap<String, String>();
					
					// Sensor Data
					
					messageHash.put("075bbef2-4c53-46c0-826c-a7dc0f30cfb0", String.valueOf(ik.getSensorValue(6)));
					messageHash.put("d2544026-6a99-415f-98a7-6d1c55560886", String.valueOf(ik.getSensorValue(7)));
					messageHash.put("fa1840e6-b590-4aa9-9445-69cd1bbf3175", String.valueOf(ik.getSensorValue(5)));
					messageHash.put("d5d57ac8-6fa3-474e-b7af-5792b1d4964a", String.valueOf(ik.getSensorValue(1)));
				
					// Geo Location
					
					File dbfile = new File("GeoLiteCity.dat");
					LookupService lookupService = new LookupService(dbfile, LookupService.GEOIP_MEMORY_CACHE);

					URL whatismyip = new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(
					                whatismyip.openStream()));

					String ip = in.readLine(); //you get the IP as a String
					
					Location location = lookupService.getLocation(ip);

					// Populate region. Note that regionName is a MaxMind class, not an instance variable
					if (location != null) {
					    location.region = regionName.regionNameByCode(location.countryCode, location.region);
					}
					
					messageHash.put("7244bc76-8385-4bb5-b242-18cfa97d473b", "{"	+ String.valueOf(location.latitude) + "}" + 
							"[" + String.valueOf(location.longitude) + "]");
					
					
					// Send out
					
					System.out.println("Message Hash Sent to Controller: " + messageHash);
					
					AmqpClient.sendHashTable(messageHash, ConfigData.epid, "REG");
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} 
		}); 
		ik.openAny(); 
		System.out.println("waiting for InterfaceKit attachment..."); 
		ik.waitForAttachment(); 
		System.out.println(ik.getDeviceName()); 
		System.in.read(); 
		ik.close(); 
		ik = null; 
		System.out.println(" ok"); 
		 
	} 
	
	public static final void relayOff() throws PhidgetException {
		
		ik.setOutputState(6, false);
		System.out.println("Status von Digital 6: " + ik.getOutputState(6));	
	
}

public static final void relayOn() throws PhidgetException {
	
	ik.setOutputState(6, true);
	System.out.println("Status von Digital 6: " + ik.getOutputState(6));	

}
	
}