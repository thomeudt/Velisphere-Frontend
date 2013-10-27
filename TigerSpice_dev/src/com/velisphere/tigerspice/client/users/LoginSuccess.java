/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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
import com.github.gwtbootstrap.client.ui.Container;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.StackProgressBar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.NavBar;
import com.velisphere.tigerspice.client.endpointclasses.EPCList;
import com.velisphere.tigerspice.client.endpoints.EndpointList;
import com.velisphere.tigerspice.client.helper.Banderole;
import com.velisphere.tigerspice.client.properties.PropertyList;
import com.velisphere.tigerspice.shared.UserData;


public class LoginSuccess extends Composite {

interface MyBinder extends UiBinder<Widget, LoginSuccess>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);

	
	RootPanel rootPanel;
	RootPanel rootPanelHeader;
	VerticalPanel mainPanel;
	NavBar navBar;
	@UiField PageHeader pageHeader;
	@UiField Bar pgbGreen;
	@UiField Bar pgbYellow;
	@UiField Bar pgbRed;
	@UiField StackProgressBar pgbUtilization;
	String userName;
	
 	
	public LoginSuccess() {
		
		 String sessionID = Cookies.getCookie("sid");
	       System.out.println("Session ID: " +sessionID);
	       if (sessionID == null)
	       {
	       	Login loginScreen = new Login();
	   		loginScreen.onModuleLoad();
	       } else
	       {
	           checkWithServerIfSessionIdIsStillLegal(sessionID);
	       }
		initWidget(uiBinder.createAndBindUi(this));	
		
	}	

	   
	
	public void loadContent(){

		// set history for back button support
		History.newItem("login_success");
		// always reload the nav bar as it is context sensitive
		rootPanelHeader = RootPanel.get("stockList");
		rootPanelHeader.clear();
		rootPanelHeader.getElement().getStyle().setPosition(Position.RELATIVE);
		navBar = new NavBar();
		rootPanelHeader.add(navBar);
		
		
	}
		

	
	
	private void checkWithServerIfSessionIdIsStillLegal(String sessionID)
	{
	    LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserData>()
	    {
	        @Override
	        public void onFailure(Throwable caught)
	        {
	        	Login loginScreen = new Login();
	    		loginScreen.onModuleLoad();
	        }
	 
	        @Override
	        public void onSuccess(UserData result)
	        {
	            if (result == null)
	            {
	            	Login loginScreen = new Login();
	        		loginScreen.onModuleLoad();
	            } else
	            {
	                if (result.getLoggedIn())
	                {
	                	// load all other content
	                	loadContent();
	            		// set page header welcome back message
	                	pageHeader.setText("Welcome Back, " + result.userName);
	                	pgbGreen.setPercent(30);
	                	
	                	pgbRed.setPercent(0);
	                	pgbYellow.setPercent(0);
	                	pgbGreen.setText("30/100 endpoints activated");
	                	
	                } else
	                {
	                	Login loginScreen = new Login();
		        		loginScreen.onModuleLoad();
	                }
	            }
	        }
	 
	    });
	}

		
	
	
	
}
