package com.velisphere.tigerspice.client.admin.vendor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ManageVendor extends Composite {

	private static ManageVendorUiBinder uiBinder = GWT
			.create(ManageVendorUiBinder.class);

	interface ManageVendorUiBinder extends UiBinder<Widget, ManageVendor> {
	}

	public ManageVendor() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
