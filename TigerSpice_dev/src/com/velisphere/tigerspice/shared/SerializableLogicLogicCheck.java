package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.velisphere.tigerspice.client.logic.draggables.LinkCreator;

public class SerializableLogicLogicCheck implements IsSerializable {

	String content;
	int sourceCount;
	int xPos;
	int yPos;
	String id;
	boolean or;
	boolean and;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getSourceCount() {
		return sourceCount;
	}
	public void setSourceCount(int sourceCount) {
		this.sourceCount = sourceCount;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public boolean isOr() {
		return or;
	}
	public void setOr(boolean or) {
		this.or = or;
	}
	public boolean isAnd() {
		return and;
	}
	public void setAnd(boolean and) {
		this.and = and;
	}
	
	
	
}
