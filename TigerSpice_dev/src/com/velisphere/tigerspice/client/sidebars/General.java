package com.velisphere.tigerspice.client.sidebars;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;

public class General extends Composite {

	@UiField Anchor ancProvisioning;

	
	private static GeneralUiBinder uiBinder = GWT.create(GeneralUiBinder.class);

	interface GeneralUiBinder extends UiBinder<Widget, General> {
	}

	public General() {
		initWidget(uiBinder.createAndBindUi(this));
		ancProvisioning.setHref("#");
	}
	
	@UiHandler("ancProvisioning")
	void openProvisioning (ClickEvent event) {
	
		AppController.openProvisioningWizard();
		
		
	}

}
