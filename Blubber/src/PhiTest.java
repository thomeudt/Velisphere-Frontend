import com.phidgets.*; 
import com.phidgets.event.*; 
public class PhiTest 
{ 
	public static InterfaceKitPhidget ik;
	
	public static final void main(String args[]) throws Exception { 
		 
		
		// Start AMQP Listener
		ServerParameters.my_queue_name = "phi";
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

				if (se.getIndex() == 6){				
					System.out.println("Gesendet " + message);
					try {
						System.out.println("Status von Digital 6: " + ik.getOutputState(6));

					} catch (PhidgetException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}

				try {
					Send.main("PONG:" + message, "adam");
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