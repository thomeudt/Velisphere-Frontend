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



import com.github.gwtbootstrap.client.ui.Bar;
import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.NavBar;
import com.velisphere.tigerspice.client.helper.EventUtils;
import com.velisphere.tigerspice.client.helper.SessionHelper;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.shared.UserData;


public class LoginSuccess extends Composite  {

interface MyBinder extends UiBinder<Widget, LoginSuccess>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);

	
	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	@UiField PageHeader pageHeader;
	@UiField
	Breadcrumbs brdMain;
	NavLink bread0;
	String userName;
	
 	
	public LoginSuccess() {
	    
			
		initWidget(uiBinder.createAndBindUi(this));
		loadContent();
		
	
		

		
	}
	 
	public LoginSuccess(HandlerRegistration reg) {
		
		initWidget(uiBinder.createAndBindUi(this));
		reg.removeHandler();
		
		loadContent();
			
	}
	
	public void loadContent(){

		
		
		//initWidget(uiBinder.createAndBindUi(this));	
		// set history for back button support
		//History.newItem("login_success");
		
		//
		
		
		// set page header welcome back message
    	pageHeader.setText("Welcome Back, " + SessionHelper.getCurrentUserName());
    	
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);

		
	}
		

	
	
			
	
	
	
}
