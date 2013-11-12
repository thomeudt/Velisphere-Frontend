package com.velisphere.tigerspice.client.checks;


import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckNewDialogBox extends PopupPanel {

	@UiField ListBox lstPropertyID; 
	
	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, CheckNewDialogBox> {
	}

	public CheckNewDialogBox(String endpointID, String propertyID, String propertyClassID) {
		setWidget(uiBinder.createAndBindUi(this));
		
		lstPropertyID.addItem(propertyID, propertyID);
		
	}

}
