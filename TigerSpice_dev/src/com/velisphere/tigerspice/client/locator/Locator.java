package com.velisphere.tigerspice.client.locator;

import java.util.ArrayList;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.TabLink;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.analytics.LogService;
import com.velisphere.tigerspice.client.analytics.LogServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointActorWidget;
import com.velisphere.tigerspice.client.event.CheckpathCalculatedEvent;
import com.velisphere.tigerspice.client.event.CheckpathCalculatedEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.locator.maps.HeatMapLayerWidget;
import com.velisphere.tigerspice.client.locator.maps.InfoWindowMapWidget;
import com.velisphere.tigerspice.client.locator.maps.LogicalMapWidget;
import com.velisphere.tigerspice.client.locator.maps.PolylineMapWidget;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class Locator extends Composite {

	
	String userID;
	@UiField
	Breadcrumbs brdMain;
	@UiField
	TabPanel tabPanel;
	@UiField
	PageHeader pageHeader;
	@UiField
	Column colMap;
	@UiField
	TabLink tblGeoLocation;
	@UiField
	TabPane tbpMap;
	@UiField
	TabLink tblHeatMap;
	@UiField
	TabLink tblTrailMap;
	@UiField
	TabLink tblConnectionMap;

	HandlerRegistration mapCompleted;
	
	
	NavLink bread0;
	NavLink bread1;

	private static GeoLocatorUiBinder uiBinder = GWT
			.create(GeoLocatorUiBinder.class);

	interface GeoLocatorUiBinder extends UiBinder<Widget, Locator> {
	}

	public Locator() {
		
		loadMapApi();
		this.userID = SessionHelper.getCurrentUserID();
			
		initWidget(uiBinder.createAndBindUi(this));
			
		loadContent();
		
		
		
	}
	
	
	
	public void loadContent() {

		// set page header welcome back message
		pageHeader.setText("VeliSphere Location Service");

		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Locator");
		brdMain.add(bread1);
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

			AppController.openHome();

			}

		});
			
		
	}
	
	
	private void loadMapApi() {
		boolean sensor = true;

		// load all the libs for use in the maps
		ArrayList<LoadLibrary> loadLibraries = new ArrayList<LoadApi.LoadLibrary>();
		loadLibraries.add(LoadLibrary.ADSENSE);
		loadLibraries.add(LoadLibrary.DRAWING);
		loadLibraries.add(LoadLibrary.GEOMETRY);
		loadLibraries.add(LoadLibrary.PANORAMIO);
		loadLibraries.add(LoadLibrary.PLACES);
		loadLibraries.add(LoadLibrary.WEATHER);
		loadLibraries.add(LoadLibrary.VISUALIZATION);

		Runnable onLoad = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};

		LoadApi.go(onLoad, loadLibraries, sensor);
	}
	

	/**
	 * See the map widgets for different map configurations
	 */
	private void draw() {

		tblGeoLocation.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	    tbpMap.clear();
	        	  	InfoWindowMapWidget gMap = new InfoWindowMapWidget();
	        		tbpMap.add(gMap);
	              
	          }
	      });
		
		tblHeatMap.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpMap.clear();
	        	  HeatMapLayerWidget gMap = new HeatMapLayerWidget();
	        		tbpMap.add(gMap);
	              
	          }
	      });
		
		tblTrailMap.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpMap.clear();
	        	  PolylineMapWidget gMap = new PolylineMapWidget();
	        		tbpMap.add(gMap);
	              
	          }
	      });
		
		tblConnectionMap.addClickHandler(new ClickHandler() {
	          public void onClick(ClickEvent event) {
	        	  tbpMap.clear();
	        	  LogicalMapWidget gMap = new LogicalMapWidget();
	        		tbpMap.add(gMap);
	              
	          }
	      });
		
		// load default
		
		InfoWindowMapWidget gMap = new InfoWindowMapWidget();
		tbpMap.add(gMap);
	
		tabPanel.selectTab(0);
		
		tblGeoLocation.setActive(true);
		
		
		/**
		
		mapCompleted = EventUtils.EVENT_BUS.addHandler(MapCompletedEvent.TYPE, new MapCompletedEventHandler()     {
	        @Override
	        public void onMapCompleted(MapCompletedEvent mapCompletedEvent) {
	    		//tbpGeoLocation.setActive(false);
	    		tbpHeatmap.setActive(true);	        	
	    		HeatMapLayerWidget hMap = new HeatMapLayerWidget();
	    		tbpHeatmap.add(hMap);
	    		mapCompleted.removeHandler();
	    		//tbpHeatmap.setActive(false);
	    		//tbpGeoLocation.setActive(true);
	    		
	    		        
	        }
	    });
		
		**/
		
	}
	
	
	
	
	


	

}
