package com.velisphere.tigerspice.client.appcontroller;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.admin.ManagePlan;
import com.velisphere.tigerspice.client.admin.epc.AddPropertyToEPC;
import com.velisphere.tigerspice.client.admin.epc.CreateEPC;
import com.velisphere.tigerspice.client.admin.epc.EditEPC;
import com.velisphere.tigerspice.client.admin.epc.EditEPCInputPage;
import com.velisphere.tigerspice.client.admin.epc.RetireEPC;
import com.velisphere.tigerspice.client.admin.propertyclass.CreatePropertyClass;
import com.velisphere.tigerspice.client.admin.propertyclass.EditPropertyClass;
import com.velisphere.tigerspice.client.admin.vendor.CreateVendor;
import com.velisphere.tigerspice.client.analytics.AnalyticsHome;
import com.velisphere.tigerspice.client.dash.ConfigurableDashboard;
import com.velisphere.tigerspice.client.documentation.BestPractices;
import com.velisphere.tigerspice.client.documentation.GettingStarted;
import com.velisphere.tigerspice.client.endpoints.EndpointView;
import com.velisphere.tigerspice.client.endpoints.EndpointViewPublic;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.event.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.locator.Locator;
import com.velisphere.tigerspice.client.logic.LogicDesignList;
import com.velisphere.tigerspice.client.logic.LogicDesigner;
import com.velisphere.tigerspice.client.marketplace.MarketPlace;
import com.velisphere.tigerspice.client.provisioning.ProvisioningSuccess;
import com.velisphere.tigerspice.client.provisioning.ProvisioningWizard;
import com.velisphere.tigerspice.client.provisioning.TakeOwnership;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.spheres.SphereView;
import com.velisphere.tigerspice.client.spheres.SphereViewPublic;
import com.velisphere.tigerspice.client.status.StatusBoard;
import com.velisphere.tigerspice.client.users.ChangeAccount;
import com.velisphere.tigerspice.client.users.CloseAccountConfirm;
import com.velisphere.tigerspice.client.users.LoginSuccess;

// this class manages all navigational calls within the application

public class AppController {
	
	private static HandlerRegistration sessionHandler;

		


