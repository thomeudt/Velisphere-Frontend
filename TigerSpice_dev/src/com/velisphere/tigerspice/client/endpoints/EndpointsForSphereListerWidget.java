
package com.velisphere.tigerspice.client.endpoints;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.shared.EndpointData;

 

public class EndpointsForSphereListerWidget extends Composite {
	
	private EndpointServiceAsync rpcService;
	
	
	VerticalPanel verticalPanel = new VerticalPanel();;
	String sphereID;

	
	
	public EndpointsForSphereListerWidget() {

		
		
		rpcService = GWT.create(EndpointService.class);
		refreshEndpoints(this.sphereID);
		initWidget(verticalPanel);
		
		
			
	}


	public void refreshEndpoints(String sphereID){
		
		final AnimationLoading animationLoading = new  AnimationLoading();
		showLoadAnimation(animationLoading);
		rpcService.getEndpointsForSphere(sphereID, new AsyncCallback<Vector<EndpointData>>() {
			
			// There's been a failure in the RPC call
			// Normally you would handle that in a good way,
			// here we just throw up an alert.
			
			
			
			public void onFailure(Throwable caught) {
				Window.alert("Error" + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<EndpointData> result) {
				
								
				Iterator<EndpointData> it = result.iterator();
				removeLoadAnimation(animationLoading);	
				
				while (it.hasNext()){

					final EndpointData currentItem = it.next();
					final DynamicAnchor ancToEndpoint = new DynamicAnchor(currentItem.endpointName + " (...)", false, currentItem.endpointId);
					verticalPanel.add(ancToEndpoint);
					ancToEndpoint.addClickHandler(
							new ClickHandler(){
								public void onClick(ClickEvent event){
								// Label label = new Label();
								// label.setText(ancToEndpoint.getStringQueryFirst());
								// verticalPanel.add(label);
									Window.alert("Clicker");
								}
							});
				}
				
				
			}

			// We've successfully for the data from the RPC call,
			// Now we update the row data with that result starting
			// at a particular row in the cell widget (usually the range start)

		});
		
		
	}
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}
	
	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null) animationLoading.removeFromParent();
	}
	

	

	
	
}
