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
import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.NavForm;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavText;
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
import com.velisphere.tigerspice.client.admin.Overviewer;
import com.velisphere.tigerspice.client.spheres.SphereEditor;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.client.users.NewAccountScreen;
import com.velisphere.tigerspice.shared.UserData;

public class NavBar extends Composite implements HasText {

	@UiField Navbar navbar;
	@UiField NavLink btnAdmin;
	@UiField NavLink btnLogout;
	@UiField NavLink btnAccount;
	@UiField NavLink btnSearch;
	@UiField NavLink btnSpheres;
	@UiField NavLink btnMap;
	@UiField NavForm forSearch;
	@UiField NavLink btnHome;
	@UiField Brand brdHome;
	@UiField NavText txtUserName;
	
	
	private static NavBarUiBinder uiBinder = GWT.create(NavBarUiBinder.class);

	interface NavBarUiBinder extends UiBinder<Widget, NavBar> {
	}

	public NavBar() {
		navbar = new Navbar();
		initWidget(uiBinder.createAndBindUi(this));
		navbar.setPosition(NavbarPosition.TOP);
		
		  String sessionID = Cookies.getCookie("sid");
		     
		     if (sessionID == null)
		     {
		    	 btnAdmin.setVisible(false);
		    	 btnLogout.setVisible(false);
		    	 btnAccount.setVisible(false);
		    	 btnSearch.setVisible(false);
		    	 btnSpheres.setVisible(false);
		    	 btnMap.setVisible(false);
		    	 forSearch.setVisible(false);
		    	 btnHome.setVisible(false);
		    	 txtUserName.setText("Not Logged In");
	 
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
		
		clearBandarole();
		NewAccountScreen newAccountScreen = new NewAccountScreen();
		newAccountScreen.open();
			
		
		
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	@UiHandler("btnHome")
	void openHome (ClickEvent event) {
		String sessionID = Cookies.getCookie("sid");
		clearBandarole();
	     if (sessionID != null){
			
			RootPanel.get("main").clear();
			LoginSuccess loginSuccess = new LoginSuccess();
			RootPanel.get("main").add(loginSuccess);
		}
	     else
	    	 {
	    	 NewAccountScreen newAccountScreen = new NewAccountScreen();
				newAccountScreen.open();
			
		}
	}
	
	@UiHandler("brdHome")
	void openHomeBrand (ClickEvent event) {
		Login loginScreen = new Login();
		clearBandarole();
		loginScreen.onModuleLoad();
		
	}

	@UiHandler("btnSpheres")
	void openSpheres (ClickEvent event) {
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		clearBandarole();
		SphereEditor sphereOverview = new SphereEditor(); 		
		
		mainPanel.add(sphereOverview);
		
	}
	
	@UiHandler("btnAdmin")
	void openAdmin (ClickEvent event) {
		
		clearBandarole();
		Overviewer overviewer = new Overviewer();
		overviewer.onModuleLoad();
	}
	
	@UiHandler("btnLogout")
	void logout (ClickEvent event) {
		Cookies.removeCookie("sid");
		clearBandarole();
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
		    	 btnLogout.setVisible(false);
		    	 btnAccount.setVisible(false);
		    	 btnSearch.setVisible(false);
		    	 btnSpheres.setVisible(false);
		    	 btnMap.setVisible(false);
		    	 forSearch.setVisible(false);
		    	 btnHome.setVisible(false);
		    	 txtUserName.setText("Not Logged In");
	        }
	 
	        @Override
	        public void onSuccess(UserData result)
	        {
	            if (result == null)
	            {
	            	btnAdmin.setVisible(false);
			    	 btnLogout.setVisible(false);
			    	 btnAccount.setVisible(false);
			    	 btnSearch.setVisible(false);
			    	 btnSpheres.setVisible(false);
			    	 btnMap.setVisible(false);
			    	 forSearch.setVisible(false);
			    	 btnHome.setVisible(false);
			    	 txtUserName.setText("Not Logged In");
	            	
	            } else
	            {
	                if (result.getLoggedIn())
	                {
	                	txtUserName.setText(result.userName);
	                		                   
	                } else
	                {
	                	btnAdmin.setVisible(false);
	   		    	 btnLogout.setVisible(false);
	   		    	 btnAccount.setVisible(false);
	   		    	 btnSearch.setVisible(false);
	   		    	 btnSpheres.setVisible(false);
	   		    	 btnMap.setVisible(false);
	   		    	 forSearch.setVisible(false);
	   		    	 btnHome.setVisible(false);
	   		    	 txtUserName.setText("Not Logged In");
	                }
	            }
	        }
	 
	    });
	}

	
	private void clearBandarole(){
		RootPanel banderolePanel = RootPanel.get("banderole");
		banderolePanel.clear();
	}

}
