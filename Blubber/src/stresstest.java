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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class stresstest {

	public static void main(String[] args) throws Exception {

		ServerParameters.my_queue_name = "1003";
		
		int numworkers = 48;
		
		ExecutorService B52 = Executors.newFixedThreadPool(1000);
		Bombarder[] bombThread = new Bombarder[numworkers];
		for (int i = 0; i < numworkers; i++) {
        
			bombThread[i] = new Bombarder(i);
			B52.execute(bombThread[i]);
            
        }
		
		
	 	B52.shutdown();
		
	}

}
