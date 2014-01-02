package com.velisphere.tigerspice.client.rules;

import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.CheckPathObjectData;


@RemoteServiceRelativePath("voltCheckPath")
public interface CheckPathService extends RemoteService {
		
		String addNewUiObject(CheckPathObjectData uiObject);
		String createJsonCheckpath(LinkedList<LinkedList<CheckPathObjectData>> uiObject);
		String addNewMulticheck(String checkId, String operator, String multicheckName);
		String addNewMulticheckCheckLink(String multiCheckId, String checkId);
		String addNewMulticheckMulticheckLink(String multicheckLId, String multicheckRId);
}
