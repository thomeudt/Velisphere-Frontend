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
package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Label;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointsForSphereListerWidget_unused;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.users.NewAccountWidget;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.SphereData;

 

public class SphereListerWidgetUnused extends Composite {
	
	private SphereServiceAsync rpcService;
	
	AnimationLoading animationLoading;
	VerticalPanel verticalPanel = new VerticalPanel();;
	

	
	
	public SphereListerWidgetUnused() {

		rpcService = GWT.create(SphereService.class);
		
		
		refreshPrivateSpheres();
		initWidget(verticalPanel);
	
		
		
	}


	private void refreshPrivateSpheres(){
		
		final AnimationLoading animation = new AnimationLoading();
		animation.showLoadAnimation("Loading endpoints");
		
		// TODO needs to be changed to show only spheres personal to user
		rpcService.getAllSpheres(new AsyncCallback<LinkedList<SphereData>>() {
			
			// There's been a failure in the RPC call
			// Normally you would handle that in a good way,
			// here we just throw up an alert.
			
			
			
			public void onFailure(Throwable caught) {
				Window.alert("Error" + caught.getMessage());
			}

			@Override
			public void onSuccess(LinkedList<SphereData> result) {
				
								
				Iterator<SphereData> it = result.iterator();
					
				
				while (it.hasNext()){

					final SphereData currentItem = it.next();
					final DynamicAnchor ancToEndpoint = new DynamicAnchor(currentItem.sphereName + " (...)", false, currentItem.sphereId);
					verticalPanel.add(ancToEndpoint);
					ancToEndpoint.addClickHandler(
							new ClickHandler(){
								public void onClick(ClickEvent event){
								// Label label = new Label();
								// label.setText(ancToEndpoint.getStringQueryFirst());
								// verticalPanel.add(label);
									RootPanel.get("main").clear();
								    RootPanel.get("main").add(new SphereView(currentItem.sphereId));
									//RootPanel.get("main").add(new EndpointsForSphereListerWidget(currentItem.sphereId));
								}
							});
				}
				animation.removeFromParent();
				
			}

			// We've successfully for the data from the RPC call,
			// Now we update the row data with that result starting
			// at a particular row in the cell widget (usually the range start)

		});
		
		
	}
	

	

	

	
	
}
