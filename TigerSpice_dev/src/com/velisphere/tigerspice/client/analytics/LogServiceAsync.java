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
package com.velisphere.tigerspice.client.analytics;

import java.util.HashSet;
import java.util.LinkedList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.LogData;
import com.velisphere.tigerspice.shared.UserData;

public interface LogServiceAsync {
	void getAllLogEntries(AsyncCallback<HashSet<LogData>> callback);
	void getLogCount(AsyncCallback<LinkedList<String>> callback);
}
