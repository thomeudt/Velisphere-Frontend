package com.velisphere.tigerspice.client.spheres;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.client.users.UserServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.SphereData;

public class EndpointLister extends Composite {
	
	ListBox lstEndpoints;
	VerticalPanel mainPanel; 
	
	public EndpointLister() {

		initWidget(createBaseLayout());
		getEndpoints();
		
	}
	
	private VerticalPanel createBaseLayout(){
		
		
		
		
		Row row2 = new Row();
		
		Column col3 = new Column(2);
		col3.add(new HTML("<b>Select your Device</b>"));
		row2.add(col3);
		
		Column col4 = new Column(4);
		lstEndpoints = new ListBox();
		lstEndpoints.setVisibleItemCount(7);
		lstEndpoints.setWidth("100%");
				
		col4.add(lstEndpoints);
		
		row2.add(col4);
		
		Row row3 = new Row();
		Column col5 = new Column(2,2);
		final Button btnOpenEndpoint = new Button("Open Selected Device");
		btnOpenEndpoint.setType(ButtonType.PRIMARY);
		btnOpenEndpoint.setSize(ButtonSize.SMALL);
		btnOpenEndpoint.setWidth("100%");
		btnOpenEndpoint.setEnabled(false);
		col5.add(btnOpenEndpoint);
		row3.add(col5);
		
	
		lstEndpoints.addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				btnOpenEndpoint.setEnabled(true);
			}
			
		});
	
			
		btnOpenEndpoint.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(lstEndpoints != null){
					AppController.openEndpoint(lstEndpoints.getValue());	
				}
				
			}
			
		});
		
		VerticalPanel vP = new VerticalPanel();
		vP.add(row2);
		vP.add(row3);

		mainPanel = vP;
		
		return vP;
		
	}
	
	private void getEndpoints(){
		
		
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		
		endpointService.getEndpointsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<EndpointData>>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {
						// TODO Auto-generated method stub

						if (result.isEmpty())
						{
					
							mainPanel.clear();
							mainPanel.add(new HTML("<b>No Devices Available</b><br></br>"
									+ "You have not yet provisioned any endpoints. Use the Provisioning Wizard to add "
									+ "a new endpoint."));
							
						}
						else
						{
							
						
						Iterator<EndpointData> it = result.iterator();
						
						while (it.hasNext()){
						
							EndpointData current = it.next();
							lstEndpoints.addItem(current.endpointName, current.endpointId);
						}
						}
				
						
					}
			
				});
		
		
		
	}

			
		
	
	
}