	static void openWithHistoryHandler(final String token, final Object screenObject)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add((Widget) screenObject);
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add((Widget) screenObject);
		        }
			}
		    });
	}

	
	static void openLogicDesignWithHistoryHandler(final String token, final String checkpathID, final String userID)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new LogicDesigner(userID, checkpathID));
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new LogicDesigner(userID, checkpathID));
		        }
			}
		    });
	}



	static void openDashSelectedWithHistoryHandler(final String token, final String dashID)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new ConfigurableDashboard(dashID));
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
  	      		  RootPanel.get("main").add(new ConfigurableDashboard(dashID));
		        }
			}
		    });
	}

	
	static void openSphereWithHistoryHandler(final String token, final String sphereId)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new SphereView(sphereId));
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new SphereView(sphereId));
		        }
			}
		    });
	}
	
	static void openPublicSphereWithHistoryHandler(final String token, final String sphereId)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new SphereViewPublic(sphereId));
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new SphereViewPublic(sphereId));
		        }
			}
		    });
	}


	static void openTakeDeviceOwnershipWithHistoryHandler(final String token, final String uEPID, final String identifier, final String endpointclassID, final String endpointclassName)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new TakeOwnership(uEPID, identifier, endpointclassID, endpointclassName));
				 
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new TakeOwnership(uEPID, identifier, endpointclassID, endpointclassName));
		        }
			}
		    });
	}

	
	static void openEndpointWithHistoryHandler(final String token, final String endpointID)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new EndpointView("", "",
				endpointID, "", ""));
				 
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new EndpointView("0", "0",
	      				endpointID, "", ""));
		        }
			}
		    });
	}

	static void openEndpointViewModeWithHistoryHandler(final String token, final String endpointID, final int viewMode)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new EndpointView("", "",
				endpointID, "", "", viewMode));
				 
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new EndpointView("0", "0",
	      				endpointID, "", "", viewMode));
		        }
			}
		    });
	}

	
	static void openEndpointPublicWithHistoryHandler(final String token, final String endpointID)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new EndpointViewPublic(endpointID));
				 
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new EndpointViewPublic(endpointID));
		        }
			}
		    });
	}


	static void openEPCInputWithHistoryHandler(final String token, final String id, final String message)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new EditEPCInputPage(id, message));
				 
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new EditEPCInputPage(id, ""));
		        }
			}
		    });
	}

	static void openEPCAddPropertyWithHistoryHandler(final String token, final String EPCId)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add(new AddPropertyToEPC(EPCId));
				 
		
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add(new AddPropertyToEPC(EPCId));
		        }
			}
		    });
	}
	
		
	

	public static void openStatusBoard()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			StatusBoard statusboard = new StatusBoard();
			openWithHistoryHandler("statusboard", statusboard);
		}		
	});
	}
	
	public static void openDashBoard()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			ConfigurableDashboard dashboard = new ConfigurableDashboard();
			openWithHistoryHandler("dashboard", dashboard);
		}		
	});
	}
	
	public static void openDashBoardWithSelected(final String dashID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			openDashSelectedWithHistoryHandler("dashboard"+dashID, dashID);
		}		
	});
	}
	
	
	
	public static void openHome()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			LoginSuccess loginSuccess = new LoginSuccess();
			openWithHistoryHandler("home", loginSuccess);
			
			//Start start = new Start();
			//start.onModuleLoad();
		}		
	});
	}

	public static void openLogicDesigner()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			LogicDesignList checkpathList = new LogicDesignList(userID);
			openWithHistoryHandler("logicdesigner", checkpathList);
		}		
	});
	}

	public static void openNewLogicDesigner()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			LogicDesigner logicDesigner = new LogicDesigner(userID);
			openWithHistoryHandler("logicdesigner_new", logicDesigner);
		}		
	});
	}

	
	public static void openLogicDesign(final String checkpathID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			
			openLogicDesignWithHistoryHandler("logicdesign"+checkpathID, checkpathID, userID);
		}		
	});
	}
	
	
	public static void deleteLogicDesign(final String checkpathID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			
			RootPanel.get("main").clear();
			LogicDesigner logicDesigner = new LogicDesigner(userID, checkpathID, true);
      	  	RootPanel.get("main").add(logicDesigner);
		}		
	});
	}
	
	
	public static void createLogicDesign()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			
			RootPanel.get("main").clear();
			RootPanel.get("main").add(new LogicDesigner(userID));
		}		
	});
	}
	

	public static void openEndpointManager()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			SphereLister endpointManager = new SphereLister(); 	
			openWithHistoryHandler("endpointmgr", endpointManager);
		}		
	});
	}

	public static void openSphere(final String sphereID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			
			openSphereWithHistoryHandler("sphere"+sphereID, sphereID);
		}		
	});
	}
	
	public static void openPublicSphere(final String sphereID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			
			openPublicSphereWithHistoryHandler("PublicSphere"+sphereID, sphereID);
		}		
	});
	}
	

	public static void openAnalytics()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			AnalyticsHome analytics = new AnalyticsHome();
			openWithHistoryHandler("analytics_home", analytics);
		}		
	});
	}

	
	public static void openAnalyticsForDataTrail(final String sphereID, final String endpointID, final String propertyID, final String endpointName, final String propertyName, final boolean sensor)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			//AnalyticsHome analytics = new AnalyticsHome();
			
			AnalyticsHome analytics = new AnalyticsHome(sphereID, endpointID, propertyID, endpointName, propertyName, sensor);
			openWithHistoryHandler("analytics_home", analytics);
		}		
	});
	}

	
	
	
	public static void openDirectLink(final String URL)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			
			Window.open(URL, "", "");
		}		
	});
	}
	
	public static void openProvisioningWizard()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			ProvisioningWizard provWizard = new ProvisioningWizard();
			openWithHistoryHandler("provisioning_wizard", provWizard);
		}		
	});
	}

	public static void openTakeOwnership(final String uEPID, final String identifier, final String endpointclassID, final String endpointclassName)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			
			openTakeDeviceOwnershipWithHistoryHandler("take_ownership", uEPID, identifier, endpointclassID, endpointclassName);
		}		
	});
	}

	public static void openProvisioningSuccess()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			ProvisioningSuccess provSuc = new ProvisioningSuccess();
			openWithHistoryHandler("prov_success", provSuc);
		}		
	});
	}
	
	public static void openEPCManager(final String successMessage)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			EditEPC epcManager = new EditEPC();
			openWithHistoryHandler("epc_manager", epcManager);
			epcManager.setSuccess(successMessage);
		}		
	});
	}
	
	public static void openEPCCreator()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			CreateEPC epcCreator = new CreateEPC();
			openWithHistoryHandler("epc_creator", epcCreator);
		}		
	});
	}
	
	public static void openEPCDeleter()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			RetireEPC epcDeleter = new RetireEPC();
			openWithHistoryHandler("epc_retire", epcDeleter);
		}		
	});
	}
	

	
	public static void openPropertyClassManager(final String successMessage)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			EditPropertyClass propertyClassManager = new EditPropertyClass();
			openWithHistoryHandler("prop_class_manager", propertyClassManager);
			propertyClassManager.setSuccess(successMessage);
		}		
	});
	}
	
	public static void openPropertyClassCreator()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			CreatePropertyClass propertyClassCreator = new CreatePropertyClass();
			openWithHistoryHandler("prop_class_creator", propertyClassCreator);
		}		
	});
	}
	
	public static void openVendorManager()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			CreateVendor vendorManager = new CreateVendor();
			openWithHistoryHandler("vendor_manager", vendorManager);
		}		
	});
	}
	
	public static void openPlanManager()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			ManagePlan planManager = new ManagePlan();
			openWithHistoryHandler("plan_manager", planManager);
		}		
	});
	}
	
	public static void openPopUp(final Widget w)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			PopupPanel popup = new PopupPanel();
			popup.setWidget(w);
			popup.center();
			
		}		
	});
	}
	
	
	public static void openEndpoint(final String endpointID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			
			openEndpointWithHistoryHandler("view_endpoint_"+endpointID, endpointID);
		}		
	});
	}
	
	public static void openEndpointPublic(final String endpointID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			
			openEndpointPublicWithHistoryHandler("view_endpoint_public"+endpointID, endpointID);
		}		
	});
	}
	
	public static void openEndpoint(final String endpointID, final int viewMode)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			
			openEndpointViewModeWithHistoryHandler("view_endpoint_"+endpointID+"_mode_"+viewMode, endpointID, viewMode);
		}		
	});
	}

	
	public static void openEPCInput(final String id, final String message)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			openEPCInputWithHistoryHandler("edit_epc_"+id, id, message);
		}		
	});
	}
	
	
	public static void openEPCAddProperty(final String EPCId)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			openEPCAddPropertyWithHistoryHandler("add_property_to_epc_"+EPCId, EPCId);
		}		
	});
	}
	
	public static void openGeoLocator()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			Locator geoLocator = new Locator();
			openWithHistoryHandler("geo_locator", geoLocator);
		}		
	});
	}
		
	public static void openMarket()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			MarketPlace marketPlace = new MarketPlace();
			openWithHistoryHandler("marketplace", marketPlace);
		}		
	});
	}
		
	public static void openGettingStarted()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			GettingStarted gettingStarted = new GettingStarted();
			openWithHistoryHandler("gettingStarted", gettingStarted);
		}		
	});
	}

	public static void openBestPractices()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			BestPractices bestPractices = new BestPractices();
			openWithHistoryHandler("bestPractices", bestPractices);
		}		
	});
	}

	public static void openChangeAccount()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			ChangeAccount changeAccount = new ChangeAccount();
			openWithHistoryHandler("changeAccount", changeAccount);
		}		
	});
	}

	public static void openCloseAccountConfirm()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//String userID = SessionHelper.getCurrentUserID();
			CloseAccountConfirm closeAccountConfirm = new CloseAccountConfirm();
			openWithHistoryHandler("closeAccountConfirm", closeAccountConfirm);
		}		
	});
	}


}
