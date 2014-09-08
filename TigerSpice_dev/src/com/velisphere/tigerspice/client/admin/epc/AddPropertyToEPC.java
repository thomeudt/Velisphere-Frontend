package com.velisphere.tigerspice.client.admin.epc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class AddPropertyToEPC extends Composite {

	private static AddPropertyToEPCUiBinder uiBinder = GWT
			.create(AddPropertyToEPCUiBinder.class);

	interface AddPropertyToEPCUiBinder extends
			UiBinder<Widget, AddPropertyToEPC> {
	}

	public AddPropertyToEPC() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	public AddPropertyToEPC(String EPCId) {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
