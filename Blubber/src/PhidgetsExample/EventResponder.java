package PhidgetsExample;

import java.io.IOException;
import java.util.HashMap;

import com.phidgets.PhidgetException;
import com.velisphere.fs.sdk.CTLListener;
import com.velisphere.fs.sdk.Server;


public class EventResponder implements EventListener {
   
	@Override
	public void isAliveRequested() {

		System.out.println("IsAlive Requested...");
		
		HashMap<String, String> messageHash = new HashMap<String, String>();
		messageHash.put("setState", "REACHABLE");
		
		try {
			Server.sendHashTable(messageHash, ServerParameters.my_queue_name, "CTL");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void allPropertiesRequested() {

		System.out.println("AllProperties Requested, but not supported by Blubber");
		
		
	}

	@Override
	public void newInboundMessage(String message) {
		// TODO Auto-generated method stub
		
	    if (message.contains("lightsoff")) {
	    	try {
				PhiTestAdvanced.relayOn();
			} catch (PhidgetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 
	    if (message.contains("lightson")) {
	    	try {
				PhiTestAdvanced.relayOff();
			} catch (PhidgetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 

		
		
		System.out.println("Do nothing on incoming, as no incoming commands are supported yet.");
		
	}
}