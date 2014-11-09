package com.velisphere.tigerspice.client.locator;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.LogService;
import com.velisphere.tigerspice.client.analytics.LogServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointActorWidget;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class Locator extends Composite {

	
	String userID;
	@UiField
	Breadcrumbs brdMain;
	@UiField
	TabPanel tabPanel;
	@UiField
	PageHeader pageHeader;
	@UiField
	Column colMap;

	
	
	NavLink bread0;
	NavLink bread1;

	private static GeoLocatorUiBinder uiBinder = GWT
			.create(GeoLocatorUiBinder.class);

	interface GeoLocatorUiBinder extends UiBinder<Widget, Locator> {
	}

	public Locator() {
		this.userID = SessionHelper.getCurrentUserID();
		initWidget(uiBinder.createAndBindUi(this));
		loadContent();
		
		
		
	}
	
	public void loadContent() {

		// set page header welcome back message
		pageHeader.setText("VeliSphere Location Service");

		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Analytics");
		brdMain.add(bread1);
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			AppController.openHome();

			}

		});
	}

	

}
