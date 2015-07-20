/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
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
package com.velisphere.tigerspice.server;

import java.io.IOException;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.voltdb.VoltTable;
import org.voltdb.client.ClientResponse;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.velisphere.tigerspice.client.amqp.AMQPService;
import com.velisphere.tigerspice.client.favorites.FavoriteService;
import com.velisphere.tigerspice.shared.AlertData;
import com.velisphere.tigerspice.shared.FavoriteData;


@SuppressWarnings("serial")
public class FavoriteServiceImpl extends RemoteServiceServlet implements
		FavoriteService {

	@Override
	public String addFavorite(FavoriteData favoriteData) {
		
		// first add to VoltDB

		VoltConnector voltCon = new VoltConnector();
		String favID = UUID.randomUUID().toString();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			voltCon.montanaClient.callProcedure("FAVORITES.insert", favID,
					favoriteData.getUserId(), favoriteData.getName(), favoriteData.getType(), favoriteData.getTargetId());
		} catch (NoConnectionsException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProcCallException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ("OK");
	}

	@Override
	public LinkedList<FavoriteData> getAllFavoritesForUser(String userID)

	{
		VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		LinkedList<FavoriteData> allFavorties = new LinkedList<FavoriteData>();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
				       "SELECT * FROM FAVORITES " +
				       "WHERE USERID='" + userID+"' ORDER BY NAME ASC").getResults();
			
			

			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					FavoriteData favorite = new FavoriteData();
					favorite.setId(result.getString("FAVORITEID"));
					favorite.setName(result.getString("NAME"));
					favorite.setType(result.getString("TARGETTYPE"));
					favorite.setUserId(result.getString("USERID"));
					favorite.setTargetId(result.getString("TARGETID"));
					allFavorties.add(favorite);
					
				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allFavorties;
	}
	
	
	@Override
	public FavoriteData getFavoriteForFavoriteID(String favoriteID)

	{
		VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 FavoriteData favorite = new FavoriteData();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
				       "SELECT * FROM FAVORITES " +
				       "WHERE FAVORITEID='" + favoriteID+"'").getResults();
			
			
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					favorite.setId(result.getString("FAVORITEID"));
					favorite.setName(result.getString("NAME"));
					favorite.setType(result.getString("TARGETTYPE"));
					favorite.setUserId(result.getString("USERID"));
					favorite.setTargetId(result.getString("TARGETID"));
					
					
				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return favorite;
	}
	

	@Override
	public FavoriteData getFavoriteForTargetID(String targetID)

	{
		VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 FavoriteData favorite = new FavoriteData();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("@AdHoc",
				       "SELECT * FROM FAVORITES " +
				       "WHERE TARGETID='" + targetID+"'").getResults();
			
			
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					favorite.setId(result.getString("FAVORITEID"));
					favorite.setName(result.getString("NAME"));
					favorite.setType(result.getString("TARGETTYPE"));
					favorite.setUserId(result.getString("USERID"));
					favorite.setTargetId(result.getString("TARGETID"));
					
					
				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return favorite;
	}
	
	
	@Override
	public String deleteFavoriteForFavoriteID(String favoriteID)

	{
		VoltConnector voltCon = new VoltConnector();

		try {
			voltCon.openDatabase();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		 FavoriteData favorite = new FavoriteData();
		try {

			
			 VoltTable[] results = voltCon.montanaClient.callProcedure("FAVORITES.delete",
						favoriteID).getResults();
			
			
			
			VoltTable result = results[0];
			// check if any rows have been returned

			while (result.advanceRow()) {
				{
					// extract the value in column checkid
					
					
					
				}
			}

			// System.out.println(allEndPoints);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			voltCon.closeDatabase();
		} catch (IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "OK";
	}

	
}
