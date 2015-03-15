package com.velisphere.fs;

import java.io.IOException;

import com.velisphere.fs.sdk.CTLListener;

class CTLEventResponder implements CTLListener {
   
	@Override
	public void isAliveRequested() {

		System.out.println("IsAlive Requested...");
	}

	@Override
	public void allPropertiesRequested() {

		System.out.println("AllProperties Requested, but not supported by VeliFS");
		
		
	}
}