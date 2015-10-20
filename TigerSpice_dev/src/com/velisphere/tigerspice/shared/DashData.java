package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DashData implements IsSerializable{


	String dashboardID;
	String userID;
	String json;
	String dashboardName;

	public String getDashboardID() {
		return dashboardID;
	}
	public void setDashboardID(String dashboardID) {
		this.dashboardID = dashboardID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getJson() {
		return json;
	}
	public void setJson(String json) {
		this.json = json;
	}
	public String getDashboardName() {
		return dashboardName;
	}
	public void setDashboardName(String dashboardName) {
		this.dashboardName = dashboardName;
	}

}
