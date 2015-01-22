package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.widgets.AlertWidget;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.SphereData;

public class SphereViewPublic extends Composite {

	private static SphereViewPublicUiBinder uiBinder = GWT
			.create(SphereViewPublicUiBinder.class);

	interface SphereViewPublicUiBinder extends
			UiBinder<Widget, SphereViewPublic> {
	}
	
	
	@UiField Breadcrumbs brdMain;
	@UiField PageHeader pghSphereNameHeader;
	@UiField ListBox lbxEndpoints;
	String sphereID;  
	String sphereName;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;

	public SphereViewPublic(final String sphereID) {

		this.sphereID = sphereID;

		initWidget(uiBinder.createAndBindUi(this));
		createBaseLayout();
		updateSphereNameAndState();
		getEndpoints();

	}

	
	private void createBaseLayout()
	{
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Endpoint Manager");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText("-");
		brdMain.add(bread2);

		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});

		bread1.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				RootPanel mainPanel = RootPanel.get("main");
				mainPanel.clear();
				SphereLister sphereLister = new SphereLister();
				mainPanel.add(sphereLister);

			}
		});

		
	}
	
	
	private void getEndpoints()
	{
		

		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		endpointService.getEndpointsForSphere(this.sphereID, new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
				// TODO Auto-generated method stub
				
				lbxEndpoints.setVisibleItemCount(7);;
				
				Iterator<EndpointData> it = result.iterator();
				while(it.hasNext())
				{
					EndpointData current = it.next();
					lbxEndpoints.addItem(current.endpointName, current.endpointId);
				}
				
				lbxEndpoints.addClickHandler(new ClickHandler(){

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						
						if (lbxEndpoints.getValue() != null)
						{
							AppController.openEndpointPublic(lbxEndpoints.getValue());
						}
						
					}
					
				});
				
				
			}
			
		});
		
	}
	
	
private void updateSphereNameAndState(){
		
		
	SphereServiceAsync sphereService = GWT
			.create(SphereService.class);
		
	sphereService.getSphereForSphereID(this.sphereID,
				new AsyncCallback<SphereData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(SphereData result) {
						// TODO Auto-generated method stub
						pghSphereNameHeader.setText(result.getName());
						brdMain.remove(bread2);
						bread2.setText(result.getName());
						brdMain.add(bread2);
						
						sphereName = result.getName();
						
						
						

					}

				});

		
	}
	
	
}
