package com.velisphere.tigerspice.client.helper;

import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.MontanaStatsData;

@RemoteServiceRelativePath("voltHelper")
public interface HelperService extends RemoteService {
	MontanaStatsData getMontanaStats();
	String autoConfMontana();
}
