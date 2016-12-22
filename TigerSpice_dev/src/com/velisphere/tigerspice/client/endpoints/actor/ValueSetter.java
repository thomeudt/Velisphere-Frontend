package com.velisphere.tigerspice.client.endpoints.actor;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.amqp.AMQPService;
import com.velisphere.tigerspice.client.amqp.AMQPServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.spheres.SphereAdder;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.client.spheres.SphereServiceAsync;


public class ValueSetter extends PopupPanel  {


	@UiField TextBox txtNewValue;
	String propertyID;
	String endpointID;
	
	private static ValueSetterUiBinder uiBinder = GWT
			.create(ValueSetterUiBinder.class);

	interface ValueSetterUiBinder extends UiBinder<Widget, ValueSetter> {
	}

	
	public ValueSetter(String endpointID, String propertyID) {
		this.endpointID = endpointID;
		this.propertyID = propertyID;
		add(uiBinder.createAndBindUi(this));
	}

		
	
	@UiHandler("btnSubmit")
	void createSphere(ClickEvent event){
		
		AMQPServiceAsync amqpService = GWT
				.create(AMQPService.class);

		amqpService.sendRegMessage(endpointID, propertyID, txtNewValue.getText(), new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						
						closePopup();
						
						
						
					}

				});
		
		
	}
  
  
	void closePopup(){
		  this.removeFromParent();
	  }
	  
}
