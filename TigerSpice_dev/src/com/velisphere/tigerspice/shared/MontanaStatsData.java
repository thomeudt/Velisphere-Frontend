package com.velisphere.tigerspice.shared;

import java.sql.Timestamp;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MontanaStatsData implements IsSerializable {

	public long timestamp;
	public long hostId;
	public String hostname;
	public long connectionId;
	public String connectionHostname;
	public long bytesRead;
	public long messagesRead;
	public long bytesWritten;
	public long messagesWritten;
	public String IP;

	
	
}
