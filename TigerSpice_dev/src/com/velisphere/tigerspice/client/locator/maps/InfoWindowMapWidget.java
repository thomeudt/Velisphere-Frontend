package com.velisphere.tigerspice.client.locator.maps;

/*
 * #%L
 * GWT Maps API V3 - Showcase
 * %%
 * Copyright (C) 2011 - 2012 GWT Maps API V3
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Geolocation.PositionOptions;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.controls.ScaleControlOptions;
import com.google.gwt.maps.client.events.MouseEvent;
import com.google.gwt.maps.client.events.click.ClickMapEvent;
import com.google.gwt.maps.client.events.click.ClickMapHandler;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapEvent;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapHandler;
import com.google.gwt.maps.client.overlays.InfoWindow;
import com.google.gwt.maps.client.overlays.InfoWindowOptions;
import com.google.gwt.maps.client.overlays.Marker;
import com.google.gwt.maps.client.overlays.MarkerOptions;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.locator.GeoDataForMap;
import com.velisphere.tigerspice.shared.GeoLocationData;

public class InfoWindowMapWidget extends Composite {

  private VerticalPanel pWidget;
  private MapWidget mapWidget;
  private HashMap<String, GeoDataForMap> allGeoDataForMap;

  public InfoWindowMapWidget() {
    
	pWidget = new VerticalPanel();
    initWidget(pWidget);

    draw();
    
  }

  private void draw() {
    pWidget.clear();
    
    Geolocation geolocation = Geolocation.getIfSupported();
	
	   
	  geolocation.getCurrentPosition(new Callback<Position, PositionError>()
	  {
	    
	   @Override
	   public void onSuccess(Position result)
	   {
		   
		   drawMap(LatLng.newInstance(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude()));
		   getMarkersForMap();
		   drawInfoWindowOnMapCenter();
	   }
	    
	   @Override
	   public void onFailure(PositionError reason)
	   {
		   
	    Window.alert(reason.getMessage());
	   }
	  });
    
    
    
   
    
  }

  
  private void drawMarker( HashMap<String, GeoDataForMap> geoLocations) {
	  
	    HTML html = new HTML("LAT ... ... " + geoLocations.size());
	    RootPanel.get().add(html);
		Iterator<Entry<String, GeoDataForMap>> it = geoLocations.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, GeoDataForMap> locationPair = (Map.Entry<String, GeoDataForMap>)it.next();
	        final GeoDataForMap geoDataForMap = locationPair.getValue();
	        html = new HTML("LAT ... ... " + geoDataForMap.getLat());
	        RootPanel.get().add(html);
	        //drawMarker(Double.valueOf(geoDataForMap.getLat()), Double.valueOf(geoDataForMap.getLon()));
	        //drawMarker(-76.3, 37.08);
	   
	        
	        LatLng center = LatLng.newInstance(Double.valueOf(geoDataForMap.getLat()), Double.valueOf(geoDataForMap.getLon()));
		    MarkerOptions options = MarkerOptions.newInstance();
		    options.setPosition(center);
		    options.setTitle("Hello World");

		    final Marker marker = Marker.newInstance(options);
		    marker.setMap(mapWidget);

		    marker.addClickHandler(new ClickMapHandler() {
		      public void onEvent(ClickMapEvent event) {
		        drawInfoWindow(marker, event.getMouseEvent(), geoDataForMap.getEndpointName());
		      }
		    });

	        
	        
	        
	        
	        
	        
	        
	        it.remove();
		}
	  
       	  
	  
	  	  
      }

  
  private void drawInfoWindowOnMapCenter() {
	    HTML html = new HTML("Center: " + mapWidget.getCenter().getToString());

	    InfoWindowOptions options = InfoWindowOptions.newInstance();
	    options.setContent(html);
	    options.setPosition(mapWidget.getCenter());

	    InfoWindow iw = InfoWindow.newInstance(options);
	    iw.open(mapWidget);
	  }

  
  protected void drawInfoWindow(final Marker marker, MouseEvent mouseEvent, String endpointName) {
    if (marker == null || mouseEvent == null) {
      return;
    }

    HTML html = new HTML("Why did you click on me? <br/> This is: " + endpointName + " at " + mouseEvent.getLatLng().getToString());

    Button b1 = new Button("b1");
    b1.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert("b1 clicked");
      }
    });

    Button b2 = new Button("b2");
    b2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Window.alert("b2 clicked");
      }
    });

    VerticalPanel vp = new VerticalPanel();
    vp.add(html);
    vp.add(b1);
    vp.add(b2);

    InfoWindowOptions options = InfoWindowOptions.newInstance();
    options.setContent(vp);

    InfoWindow iw = InfoWindow.newInstance(options);
    iw.open(mapWidget, marker);

    // If you want to clear widgets, Use options.clear() to remove the widgets
    // from map
    // options.clear();
  }

  private void drawMap(LatLng center) {
	 	 
    MapOptions opts = MapOptions.newInstance();
    opts.setZoom(4);
    opts.setScaleControl(true);
    opts.setZoomControl(true);
    opts.setCenter(center);
    opts.setMapTypeId(MapTypeId.HYBRID);

    mapWidget = new MapWidget(opts);
    pWidget.add(mapWidget);
    
    mapWidget.setSize("700px", "350px");
    mapWidget.setStyleName("map_canvas");
    
    
    
    mapWidget.addClickHandler(new ClickMapHandler() {
      public void onEvent(ClickMapEvent event) {
        GWT.log("clicked on latlng=" + event.getMouseEvent().getLatLng());
      }
    });

    mapWidget.addTilesLoadedHandler(new TilesLoadedMapHandler() {
      public void onEvent(TilesLoadedMapEvent event) {
        // Load something after the tiles load
    	  
    	  mapWidget.triggerResize();
    	  mapWidget.setZoom(mapWidget.getZoom());
    	  
      }
    });
  }
  
  
  private void getMarkersForMap() {
		allGeoDataForMap = new HashMap<String, GeoDataForMap>();

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getAllGeoLocations(SessionHelper.getCurrentUserID(),
				new AsyncCallback<LinkedList<GeoLocationData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println(caught);
					}

					@Override
					public void onSuccess(LinkedList<GeoLocationData> result) {
						// TODO Auto-generated method stub

						Iterator<GeoLocationData> it = result.iterator();
						// RootPanel.get().add(new HTML("Length of List" +
						// result.size()));

						while (it.hasNext()) {

							GeoLocationData geoFromServer = new GeoLocationData();

							geoFromServer = it.next();

							RootPanel
									.get()
									.add(new HTML("Just iterated... "
											+ geoFromServer
													.getPropertyClassID()
											+ "... " + geoFromServer.getValue()));

							if (allGeoDataForMap.containsKey(geoFromServer
									.getEndpointID())) {
								GeoDataForMap geoDataPoint = allGeoDataForMap
										.get(geoFromServer.getEndpointID());
								if (geoFromServer.getPropertyClassID() == "PC_GEO_LON") {
									geoDataPoint.setLon(geoFromServer
											.getValue());
									RootPanel
											.get()
											.add(new HTML(
													geoFromServer
															.getPropertyClassID()
															+ "Added Lon to existing Lat"
															+ geoFromServer
																	.getValue()));

								} else {
									geoDataPoint.setLat(geoFromServer
											.getValue());
									RootPanel
											.get()
											.add(new HTML(
													geoFromServer
															.getPropertyClassID()
															+ "Added Lat to existing Lon"
															+ geoFromServer
																	.getValue()));
								}
								allGeoDataForMap.put(
										geoFromServer.getEndpointID(),
										geoDataPoint);

							} else {

								GeoDataForMap geoDataPoint = new GeoDataForMap();
								geoDataPoint.setEndpointID(geoFromServer
										.getEndpointID());
								geoDataPoint.setEndpointName(geoFromServer
										.getEndpointName());
								if (geoFromServer.getPropertyClassID() == "PC_GEO_LON") {
									geoDataPoint.setLon(geoFromServer
											.getValue());
									RootPanel
											.get()
											.add(new HTML(geoFromServer
													.getPropertyClassID()
													+ "Added new Lon"
													+ geoFromServer.getValue()));
								} else {
									geoDataPoint.setLat(geoFromServer
											.getValue());
									RootPanel
											.get()
											.add(new HTML(geoFromServer
													.getPropertyClassID()
													+ "Added new Lat"
													+ geoFromServer.getValue()));
								}

								allGeoDataForMap.put(
										geoFromServer.getEndpointID(),
										geoDataPoint);

							}

						}

						drawMarker(allGeoDataForMap);
						
					}

				});

	}

}