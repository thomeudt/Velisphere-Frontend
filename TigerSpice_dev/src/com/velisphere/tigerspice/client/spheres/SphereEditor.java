package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;
import java.util.LinkedList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.spheres.widgets.DualListBox;
import com.velisphere.tigerspice.shared.EndpointData;

public class SphereEditor extends Composite {
	
	DualListBox dualListBox;
	String sphereID;
	
	public SphereEditor(final String sphereID, final String sphereName) {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();
		this.sphereID = sphereID;
		
		
		
		// this.sphereName = sphereName;
		


		
				
		dualListBox = new DualListBox(10);
	    
		initWidget(dualListBox);

		
		
	    
		
	    
		dualListBox.getDragController();

		refreshSourceEndpoints();
		refreshTargetEndpoints();
	    
	    // use the dual list box as our widget
	    

	  		// add some items to the list
	  
		/*
		dualListBox.addLeft("Apples");
	    dualListBox.addLeft("Bananas");
	    dualListBox.addLeft("Cucumbers");
	    dualListBox.addLeft("Dates");
	    dualListBox.addLeft("Enchiladas");
     */
		
		
	}

	
	private void refreshSourceEndpoints() {

			final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
			endpointService.getEndpointsForUser(
												SessionHelper.getCurrentUserID(),
												new AsyncCallback<LinkedList<EndpointData>>() {
													public void onFailure(
															Throwable caught) {
														Window.alert("Error"
																+ caught.getMessage());
													}

													@Override
													public void onSuccess(
															LinkedList<EndpointData> result) {

														Iterator<EndpointData> it = result
																.iterator();
														

														while (it.hasNext()) {

															final EndpointData currentItem = it
																	.next();
														    dualListBox.addRight(currentItem.endpointName);
															
															}
														}
												});

	}
		
	

	private void refreshTargetEndpoints() {

		// show animation while rpc is processed

		final AnimationLoading animationLoading = new AnimationLoading();

		// query endpoints that are already part of the current sphere
		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
			endpointService.getEndpointsForSphere(sphereID,
				new AsyncCallback<LinkedList<EndpointData>>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error" + caught.getMessage());
					}

					@Override
					public void onSuccess(
							LinkedList<EndpointData> result) {

						Iterator<EndpointData> it = result
								.iterator();
						

						while (it.hasNext()) {

							final EndpointData currentItem = it
									.next();
						    dualListBox.addLeft(currentItem.endpointName);
							
							}
						}
				});
	
	}

	

}
