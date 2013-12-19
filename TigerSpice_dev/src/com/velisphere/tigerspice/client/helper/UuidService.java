package com.velisphere.tigerspice.client.helper;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.EndpointData;


@RemoteServiceRelativePath("voltUuid")
public interface UuidService extends RemoteService {
		String getUuid();
		

}
