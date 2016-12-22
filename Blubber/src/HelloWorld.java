/* 
 * Phidget Hello World Program for all devices
 * (c) Phidgets 2012
 */

import com.phidgets.*;
import com.phidgets.event.*;

public class HelloWorld {


    // ========= General Java Functions =============
    public static void printError(int number, String description) {
        System.out.println("Error Event: " + Integer.toString(number) + " - "  + description); 
    }

    public static final void main(String args[]) throws Exception {

        // ========== Event Handling Functions ==========

        AttachListener attachHandler;
        attachHandler = new AttachListener() {
            public void attached(AttachEvent event) {

                int serialNumber = 0;
                String name = new String();

                try {
                    serialNumber = ((Phidget)event.getSource()).getSerialNumber();
                    name = ((Phidget)event.getSource()).getDeviceName();
                } catch (PhidgetException exception) { 
                    printError(exception.getErrorNumber(), exception.getDescription());
                }

                System.out.println("Hello Device " + name + ", Serial Number: " + Integer.toString(serialNumber));
            }
        };
            
        DetachListener detachHandler;
        detachHandler = new DetachListener() {
            public void detached(DetachEvent event) {

                int serialNumber = 0;
                String name = new String();

                try {
                    serialNumber = ((Phidget)event.getSource()).getSerialNumber();
                    name = ((Phidget)event.getSource()).getDeviceName();
                } catch (PhidgetException exception) {
                    printError(exception.getErrorNumber(), exception.getDescription());
                }

                System.out.println("Goodbye Device " + name + ", Serial Number: " + Integer.toString(serialNumber));

            }
        };

        // No Error listener in Manager class, but it does exist for Phidget device APIs

        // =========== Main Code ==========

        Manager manager;
        // No exception thrown on create
        manager = new Manager();

        manager.addAttachListener(attachHandler);
        manager.addDetachListener(detachHandler);

        System.out.println("Opening....");
        try {
            manager.open();
        } catch (PhidgetException exception) {
            printError(exception.getErrorNumber(), exception.getDescription());
        }

        System.out.println("Phidget Simple Playground (plug and unplug devices)\n");
        System.out.println("Press Enter to end anytime...\n");
        System.in.read();

        System.out.println("Closing....");
        try {
            manager.close();
        } catch (PhidgetException exception) {
            printError(exception.getErrorNumber(), exception.getDescription());
        }
        manager = null;
    }
}

