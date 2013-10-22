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
package com.velisphere.tigerspice.client;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Navbar;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.NavbarPosition;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.NewAccountScreen;
import com.velisphere.tigerspice.shared.UserData;

public class NavBar extends Composite implements HasText {

	@UiField Navbar navbar;
	@UiField NavLink btnAdmin; 
	
	private static NavBarUiBinder uiBinder = GWT.create(NavBarUiBinder.class);

	interface NavBarUiBinder extends UiBinder<Widget, NavBar> {
	}

	public NavBar() {
		navbar = new Navbar();
		initWidget(uiBinder.createAndBindUi(this));
		navbar.setPosition(NavbarPosition.TOP);
		  String sessionID = Cookies.getCookie("sid");
		     System.out.println("Session ID: " +sessionID);
		     if (sessionID == null)
		     {
		    	 btnAdmin.setVisible(false);
		     } else
		     {
		         checkWithServerIfSessionIdIsStillLegal(sessionID);
		     }
		
	}

	
	 
	
	public NavBar(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	
	}

	@UiHandler("btnAccount")
	void openNewAccountScreen (ClickEvent event) {
		// Window.alert("Logging In");
		NewAccountScreen newAccountScreen = new NewAccountScreen();
		newAccountScreen.open();
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	@UiHandler("btnHome")
	void openHome (ClickEvent event) {
		// Window.alert("Logging In");
	
		// RootPanel.get().clear();
		Login loginScreen = new Login();
		loginScreen.onModuleLoad();
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	@UiHandler("btnAdmin")
	void openAdmin (ClickEvent event) {
		Overviewer overviewer = new Overviewer();
		overviewer.onModuleLoad();
			
		
		
	}
	
	@UiHandler("btnLogout")
	void logout (ClickEvent event) {
		Cookies.removeCookie("sid");
	    Login loginScreen = new Login();
		loginScreen.onModuleLoad();
	
	}
	
	
	public void setText(String text) {
	
	}

	public String getText() {
		return null;
	
	}
	
	private void checkWithServerIfSessionIdIsStillLegal(String sessionID)
	{
	    LoginService.Util.getInstance().loginFromSessionServer(new AsyncCallback<UserData>()
	    {
	        @Override
	        public void onFailure(Throwable caught)
	        {
	        	btnAdmin.setVisible(false);
	        }
	 
	        @Override
	        public void onSuccess(UserData result)
	        {
	            if (result == null)
	            {
	            	btnAdmin.setVisible(false);
	            	
	            	
	            } else
	            {
	                if (result.getLoggedIn())
	                {
	                   
	                } else
	                {
	                	btnAdmin.setVisible(false);
	                }
	            }
	        }
	 
	    });
	}


}
