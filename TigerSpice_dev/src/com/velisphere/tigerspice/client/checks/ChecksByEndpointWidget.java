package com.velisphere.tigerspice.client.checks;

import com.google.gwt.user.client.ui.Composite;

public class ChecksByEndpointWidget extends Composite {

	
	public ChecksByEndpointWidget()
	{
	
	
	
	
	rpcService
	.getEndpointsForUser(
			userID,
			new AsyncCallback<Vector<EndpointData>>() {
				public void onFailure(
						Throwable caught) {
					Window.alert("Error"
							+ caught.getMessage());
				}

				@Override
				public void onSuccess(
						Vector<EndpointData> result) {

					Iterator<EndpointData> it = result
							.iterator();
					removeLoadAnimation(animationLoading);

				}
	
}
