package com.velisphere.tigerspice.client;

import java.util.HashSet;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.UserData;

public interface UserServiceAsync {
	void getAllUserDetails(AsyncCallback<HashSet<UserData>> callback);
}
