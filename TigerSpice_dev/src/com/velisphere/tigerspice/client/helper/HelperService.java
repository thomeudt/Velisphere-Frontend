package com.velisphere.tigerspice.client.helper;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.MontanaStatsData;

@RemoteServiceRelativePath("voltHelper")
public interface HelperService extends RemoteService {
	MontanaStatsData getMontanaStats();
	String autoConfMontana();
}
