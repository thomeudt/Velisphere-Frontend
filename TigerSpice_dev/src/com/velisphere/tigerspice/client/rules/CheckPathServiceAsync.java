package com.velisphere.tigerspice.client.rules;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.CheckPathObjectData;

public interface CheckPathServiceAsync {
	
	void addNewCheckPath(CheckPathObjectData uiObject, AsyncCallback<String> callback );
		
}
