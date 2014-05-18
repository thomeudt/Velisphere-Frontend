package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MontanaStatsData implements IsSerializable {

	public long duration;
	public double averageLatency;
	public String hostname;
	public long invocationErrors;
	public long invocationsCompleted;
	public long readThroughput;
	public long writeThroughput;
	
	
	
}
