package com.velisphere.tigerspice.client.admin;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ManageProperty extends Composite {

	private static ManagePropertyUiBinder uiBinder = GWT
			.create(ManagePropertyUiBinder.class);

	interface ManagePropertyUiBinder extends UiBinder<Widget, ManageProperty> {
	}

	
	
	public ManageProperty() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
