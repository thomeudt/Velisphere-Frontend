package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class SerializableLogicConnector implements IsSerializable {
	
	String leftEndpointID;
	String rightEndpointID;
	String leftPropertyID;
	String rightPropertyID;
	int type;
	
	public String getLeftEndpointID() {
		return leftEndpointID;
	}
	public void setLeftEndpointID(String left) {
		this.leftEndpointID = left;
	}
	public String getRightEndpointID() {
		return rightEndpointID;
	}
	public void setRightEndpointID(String right) {
		this.rightEndpointID = right;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

	public String getRightPropertyID() {
		return rightPropertyID;
	}
	public void setRightPropertyID(String rightPropertyID) {
		this.rightPropertyID = rightPropertyID;
	}

	public String getLeftPropertyID() {
		return leftPropertyID;
	}
	public void setLeftPropertyID(String leftPropertyID) {
		this.leftPropertyID = leftPropertyID;
	}
	
		
}
