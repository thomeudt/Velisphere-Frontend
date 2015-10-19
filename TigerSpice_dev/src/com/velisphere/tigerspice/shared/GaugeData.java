package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class GaugeData implements IsSerializable {


	private String endpointID;
	private String propertyID;
	int gaugeType;
	int[] greenRange;
	int[] yellowRange;
	int[] redRange; 
	double[] minMax;
	
	public String getEndpointID() {
		return endpointID;
	}
	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}
	public String getPropertyID() {
		return propertyID;
	}
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}
	public int getGaugeType() {
		return gaugeType;
	}
	public void setGaugeType(int gaugeType) {
		this.gaugeType = gaugeType;
	}
	public int[] getGreenRange() {
		return greenRange;
	}
	public void setGreenRange(int[] greenRange) {
		this.greenRange = greenRange;
	}
	public int[] getYellowRange() {
		return yellowRange;
	}
	public void setYellowRange(int[] yellowRange) {
		this.yellowRange = yellowRange;
	}
	public int[] getRedRange() {
		return redRange;
	}
	public void setRedRange(int[] redRange) {
		this.redRange = redRange;
	}
	public double[] getMinMax() {
		return minMax;
	}
	public void setMinMax(double[] minMax) {
		this.minMax = minMax;
	}

}
