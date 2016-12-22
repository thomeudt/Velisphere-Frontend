package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TableRowData implements IsSerializable {

	String[] cols;
	

	public void setRow(String[] cols) {
		this.cols = cols;
		
	}

	public String[] getCols() {
		return this.cols;
	}

	
}
