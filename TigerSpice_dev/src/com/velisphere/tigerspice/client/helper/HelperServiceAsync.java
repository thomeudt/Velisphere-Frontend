package com.velisphere.tigerspice.client.helper;

import java.util.Vector;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.shared.MontanaStatsData;

public interface HelperServiceAsync {
	void getMontanaStats(AsyncCallback<MontanaStatsData> callback);
	void autoConfMontana(AsyncCallback<String> callback);
	
}