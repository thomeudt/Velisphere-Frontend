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
	public static String volt_ip = "ec2-54-200-71-184.us-west-2.compute.amazonaws.com";
}
