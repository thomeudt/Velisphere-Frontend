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
package com.velisphere.chai;

public class ServerParameters {

	public static String bunny_ip;
	public static String DestinationQueue = "";
	public static String ldapUrl;
	public static String ldapPrincipal;
	public static String ldapPassword;
	public static String controllerQueueName;
	public static Integer threadpoolSize;
	public static String msgTypeAdministrativeRequest = "*ADM*";
    //public static String volt_ip = "16.1.1.149"; // for local db
	public static String volt_ip = "ec2-54-213-184-238.us-west-2.compute.amazonaws.com"; // for aws db
	// public static String volt_ip = "ec2-54-200-208-195.us-west-2.compute.amazonaws.com"; // for strato db
	public static String vertica_ip = "127.0.0.1";
}
