package com.velisphere.tigerspice.shared;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.velisphere.tigerspice.client.logic.draggables.LogicCheck;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class SerializableLogicConnector implements IsSerializable {
	
	
	String txtManualEntryContent;	
	String txtCheckValueContent;
	int type;
	String lbxSourceValue;
	String lbxValueFromSensorValue;
	String lbxTypicalValuesValue;
	String lbxOperatorValue;
	String leftID;
	String rightID;
	String actionID;
	String checkID;
	

	
	
	public String getCheckID() {
		return checkID;
	}
	public void setCheckID(String checkID) {
		this.checkID = checkID;
	}
	public String getActionID() {
		return actionID;
	}
	public void setActionID(String actionID) {
		this.actionID = actionID;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}

		
	public String getTxtManualEntryContent() {
		return txtManualEntryContent;
	}
	public void setTxtManualEntryContent(String txtManualEntryContent) {
		this.txtManualEntryContent = txtManualEntryContent;
	}
	public String getTxtCheckValueContent() {
		return txtCheckValueContent;
	}
	public void setTxtCheckValueContent(String txtCheckValueContent) {
		this.txtCheckValueContent = txtCheckValueContent;
	}
	public String getLbxSourceValue() {
		return lbxSourceValue;
	}
	public void setLbxSourceValue(String lbxSourceValue) {
		this.lbxSourceValue = lbxSourceValue;
	}
	public String getLbxValueFromSensorValue() {
		return lbxValueFromSensorValue;
	}
	public void setLbxValueFromSensorValue(String lbxValueFromSensorValue) {
		this.lbxValueFromSensorValue = lbxValueFromSensorValue;
	}
	public String getLbxTypicalValuesValue() {
		return lbxTypicalValuesValue;
	}
	public void setLbxTypicalValuesValue(String lbxTypicalValuesValue) {
		this.lbxTypicalValuesValue = lbxTypicalValuesValue;
	}
	public String getLbxOperatorValue() {
		return lbxOperatorValue;
	}
	public void setLbxOperatorValue(String lbxOperatorValue) {
		this.lbxOperatorValue = lbxOperatorValue;
	}
	
	public String getLeftID() {
		return leftID;
	}
	public void setLeftID(String leftID) {
		this.leftID = leftID;
	}
	public String getRightID() {
		return rightID;
	}
	public void setRightID(String rightID) {
		this.rightID = rightID;
	}
	
	
	
		
}
