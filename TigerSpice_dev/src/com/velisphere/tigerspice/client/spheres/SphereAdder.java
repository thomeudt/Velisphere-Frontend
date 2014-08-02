package com.velisphere.tigerspice.client.spheres;

import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;

public class SphereAdder extends PopupPanel {

	
	@UiField TextBox txtSphereName;
	
	private static SphereAdderUiBinder uiBinder = GWT
			.create(SphereAdderUiBinder.class);

	interface SphereAdderUiBinder extends UiBinder<Widget, SphereAdder> {
	}

	public SphereAdder() {
		add(uiBinder.createAndBindUi(this));
	}

	
	@UiHandler("btnCreate")
	void createSphere(ClickEvent event){
		
		final SphereServiceAsync sphereService = GWT
				.create(SphereService.class);

		
		String sphereName = new String("");
		sphereName = txtSphereName.getText();
				
		
		sphereService.addSphere(sphereName, 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
						
					}

					@Override
					public void onSuccess(String result) {						
						AppController.openEndpointManager();
						closePopup();
					}
				
		});
	}
  
  
	void closePopup(){
		  this.removeFromParent();
	  }
	  
}
