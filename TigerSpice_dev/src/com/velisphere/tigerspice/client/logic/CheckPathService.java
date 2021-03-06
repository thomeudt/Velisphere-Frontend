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
package com.velisphere.tigerspice.client.logic;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.CheckDataUNUSED;
import com.velisphere.tigerspice.shared.CheckPathDataUNUSED;
import com.velisphere.tigerspice.shared.CheckPathObjectDataUNUSED;
import com.velisphere.tigerspice.shared.CheckPathObjectTreeUNUSED;
import com.velisphere.tigerspice.shared.LinkedPair;
import com.velisphere.tigerspice.shared.SerializableLogicConnector;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;
import com.velisphere.tigerspice.shared.SerializableLogicLogicCheck;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;


@RemoteServiceRelativePath("voltCheckPath")
public interface CheckPathService extends RemoteService {
		
		String addNewCheckpath(String checkpathName, String userID);
		String addNewCheckpath(String checkpathName, String userID, String checkpathID);
		String addNewCheckpath(String checkpathName, String userID, String checkpathID, String uiObject);
		String updateCheckpath(String checkpathId, String uiObject);
		String addNewUiObject(CheckPathObjectDataUNUSED uiObject);
		String createJsonCheckpath(CheckPathObjectTreeUNUSED uiObject);
		String createJsonFromPhysical(SerializableLogicPhysicalItem object);
		String createJsonFromLogical(SerializableLogicLogicCheck object);
		String createJsonFromConnector(SerializableLogicConnector object);
		String createJsonFromContainer(SerializableLogicContainer object);
		SerializableLogicContainer loadJsonToContainer(String checkpathName);
		CheckPathObjectTreeUNUSED getUiObjectJSONForCheckpathID(String checkpathID);
		String addNewMulticheck(String checkId, String operator, String multicheckName, String checkpathID, LinkedList<ActionObject> actions);
		String addNewMulticheckCheckLink(String linkId, String multiCheckId, String checkId, String checkPathId);
		String addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId, String checkPathId);
		LinkedHashMap<String, String> getAllCheckpaths(String userID);
		CheckPathDataUNUSED getCheckpathDetails(String checkpathId);
		String updateCheckpathName(String checkpathId, String checkpathName);
		String updateMulticheck(String multicheckID, String multicheckOperator, String multicheckName,String checkpathID, LinkedList<ActionObject> actions);
		String deleteMulticheckCheckLink(String linkID);
		String deleteMulticheckMulticheckLink(String parentMulticheckID);
		String deleteMulticheck(String multicheckID);
		String deleteCheckpath(String checkpathID);
		String addNewCheckpathCheckLink(String checkpathID, String checkID);
		String addNewCheckpathMulticheckLink(String checkpathID, String multicheckID);
		LinkedList<ActionObject> getActionsForMulticheckID(String multicheckID, String checkpathID);
		String deleteAllActionsForMulticheckId(String multicheckID);
}
