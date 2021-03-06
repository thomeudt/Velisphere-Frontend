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
package com.velisphere.tigerspice.client.favorites;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.FavoriteData;


@RemoteServiceRelativePath("voltFavorite")
public interface FavoriteService extends RemoteService {
		
	String addFavorite(FavoriteData favoriteData);
	LinkedList<FavoriteData> getAllFavoritesForUser(String userID);
	FavoriteData getFavoriteForFavoriteID(String favoriteID);
	FavoriteData getFavoriteForTargetID(String targetID);
	String deleteFavoriteForFavoriteID(String favoriteID);
}
