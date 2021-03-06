/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client.endpoints;

import java.util.Iterator;
import java.util.LinkedList;
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

 

public class EndpointsForSphereListerWidget_unused extends Composite {
	
	private EndpointServiceAsync rpcService;
	
	
	VerticalPanel verticalPanel = new VerticalPanel();;
	String sphereID;

	
	
	public EndpointsForSphereListerWidget_unused() {

	
		rpcService = GWT.create(EndpointService.class);
		refreshEndpoints(this.sphereID);
		initWidget(verticalPanel);
		
	}


	public void refreshEndpoints(String sphereID){
		
		final AnimationLoading animationLoading = new  AnimationLoading();
		showLoadAnimation(animationLoading);
		rpcService.getEndpointsForSphere(sphereID, new AsyncCallback<LinkedList<EndpointData>>() {
			
			// There's been a failure in the RPC call
			// Normally you would handle that in a good way,
			// here we just throw up an alert.
			
			
			
			public void onFailure(Throwable caught) {
				Window.alert("Error" + caught.getMessage());
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
				
								
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
