package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;
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
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.users.NewAccountDialogbox;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.SphereData;

 

public class SphereListerWidget extends Composite {
	
	private SphereServiceAsync rpcService;
	
	AnimationLoading animationLoading;
	VerticalPanel verticalPanel = new VerticalPanel();;
	

	
	
	public SphereListerWidget() {

		rpcService = GWT.create(SphereService.class);
		
		
		refreshPrivateSpheres();
		initWidget(verticalPanel);
	
		
		
	}


	private void refreshPrivateSpheres(){
		
		showLoadAnimation();
		rpcService.getAllSpheres(new AsyncCallback<Vector<SphereData>>() {
			
			// There's been a failure in the RPC call
			// Normally you would handle that in a good way,
			// here we just throw up an alert.
			
			
			
			public void onFailure(Throwable caught) {
				Window.alert("Error" + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<SphereData> result) {
				
								
				Iterator<SphereData> it = result.iterator();
					
				
				while (it.hasNext()){

					final SphereData currentItem = it.next();
					final DynamicAnchor ancToEndpoint = new DynamicAnchor(currentItem.sphereName, false, currentItem.sphereId);
					verticalPanel.add(ancToEndpoint);
					ancToEndpoint.addClickHandler(
							new ClickHandler(){
								public void onClick(ClickEvent event){
								// Label label = new Label();
								// label.setText(ancToEndpoint.getStringQueryFirst());
								// verticalPanel.add(label);
									RootPanel.get("main").clear();
									RootPanel.get("main").add(new SphereOverview(currentItem.sphereId, currentItem.sphereName));
								}
							});
				}
				removeLoadAnimation();
				
			}

			// We've successfully for the data from the RPC call,
			// Now we update the row data with that result starting
			// at a particular row in the cell widget (usually the range start)

		});
		
		
	}
	
	private void showLoadAnimation() {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		animationLoading = new AnimationLoading();
		rootPanel.add(animationLoading, 25, 40);
	}
	
	private void removeLoadAnimation() {
		if (animationLoading != null) animationLoading.removeFromParent();
	}
	

	

	
	
}