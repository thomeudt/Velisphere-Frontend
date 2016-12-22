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
package com.velisphere.chai.messageUtils;

import com.velisphere.chai.LdapGet;
import com.velisphere.chai.broker.Send;

public class MessageAdm {


	/*
	 * This class contains all methods used for sending administrative messages to a defined target queue
	 * 
	 */

	public void provideLDAP()
	{

		/*
		 * To be filled with more generic LDAP message submission code
		 * 		
		 */

	}

	public static void provideLdapMobiles(String targetQueue) throws 
	Exception


	/*
	 *  This method is used to provide all mobile phone numbers in the LDAP directory to the queue of the entity requesting it.
	 */

	{
		LdapGet ldapMobiles = new LdapGet();
		String ldapDir = ldapMobiles.getLdapFull();
		Send.sendJson("*ADM*"+ldapDir, targetQueue);		

	}

}
