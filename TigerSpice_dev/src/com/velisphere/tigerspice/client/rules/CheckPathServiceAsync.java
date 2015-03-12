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
package com.velisphere.tigerspice.client.rules;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.CheckPathData;
import com.velisphere.tigerspice.shared.CheckPathObjectData;
import com.velisphere.tigerspice.shared.CheckPathObjectTree;
import com.velisphere.tigerspice.shared.LinkedPair;
import com.velisphere.tigerspice.shared.SerializableLogicConnector;
import com.velisphere.tigerspice.shared.SerializableLogicContainer;
import com.velisphere.tigerspice.shared.SerializableLogicLogicCheck;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;

public interface CheckPathServiceAsync {
	
	void addNewUiObject(CheckPathObjectData uiObject, AsyncCallback<String> callback );
	void updateCheckpath(String checkpathId, String uiObject, AsyncCallback<String> callback );
	void addNewCheckpath(String checkpathName, String userID, AsyncCallback<String> callback);
	void addNewCheckpath(String checkpathName, String userID, String checkpathID, AsyncCallback<String> callback);
	void addNewCheckpath(String checkpathName, String userID, String checkpathID, String uiObject, AsyncCallback<String> callback);
	void getUiObjectJSONForCheckpathID(String checkpathID, AsyncCallback<CheckPathObjectTree> callback);
	void createJsonCheckpath(CheckPathObjectTree uiObject, AsyncCallback<String> callback );
	void createJsonFromPhysical(SerializableLogicPhysicalItem object, AsyncCallback<String> callback );
	void createJsonFromLogical(SerializableLogicLogicCheck object, AsyncCallback<String> callback );
	void createJsonFromConnector(SerializableLogicConnector object, AsyncCallback<String> callback );
	void createJsonFromContainer(SerializableLogicContainer object, AsyncCallback<String> callback );
	void loadJsonToContainer(String checkpathName, AsyncCallback<SerializableLogicContainer> callback );
	void addNewMulticheck(String checkId, String operator, String multicheckName, String checkpathID, LinkedList<ActionObject> actions, AsyncCallback<String> callback );
	void addNewMulticheckCheckLink(String linkId, String multiCheckId, String checkId, String checkPathId, AsyncCallback<String> callback );
	void addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId, String checkPathId, AsyncCallback<String> callback );
	void getAllCheckpaths(String userID, AsyncCallback<LinkedHashMap<String, String>> callback);
	void getCheckpathDetails(String checkpathId, AsyncCallback<CheckPathData> callback);
	void updateCheckpathName(String checkpathId, String checkpathName, AsyncCallback<String> callback );
	void updateMulticheck(String multicheckID,  String multicheckOperator, String multicheckName, String checkpathID, LinkedList<ActionObject> actions, AsyncCallback<String> callback);
	void deleteMulticheckCheckLink(String parentMulticheckID, AsyncCallback<String> callback);
	void deleteMulticheckMulticheckLink(String parentMulticheckID, AsyncCallback<String> callback);
	void deleteMulticheck(String multicheckID, AsyncCallback<String> callback);
	void deleteCheckpath(String checkpathID, AsyncCallback<String> callback);
	void addNewCheckpathCheckLink(String checkpathID, String checkID, AsyncCallback<String> callback);
	void addNewCheckpathMulticheckLink(String checkpathID, String multicheckID, AsyncCallback<String> callback);
	void getActionsForMulticheckID(String multicheckID, String checkpathID, AsyncCallback<LinkedList<ActionObject>> callback);
	void deleteAllActionsForMulticheckId(String multicheckID, AsyncCallback<String> callback );
}
