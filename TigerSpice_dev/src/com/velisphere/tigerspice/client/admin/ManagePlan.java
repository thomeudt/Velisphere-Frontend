package com.velisphere.tigerspice.client.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ManagePlan extends Composite {

	private static ManagePlanUiBinder uiBinder = GWT
			.create(ManagePlanUiBinder.class);

	interface ManagePlanUiBinder extends UiBinder<Widget, ManagePlan> {
	}

	public ManagePlan() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
