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

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.FavoriteData;


public interface FavoriteServiceAsync {

	void addFavorite(FavoriteData favoriteData, AsyncCallback<String> callback);
	void getAllFavoritesForUser(String userID, AsyncCallback<LinkedList<FavoriteData>> callback);
	void getFavoriteForFavoriteID(String favoriteID, AsyncCallback<FavoriteData> callback);
	void getFavoriteForTargetID(String targetID, AsyncCallback<FavoriteData> callback);
	void deleteFavoriteForFavoriteID(String favoriteID, AsyncCallback<String> callback);
}
