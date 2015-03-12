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
package com.velisphere.tigerspice.client.users;




import org.voltdb.types.TimestampType;










import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.NavBar;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.event.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.helper.HelperServiceAsync;
import com.velisphere.tigerspice.shared.MontanaStatsData;
import com.velisphere.tigerspice.shared.UserData;


public class LoginSuccess extends Composite  {

interface MyBinder extends UiBinder<Widget, LoginSuccess>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);

	
	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	@UiField PageHeader pageHeader;
	@UiField Breadcrumbs brdMain;
	NavLink bread0;
	String userName;
	@UiField Paragraph voltIP;
	@UiField Paragraph voltHostID;
	@UiField Paragraph voltHostName;
	@UiField Paragraph voltConnHostName;
	@UiField Paragraph voltConnID;
	@UiField Paragraph voltBytesRead;
	@UiField Paragraph voltBytesWritten;
	@UiField Paragraph voltMsgRead;
	@UiField Paragraph voltMsgWritten;
	@UiField Paragraph voltTimestamp;

	
	
	
 	
	public LoginSuccess() {
	    
		NavBar navBar = new NavBar();
		navBar.activateForCurrentUser();
		initWidget(uiBinder.createAndBindUi(this));
				
		loadContent();
		
			
	}
	 
	public LoginSuccess(HandlerRegistration reg) {
		
		initWidget(uiBinder.createAndBindUi(this));
		reg.removeHandler();
		//AppController.openHome();
		
		loadContent();
			
	}
	
	
	public void loadContent(){

		
		
		//initWidget(uiBinder.createAndBindUi(this));	
		// set history for back button support
		//History.newItem("login_success");
		
		//
		
		
		// set page header welcome back message
    	pageHeader.setText("Welcome back, " + SessionHelper.getCurrentUserName());
    	
    	
    	
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);

		
		final HelperServiceAsync helperService = GWT
				.create(HelperService.class);
		
		helperService.getMontanaStats(new AsyncCallback<MontanaStatsData>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				System.out.println("[ER] Failed to get Montana Stats Data. Error: " + caught);
				
			}
			
			@Override
			public void onSuccess(MontanaStatsData result) {
				// TODO Auto-generated method stub
				System.out.println("[IN] Successfully retrieved Montana Stats, Stats Object: " + result);
				voltIP.setText("Connected to VoltIP via montanaconf.xml: " + result.IP);
				System.out.println("[IN] Host ID: " + result.hostId);
				voltHostID.setText("Montana Host ID reported by VoltDB: " + result.hostId);
				System.out.println("[IN] Hostname: " + result.hostname);
				voltHostName.setText("Montana Hostname/IP reported by VoltDB: " + result.hostname);
				System.out.println("[IN] Bytes Read: " + result.bytesRead);
				voltBytesRead.setText("Bytes read, reported by VoltDB: " + result.bytesRead);
				System.out.println("[IN] Bytes Written: " + result.bytesWritten);
				voltBytesWritten.setText("Bytes written, reported by VoltDB: " + result.bytesWritten);
				System.out.println("[IN] Connection Hostname: " + result.connectionHostname);
				voltConnHostName.setText("Name/IP of connecting host, reported by VoltDB: " + result.connectionHostname);
				System.out.println("[IN] Connection ID: " + result.connectionId);
				voltConnID.setText("Connection ID, reported by VoltDB: " + result.connectionId);
				System.out.println("[IN] Messages Read: " + result.messagesRead);
				voltMsgRead.setText("Messages read, reported by VoltDB: " + result.messagesRead);
				System.out.println("[IN] Messages Written: " + result.messagesWritten);
				voltMsgWritten.setText("Messages written, reported by VoltDB: " + result.messagesWritten);
				System.out.println("[IN] Timestamp: " + result.timestamp);
				voltTimestamp.setText("Timestamp of VoltDB report: " + result.timestamp);
				
				
				
			}
			
		});
		
		
	}
		
	
	
}

	