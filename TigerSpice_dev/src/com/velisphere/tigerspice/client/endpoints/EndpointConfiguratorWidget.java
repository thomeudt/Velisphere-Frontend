package com.velisphere.tigerspice.client.endpoints;

import java.util.LinkedList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.amqp.AMQPService;
import com.velisphere.tigerspice.client.amqp.AMQPServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.shared.PropertyData;

public class EndpointConfiguratorWidget extends Composite  {

	private static EndpointConfiguratorWidgetUiBinder uiBinder = GWT
			.create(EndpointConfiguratorWidgetUiBinder.class);

	interface EndpointConfiguratorWidgetUiBinder extends
			UiBinder<Widget, EndpointConfiguratorWidget> {
	}

	
	@UiField
	Button btnRefresh;
	String endpointID;

	public EndpointConfiguratorWidget(String endpointID) {
		initWidget(uiBinder.createAndBindUi(this));
		this.endpointID = endpointID;
		btnRefresh.setText("Refresh Data");
	}

	@UiHandler("btnRefresh")
	void onClick(ClickEvent e) {
		AMQPServiceAsync amqpService = GWT
				.create(AMQPService.class);

		amqpService.sendGetAllProperties(endpointID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						AppController.openEndpoint(endpointID);
						
					}

		
		});
	}

	

}

	