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
package com.velisphere.tigerspice.client.appcontroller;



import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.Dropdown;
import com.github.gwtbootstrap.client.ui.NavForm;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Navbar;
import com.github.gwtbootstrap.client.ui.ResponsiveNavbar;
import com.github.gwtbootstrap.client.ui.constants.NavbarPosition;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
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
import com.velisphere.tigerspice.client.LoggedOutHome;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.admin.Overviewer;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.event.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.client.logic.LogicDesignList;
import com.velisphere.tigerspice.client.marketplace.MarketPlace;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.status.StatusBoard;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.client.users.NewAccountScreen;
import com.velisphere.tigerspice.shared.UserData;

public class NavBar extends Composite implements HasText {

	@UiField Navbar navbar;
	@UiField NavLink btnLister;
	@UiField NavLink btnLogout;
	@UiField NavLink btnAccount;
	@UiField Dropdown dpdAccount;
	@UiField Dropdown dpdAdmin;
	@UiField NavLink btnSearch;
	@UiField NavLink btnDash;
	@UiField NavLink btnLocator;
	@UiField NavLink btnManageEPC;
	@UiField NavLink btnSpheres;
	@UiField NavLink btnRules;
	@UiField NavLink btnAnalytics;
	@UiField NavLink btnShop;
	@UiField NavForm forSearch;
	@UiField NavLink btnHome;
	@UiField NavLink btnBilling;
	@UiField NavLink btnSupport;
	@UiField Brand brdHome;
	
	
	
	
	private static NavBarUiBinder uiBinder = GWT.create(NavBarUiBinder.class);

	private HandlerRegistration sessionHandler;
	
	interface NavBarUiBinder extends UiBinder<Widget, NavBar> {
	}

	public NavBar() {
		navbar = new Navbar();
	
		initWidget(uiBinder.createAndBindUi(this));
		navbar.setPosition(NavbarPosition.TOP);
		//navbar.setWidth(RootPanel.get("main").getElement().getStyle().getWidth());
		//brdHome.setHref("#");

		brdHome.getElement().getStyle().setCursor(Cursor.POINTER); 
		btnLister.setVisible(false);
	
   	 btnLogout.setVisible(false);
   	 btnAccount.setVisible(false);
   	 btnSearch.setVisible(false);
   	 btnSpheres.setVisible(false);
   	 btnDash.setVisible(false);
   	 btnRules.setVisible(false);
   	 btnAnalytics.setVisible(false);
   	 forSearch.setVisible(false);
   	 btnHome.setVisible(false);
   	 dpdAccount.setVisible(false);
   	 dpdAdmin.setVisible(false);
   	 btnLocator.setVisible(false);
   	 btnManageEPC.setVisible(false);
	 	btnShop.setVisible(false);
	 	  
   	 
   	 
	    //checkWithServerIfSessionIdIsStillLegal();
		   
		
	}

	
	 
	
	public NavBar(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	
	}

	@UiHandler("btnAccount")
	void openNewAccountScreen (ClickEvent event) {
				
		AppController.openChangeAccount();
		
		
	}
	
	@UiHandler("btnHome")
	void openStatus (ClickEvent event) {
		
		AppController.openStatusBoard();
		
	}

	@UiHandler("brdHome")
	void openHome (ClickEvent event) {
		
		AppController.openHome();
		
	}

	
	@UiHandler("btnDash")
	void openDash (ClickEvent event) {
		
		AppController.openDashBoard();
		
	}
	
	
	@UiHandler("brdHome")
	void openHomeBrand (ClickEvent event) {
		
		AppController.openHome();
		
		/*
		String sessionID = Cookies.getCookie("sid");
		clearBandarole();
	     if (sessionID != null){
			
			RootPanel.get("main").clear();
			LoginSuccess loginSuccess = new LoginSuccess();
			RootPanel.get("main").add(loginSuccess);
		}
	     else
	    	 {
	    	 LoggedOutHome loggedOutHome = new LoggedOutHome();
	    	 loggedOutHome.open();
		}
		*/	
			
	}
	
	@UiHandler("btnLocator")
	void openLocator (ClickEvent event) {
		
		AppController.openGeoLocator();
		
		
	}
	

