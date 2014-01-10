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
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.velisphere.tigerspice.client.helper.EventUtils;
import com.velisphere.tigerspice.client.helper.SessionHelper;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.rules.CheckpathList;
import com.velisphere.tigerspice.client.rules.CheckpathCreateView;
import com.velisphere.tigerspice.client.spheres.SphereLister;
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
	@UiField NavLink btnRules;
	@UiField NavForm forSearch;
	@UiField NavLink btnHome;
	@UiField Brand brdHome;
	@UiField NavText txtUserName;
	
	private static NavBarUiBinder uiBinder = GWT.create(NavBarUiBinder.class);

	private HandlerRegistration sessionHandler;
	
	interface NavBarUiBinder extends UiBinder<Widget, NavBar> {
	}

	public NavBar() {
		navbar = new Navbar();
		initWidget(uiBinder.createAndBindUi(this));
		navbar.setPosition(NavbarPosition.TOP);

		btnAdmin.setVisible(false);
   	 btnLogout.setVisible(false);
   	 btnAccount.setVisible(false);
   	 btnSearch.setVisible(false);
   	 btnSpheres.setVisible(false);
   	 btnRules.setVisible(false);
   	 forSearch.setVisible(false);
   	 btnHome.setVisible(false);
   	 txtUserName.setText("Not Logged In");
	    //checkWithServerIfSessionIdIsStillLegal();
		   
		
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
		String sessionID = Cookies.getCookie("sid");
		clearBandarole();
	     if (sessionID != null){
			
			RootPanel.get("main").clear();
			LoginSuccess loginSuccess = new LoginSuccess();
			RootPanel.get("main").add(loginSuccess);
		}
	     else
	    	 {
	    	 Login loginScreen = new Login();
	 		loginScreen.onModuleLoad();
			
		}
		
	}

	@UiHandler("btnSpheres")
	void openSpheres (ClickEvent event) {
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		clearBandarole();
		SphereLister sphereOverview = new SphereLister(); 		
		
		mainPanel.add(sphereOverview);
		
	}
	
	@UiHandler("btnRules")
	void openRules (ClickEvent event) {
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		clearBandarole();
		// CheckpathView ruleOverview = new CheckpathView(); 		
		// mainPanel.add(ruleOverview);

		CheckpathList checkpathList = new CheckpathList();
		mainPanel.add(checkpathList);
	
		
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
	
	public void checkWithServerIfSessionIdIsStillLegal()
	{
		

		btnAdmin.setVisible(false);
   	 	btnLogout.setVisible(false);
   	 	btnAccount.setVisible(false);
   	 	btnSearch.setVisible(false);
   	 	btnSpheres.setVisible(false);
   	 	btnRules.setVisible(false);
   	 	forSearch.setVisible(false);
   	 	btnHome.setVisible(false);
   	 	txtUserName.setText("Not Logged In");
		
   	 	SessionHelper.validateCurrentSession();		
		
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		    	
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			

								// get all endpoints for user id in current
								// session
								sessionHandler.removeHandler();
								txtUserName.setText(SessionHelper.getCurrentUserName());
		}
		});

		
			 
		
	    
	    
	}

	public void activateForCurrentUser()
	{
		
		
			btnAdmin.setVisible(true);
	    	 btnLogout.setVisible(true);
	    	 btnAccount.setVisible(true);
	    	 btnSearch.setVisible(true);
	    	 btnSpheres.setVisible(true);
	    	 btnRules.setVisible(true);
	    	 forSearch.setVisible(true);
	    	 btnHome.setVisible(true);
			 txtUserName.setText(SessionHelper.getCurrentUserName());
	    
	}

	
	private void clearBandarole(){
		RootPanel banderolePanel = RootPanel.get("banderole");
		banderolePanel.clear();
	}

}
