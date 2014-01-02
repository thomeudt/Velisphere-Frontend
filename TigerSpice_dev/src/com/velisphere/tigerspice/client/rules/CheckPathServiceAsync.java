package com.velisphere.tigerspice.client.rules;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.CheckPathObjectData;

public interface CheckPathServiceAsync {
	
	void addNewUiObject(CheckPathObjectData uiObject, AsyncCallback<String> callback );
	void updateCheckpath(String checkpathId, String uiObject, AsyncCallback<String> callback );
	void addNewCheckpath(AsyncCallback<String> callback);
	void createJsonCheckpath(LinkedList<LinkedList<CheckPathObjectData>> uiObject, AsyncCallback<String> callback );
	void addNewMulticheck(String checkId, String operator, String multicheckName, AsyncCallback<String> callback );
	void addNewMulticheckCheckLink(String multiCheckId, String checkId, AsyncCallback<String> callback );
	void addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId, AsyncCallback<String> callback );
}
