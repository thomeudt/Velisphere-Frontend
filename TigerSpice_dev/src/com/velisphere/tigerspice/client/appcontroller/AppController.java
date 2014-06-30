package com.velisphere.tigerspice.client.appcontroller;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.AnalyticsHome;
import com.velisphere.tigerspice.client.analytics.ChartSensorHistoryWidget;
import com.velisphere.tigerspice.client.dashboard.Dashboard;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.event.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.provisioning.ProvisioningWizard;
import com.velisphere.tigerspice.client.rules.CheckpathCreateView;
import com.velisphere.tigerspice.client.rules.CheckpathEditView;
import com.velisphere.tigerspice.client.rules.CheckpathList;
import com.velisphere.tigerspice.client.spheres.SphereLister;
import com.velisphere.tigerspice.client.spheres.SphereView;
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

	
	static void openCheckpathWithHistoryHandler(final String token, final String checkpathID, final String userID)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add((Widget) new CheckpathEditView(checkpathID, userID));
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add((Widget) new CheckpathEditView(checkpathID, userID));
		        }
			}
		    });
	}


	static void openSphereWithHistoryHandler(final String token, final String sphereId, final String sphereName)
	{
		History.newItem(token);
		RootPanel.get("main").clear();
		RootPanel.get("main").add((Widget) new SphereView(sphereId, sphereName));
				
		History.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {

				String historyToken = event.getValue();
				
		        // Parse the history token

	          if (historyToken.equals(token)) {
	        	  RootPanel.get("main").clear();
	        	  RootPanel.get("main").add((Widget) new SphereView(sphereId, sphereName));
		        }
			}
		    });
	}

	
	

	public static void openDashboard()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			Dashboard dashboard = new Dashboard();
			openWithHistoryHandler("dashboard", dashboard);
		}		
	});
	}
	
	public static void openHome()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			LoginSuccess loginSuccess = new LoginSuccess();
			openWithHistoryHandler("home", loginSuccess);
		}		
	});
	}

	public static void openLogicDesigner()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			CheckpathList checkpathList = new CheckpathList(userID);
			openWithHistoryHandler("logicdesigner", checkpathList);
		}		
	});
	}

	
	public static void openLogicDesign(final String checkpathID)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			
			openCheckpathWithHistoryHandler("logicdesign"+checkpathID, checkpathID, userID);
		}		
	});
	}
	
	public static void createLogicDesign()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			
			RootPanel.get("main").clear();
			RootPanel.get("main").add((Widget) new CheckpathCreateView(userID));
		}		
	});
	}
	

	public static void openEndpointManager()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			//userID = SessionHelper.getCurrentUserID();
			SphereLister endpointManager = new SphereLister(); 	
			openWithHistoryHandler("endpointmgr", endpointManager);
		}		
	});
	}

	public static void openSphere(final String sphereID, final String sphereName)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			
			openSphereWithHistoryHandler("sphere"+sphereID, sphereID, sphereName);
		}		
	});
	}
	

	public static void openAnalytics()
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			AnalyticsHome analytics = new AnalyticsHome(userID);
			openWithHistoryHandler("analytics_home", analytics);
		}		
	});
	}

	public static void openDirectLink(final String URL)
	{	
		SessionHelper.validateCurrentSession();
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
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
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			sessionHandler.removeHandler();
			String userID = SessionHelper.getCurrentUserID();
			ProvisioningWizard provWizard = new ProvisioningWizard(userID);
			openWithHistoryHandler("provisioning_wizard", provWizard);
		}		
	});
	}


	
}
