package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class SerializableLogicConnector implements IsSerializable {
	
	String left;
	String right;
	int type;
	
	public String getLeft() {
		return left;
	}
	public void setLeft(String left) {
		this.left = left;
	}
	public String getRight() {
		return right;
	}
	public void setRight(String right) {
		this.right = right;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	
		
}
