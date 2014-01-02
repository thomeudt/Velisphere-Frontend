package com.velisphere.tigerspice.client.rules;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.CheckPathObjectData;
import com.velisphere.tigerspice.shared.CheckPathObjectTree;


@RemoteServiceRelativePath("voltCheckPath")
public interface CheckPathService extends RemoteService {
		
		String addNewCheckpath();
		String updateCheckpath(String checkpathId, String uiObject);
		String addNewUiObject(CheckPathObjectData uiObject);
		String createJsonCheckpath(CheckPathObjectTree uiObject);
		CheckPathObjectTree getUiObjectJSONForCheckpathID(String checkpathID);
		String addNewMulticheck(String checkId, String operator, String multicheckName);
		String addNewMulticheckCheckLink(String multiCheckId, String checkId);
		String addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId);
}
