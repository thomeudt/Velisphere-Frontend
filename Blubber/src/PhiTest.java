import java.util.HashMap;

import com.phidgets.*; 
import com.phidgets.event.*; 
public class PhiTest 
{ 
	public static InterfaceKitPhidget ik;
	
	public static final void main(String args[]) throws Exception { 
		 
		
		// Start AMQP Listener
		ServerParameters.my_queue_name = "E1";
		Thread t = new Thread(new RecvPhi(), "listener");
		t.start();
		
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
					
					messageHash.put("PR10", String.valueOf(ik.getSensorValue(6)));
					messageHash.put("PR11", String.valueOf(ik.getSensorValue(7)));
					messageHash.put("PR12", String.valueOf(ik.getSensorValue(5)));
					messageHash.put("PR13", String.valueOf(ik.getSensorValue(1)));
					
					System.out.println("Message Hash Sent to Controller: " + messageHash);
					
					Send.sendHashTable(messageHash, "controller");
					
					
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
		if (false) { 
			System.out.println("wait for finalization..."); 
			System.gc(); 
		} 
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