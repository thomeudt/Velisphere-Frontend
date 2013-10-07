package com.velisphere.tigerspice.client;


import java.util.HashSet;






import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.UserData;

@RemoteServiceRelativePath("voltUser")
public interface UserService extends RemoteService {
	HashSet<UserData> getAllUserDetails();
}


