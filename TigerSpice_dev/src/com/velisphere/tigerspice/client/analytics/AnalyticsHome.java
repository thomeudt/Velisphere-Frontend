package com.velisphere.tigerspice.client.analytics;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Bar;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.NavBar;
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
	@UiField PageHeader pageHeader;


	@UiField Breadcrumbs brdMain;
	@UiField Paragraph pgpLogCount;
	NavLink bread0;
	NavLink bread1;
	String userName;
	
	
	public AnalyticsHome() {
		initWidget(uiBinder.createAndBindUi(this));
		loadContent();
	}
	
public void loadContent(){

		
		
		//initWidget(uiBinder.createAndBindUi(this));	
		// set history for back button support
		History.newItem("dashboard");
		
		//
		
		
		// set page header welcome back message
    	pageHeader.setText("Analytics for " + SessionHelper.getCurrentUserName());
    	
    	final LogServiceAsync logService = GWT
				.create(LogService.class);
		
    	    	

    	logService.getLogCount(new AsyncCallback<LinkedList<String>>(){
    		@Override
    		public void onFailure(Throwable caught) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void onSuccess(LinkedList<String> result) {
    			// TODO Auto-generated method stub
    	
    			int logCount = 0;
    			Iterator<String> iT = result.iterator();
    			while (iT.hasNext()){
    				int entry = Integer.parseInt(iT.next());
    				logCount = logCount + entry;
    			}
    			
    			pgpLogCount.setText("Number of record sets in analytics database: " + logCount);
    		}
    		});

    	

    	
    	
    	
    	
    	/**
		NavBar navBar = new NavBar();
		navBar = (NavBar) RootPanel.get("stockList").getWidget(0);
		navBar.activateForCurrentUser();
		**/
    	
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Dashboard");
		brdMain.add(bread1);
		bread0.addClickHandler(new ClickHandler() {
		public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}

		});
	}

}
