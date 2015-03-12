package com.velisphere.tigerspice.client.analytics;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.NavBar;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class AnalyticsHome extends Composite {

	private static AnalyticsHomeUiBinder uiBinder = GWT
			.create(AnalyticsHomeUiBinder.class);

	interface AnalyticsHomeUiBinder extends UiBinder<Widget, AnalyticsHome> {
	}

	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	@UiField
	PageHeader pageHeader;

	@UiField
	Breadcrumbs brdMain;
	@UiField
	TabPanel tabPanel;

	
	
	NavLink bread0;
	NavLink bread1;
	String userName;
	String userID;
	String sphereIDActor;
	String endpointIDActor;
	String endpointNameActor;
	String propertyIDActor;
	String propertyNameActor;
	String sphereIDSensor;
	String endpointIDSensor;
	String endpointNameSensor;
	String propertyIDSensor;
	String propertyNameSensor;
	
	public AnalyticsHome() {
		this.userID = SessionHelper.getCurrentUserID();
		initWidget(uiBinder.createAndBindUi(this));

		loadContent();
	}
	
	public AnalyticsHome(String sphereID, String endpointID, String propertyID, String endpointName, String propertyName, boolean sensor) {
		this.userID = SessionHelper.getCurrentUserID();
		
		
	
		if(sensor != true){
			
			this.sphereIDActor = sphereID;
			
			this.endpointIDActor = endpointID;
			this.endpointNameActor = endpointName;
			this.propertyIDActor = propertyID;
			this.propertyNameActor = propertyName;
			initWidget(uiBinder.createAndBindUi(this));
			tabPanel.selectTab(1);
		} else 
		{
			this.sphereIDSensor = sphereID;
			this.endpointIDSensor = endpointID;
			this.endpointNameSensor = endpointName;
			this.propertyIDSensor = propertyID;
			this.propertyNameSensor = propertyName;
			initWidget(uiBinder.createAndBindUi(this));
			
		}
		
		
			
		
		
		
	

		loadContent();
	}

	public void loadContent() {

		// set page header welcome back message
		pageHeader.setText("VeliSphere Analytics");

		final LogServiceAsync logService = GWT.create(LogService.class);

		/**
		 * NavBar navBar = new NavBar(); navBar = (NavBar)
		 * RootPanel.get("stockList").getWidget(0);
		 * navBar.activateForCurrentUser();
		 **/

		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Analytics");
		brdMain.add(bread1);
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}

		});
	}

	@UiFactory
	ChartSensorHistoryWidget makeSensorHistory() { // method name is
													// insignificant
		System.out.println("SPIDS: " + sphereIDSensor);
		return new ChartSensorHistoryWidget(userID, sphereIDSensor, endpointIDSensor, propertyIDSensor, endpointNameSensor, propertyNameSensor);
	}

	@UiFactory
	ChartActorHistoryWidget makeActorHistory() { // method name is insignificant
		System.out.println("SPIDA: " + sphereIDActor);
		return new ChartActorHistoryWidget(userID, sphereIDActor, endpointIDActor, propertyIDActor, endpointNameActor, propertyNameActor);
		
	}

}
