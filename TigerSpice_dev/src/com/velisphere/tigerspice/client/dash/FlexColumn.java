package com.velisphere.tigerspice.client.dash;

import com.github.gwtbootstrap.client.ui.Column;

public class FlexColumn extends Column{

	private int columnNumber;
	
	public FlexColumn(int size) {
		super(size);
		
	}
	
	public FlexColumn(int size, int columnNumber) {
		super(size);
		this.setColumnNumber(columnNumber);
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	
	
	

}
