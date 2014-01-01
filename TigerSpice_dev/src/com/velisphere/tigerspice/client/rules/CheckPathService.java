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
		

}
