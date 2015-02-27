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
	int lbxSourceIndex;
	int lbxValueFromSensorIndex;
	int lbxTypicalValuesIndex;
	int lbxOperatorIndex;
	String leftID;
	String rightID;
	

	
	
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
	public int getLbxSourceIndex() {
		return lbxSourceIndex;
	}
	public void setLbxSourceIndex(int lbxSourceIndex) {
		this.lbxSourceIndex = lbxSourceIndex;
	}
	public int getLbxValueFromSensorIndex() {
		return lbxValueFromSensorIndex;
	}
	public void setLbxValueFromSensorIndex(int lbxValueFromSensorIndex) {
		this.lbxValueFromSensorIndex = lbxValueFromSensorIndex;
	}
	public int getLbxTypicalValuesIndex() {
		return lbxTypicalValuesIndex;
	}
	public void setLbxTypicalValuesIndex(int lbxTypicalValuesIndex) {
		this.lbxTypicalValuesIndex = lbxTypicalValuesIndex;
	}
	public int getLbxOperatorIndex() {
		return lbxOperatorIndex;
	}
	public void setLbxOperatorIndex(int lbxOperatorIndex) {
		this.lbxOperatorIndex = lbxOperatorIndex;
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
