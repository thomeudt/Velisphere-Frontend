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


import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/* This is a very basic implementation of reading an LDAP directory to
 * provide Chai with a full list of users in the directory and their
 * mobile phone numbers to enable a basic SMS type functionality
 * 
 * Clients can pull the directory data which will then be provided by
 * Chai using the directory queue.
 */


public class LdapGet {



	public String[] getLdapMobiles() {

		// ConfigHandler cfh = new ConfigHandler();
		// cfh.loadParamChangesAsXML();


		Hashtable env = new Hashtable();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ServerParameters.ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ServerParameters.ldapPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, ServerParameters.ldapPassword);

		DirContext ctx = null;
		NamingEnumeration results = null;

		List<String> ldapAllMobiles = new ArrayList<String>();

		try {
			ctx = new InitialDirContext(env);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = ctx.search("", "(objectclass=person)", controls);
			while (results.hasMore()) {
				SearchResult searchResult = (SearchResult) results.next();
				Attributes attributes = searchResult.getAttributes();
				Attribute attrCn = attributes.get("cn");
				String cn = (String) attrCn.get();
				Attribute attrMobile = attributes.get("mobile");
				String mobile = (String) attrMobile.get();
				Attribute attrUid = attributes.get("uid");
				String uid = (String) attrUid.get();

				String lineItem = uid + " " + cn + " " + mobile;

				ldapAllMobiles.add(lineItem);
				System.out.println(" User Common Name = " + cn);
				System.out.println(" User ID = " + attributes.get("uid"));
				System.out.println(" User Cellphone = " + mobile);
				System.out.println(lineItem);

			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (Exception e) {
				}
			}
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception e) {
				}
			}

		}

		String[] returnArray = new String[ ldapAllMobiles.size() ];
		ldapAllMobiles.toArray(returnArray);
		System.out.println(returnArray[0]);
		return returnArray;

	}


	public String getLdapFull() throws JSONException {

		ConfigHandler cfh = new ConfigHandler();
		cfh.loadParamChangesAsXML();


		Hashtable env = new Hashtable();

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, ServerParameters.ldapUrl);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ServerParameters.ldapPrincipal);
		env.put(Context.SECURITY_CREDENTIALS, ServerParameters.ldapPassword);

		DirContext ctx = null;
		NamingEnumeration results = null;

		// JSONObject ldapAllLines = new JSONObject();
		JSONStringer ldapAllLines = new JSONStringer();
		ldapAllLines.object();

		try {
			ctx = new InitialDirContext(env);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = ctx.search("", "(objectclass=person)", controls);
			while (results.hasMore()) {
				SearchResult searchResult = (SearchResult) results.next();
				Attributes attributes = searchResult.getAttributes();
				Attribute attrCn = attributes.get("cn");
				Attribute attrMobile = attributes.get("mobile");
				Attribute attrUid = attributes.get("uid");

				JSONObject lineItem = new JSONObject();
				lineItem.put("cn", attrCn.toString());
				lineItem.put("mobile", attrMobile.toString());
				lineItem.put("uid", attrUid.toString());

				System.out.println(lineItem);

				ldapAllLines.key(attrUid.toString());
				ldapAllLines.value(lineItem);

			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			if (results != null) {
				try {
					results.close();
				} catch (Exception e) {
				}
			}
			if (ctx != null) {
				try {
					ctx.close();
				} catch (Exception e) {
				}
			}

		}
		ldapAllLines.endObject();
		return ldapAllLines.toString();

	}




}
