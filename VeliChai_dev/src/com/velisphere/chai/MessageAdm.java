package com.velisphere.chai;

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
