package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.SphereData;

 

public class SphereListerWidget extends Composite {
	
	private SphereServiceAsync rpcService;
	
	VerticalPanel verticalPanel = new VerticalPanel();;
	

	
	
	public SphereListerWidget() {

		rpcService = GWT.create(SphereService.class);
		
		
		refreshPrivateSpheres();
		initWidget(verticalPanel);
	
		
		
	}


	private void refreshPrivateSpheres(){
		
		
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

					Hyperlink hprlnkNewHyperlink = new Hyperlink(it.next().sphereName, false, "newHistoryToken");
					
					verticalPanel.add(hprlnkNewHyperlink);
					
				}
			
			}

			// We've successfully for the data from the RPC call,
			// Now we update the row data with that result starting
			// at a particular row in the cell widget (usually the range start)

		});
		
		
	}

}