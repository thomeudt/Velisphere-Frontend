package com.velisphere.tigerspice.client.locator;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.locator.maps.HeatMapLayerWidget;
import com.velisphere.tigerspice.client.locator.maps.InfoWindowMapWidget;


public class GeoLocatorWidget extends Composite {
	
	private FlowPanel vp;
	
	
	
	public GeoLocatorWidget(){
		loadMapApi();
		vp = new FlowPanel();
		initWidget(vp);
		
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
	    
	    
	    drawBasicMap();
	  

	  }

	  /**
	   * Add the widget to the demos
	   * 
	   *
	   */
	  private void addMapWidget(Widget widget) {
	   vp.add(widget);
		//  RootPanel.get().add(widget);
	  }

	  private void drawBasicMap() {
		   InfoWindowMapWidget wMap = new InfoWindowMapWidget();
		  //HeatMapLayerWidget wMap = new HeatMapLayerWidget();
		    addMapWidget(wMap);
		    
		  }
	 

}
