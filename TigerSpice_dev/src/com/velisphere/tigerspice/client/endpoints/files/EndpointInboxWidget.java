package com.velisphere.tigerspice.client.endpoints.files;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.ChartSensorHistoryWidget;

public class EndpointInboxWidget extends Composite {

	private String endpointID;
	
	private static EndpointInboxWidgetUiBinder uiBinder = GWT
			.create(EndpointInboxWidgetUiBinder.class);

	interface EndpointInboxWidgetUiBinder extends
			UiBinder<Widget, EndpointInboxWidget> {
	}


	
	public EndpointInboxWidget(String endpointID) {
		this.endpointID = endpointID;
		
		initWidget(uiBinder.createAndBindUi(this));
	
		
	}

	
	@UiFactory
	FileList makeFileList() { // method name is
													// insignificant
		return new FileList(endpointID);
	}

	
	
	
}
