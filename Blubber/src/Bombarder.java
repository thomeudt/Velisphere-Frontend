import java.util.HashMap;

import com.velisphere.milk.amqpClient.AmqpClient;


/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
public class Bombarder implements Runnable {

	
	 private int workerNumber;

	    Bombarder(int number) {
	        workerNumber = number;
	    }
	public void run() {
		int i = 0;
		
		java.util.Random random = new java.util.Random();
		
		while (i<1000) {
			try {
				HashMap<String, String> messageHash = new HashMap<String, String>();
				Integer rndNum = random.nextInt();
				//messageHash.put("PR1", rndNum.toString());
				messageHash.put("PR6", "1");
				//messageHash.put("PR2", "1");
	        	//messageHash.put("PR3", "1");
	        	//messageHash.put("PR4", "1");
	        	//messageHash.put("PR5", "1");
	        	
	        	AmqpClient.sendHashTable(messageHash, "controller", "REG");
	        	//Send.sendHashTable(messageHash, "controller");

	        	
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
		}
		
		System.out.println("Done!");
	}

}
