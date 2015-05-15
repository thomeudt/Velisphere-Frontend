package com.velisphere.fs.sdk;

public interface CTLListener {

	public void isAliveRequested();
	public void allPropertiesRequested();
	public void cnfMessage(String message);
	public void regMessage(String message);
	
}
