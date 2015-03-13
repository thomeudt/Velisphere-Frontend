package com.velisphere.fs;

import com.velisphere.fs.sdk.CTLListener;

class CTLEventResponder implements CTLListener {
   
	@Override
	public void isAliveRequested() {
		// TODO Auto-generated method stub
		System.out.println("IsAlive Requested...");
	}

	@Override
	public void allPropertiesRequested() {
		// TODO Auto-generated method stub
		
	}
}