	@UiHandler("btnSpheres")
	void openSpheres (ClickEvent event) {
		
		AppController.openEndpointManager();
		
		/**
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		clearBandarole();
		SphereLister sphereOverview = new SphereLister(); 		
		
		mainPanel.add(sphereOverview);
		**/
		
	}
	
	@UiHandler("btnRules")
	void openRules (ClickEvent event) {
	
		AppController.openLogicDesigner();
		
		/**
		
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		clearBandarole();
		// CheckpathView ruleOverview = new CheckpathView(); 		
		// mainPanel.add(ruleOverview);

		CheckpathList checkpathList = new CheckpathList();
		mainPanel.add(checkpathList);
	**/
		
	}
		

	@UiHandler("btnAnalytics")
	void openAnalytics (ClickEvent event) {
	
		AppController.openAnalytics();
		
		
	}

	@UiHandler("btnManageEPC")
	void openEPCManager (ClickEvent event) {
	
		AppController.openEPCManager("");
		
	}
	
	
	
	
	@UiHandler("btnManagePropertyClass")
	void openPropertyClassManager (ClickEvent event) {
	
		AppController.openPropertyClassManager("");
		
	}
	
	@UiHandler("btnManagePlan")
	void openPlanManager (ClickEvent event) {
	
		AppController.openPlanManager();
		
	}
	
	@UiHandler("btnNewLogic")
	void openNewLogicDesigner (ClickEvent event) {
	
		AppController.openNewLogicDesigner();
		
	}
	
	@UiHandler("btnManageVendor")
	void openVendorManager (ClickEvent event) {
	
		AppController.openVendorManager();
		
	}
	
	
	@UiHandler("btnLister")
	void openAdmin (ClickEvent event) {
		
		clearBandarole();
		Overviewer overviewer = new Overviewer();
		overviewer.loadContent();
	}

	
	@UiHandler("btnShop")
	void openMarketplace (ClickEvent event) {
		AppController.openMarket();
	
		
	}

	
	@UiHandler("btnLogout")
	void logout (ClickEvent event) {
		Cookies.removeCookie("sid", "/");
		
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
		

		btnLister.setVisible(false);
   	 	btnLogout.setVisible(false);
   	 	btnAccount.setVisible(false);
   	 	btnSearch.setVisible(false);
   	 	btnSpheres.setVisible(false);
   	 	btnDash.setVisible(false);
   	 	btnRules.setVisible(false);
   	    btnAnalytics.setVisible(false);
   	 	forSearch.setVisible(false);
   	 	btnHome.setVisible(false);
   	 	btnShop.setVisible(false);
   	 	
		
   	 	SessionHelper.validateCurrentSession();		
		
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		    	
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			

								sessionHandler.removeHandler();
								dpdAccount.setTitle(SessionHelper.getCurrentUserName());
		}
		});

		
			 
		
	    
	    
	}

	public void activateForCurrentUser()
	{
		
		
			btnLister.setVisible(true);
	    	 btnLogout.setVisible(true);
	    	 btnAccount.setVisible(true);
	    	 btnSearch.setVisible(false);
	    	 btnSpheres.setVisible(true);
	    	 btnDash.setVisible(true);
	    	 btnRules.setVisible(true);
	    	 btnAnalytics.setVisible(true);
	    	 forSearch.setVisible(false);
	    	 btnHome.setVisible(true);
	    	 dpdAccount.setVisible(true);
	       	 btnLocator.setVisible(true);
	       	 btnManageEPC.setVisible(true);
	       	 dpdAccount.setText(SessionHelper.getCurrentUserName());
	  	     btnShop.setVisible(false);
	  	     btnBilling.setDisabled(true);
	  	     btnSupport.setDisabled(true);
	  	   checkAdmin();
	    
	}

	
	private void checkAdmin()
	{
		if (SessionHelper.getCurrentUserID() == VeliConstants.SUPERUSER_ID)
		{
			dpdAdmin.setVisible(true);	
		}
		else
		{
			dpdAdmin.setVisible(false);
		}
		
		// disable also closing/pw change for demo account)
		
		if (SessionHelper.getCurrentUserID() == "e8c2f32c-3990-42bd-9dbf-5ebf6720f2b5")
		{
			btnAccount.setDisabled(true);
		}
      	 
	}
	
	private void clearBandarole(){
		RootPanel banderolePanel = RootPanel.get("banderole");
		banderolePanel.clear();
	}

}
