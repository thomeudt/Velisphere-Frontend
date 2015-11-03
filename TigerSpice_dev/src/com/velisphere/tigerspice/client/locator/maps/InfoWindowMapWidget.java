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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.geolocation.client.Geolocation;
import com.google.gwt.geolocation.client.Geolocation.PositionOptions;
import com.google.gwt.geolocation.client.Position;
import com.google.gwt.geolocation.client.PositionError;
import com.google.gwt.maps.client.LoadApi;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.LoadApi.LoadLibrary;
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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.FilterAppliedEvent;
import com.velisphere.tigerspice.client.event.FilterAppliedEventHandler;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.widgets.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.client.locator.helpers.GeoDataForMap;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.GeoLocationData;

public class InfoWindowMapWidget extends Composite {

  private HorizontalPanel pWidget;
  private MapWidget mapWidget;
  //private HashMap<String, GeoDataForMap> allGeoDataForMap;
  private LinkedList<GeoLocationData> allGeoDataForMap;
  

  
  // general constructor to display all endpoints 
  public InfoWindowMapWidget() {
    
	
	pWidget = new HorizontalPanel();
    initWidget(pWidget);
    //this.addStyleName("span8");
   
    
    setFilterHandlerListener();
    draw();
    
  }
  
  // constructor for calls from endpoint management view 
  
  public InfoWindowMapWidget(final String endpointID) {
	
	  		
	  		pWidget = new HorizontalPanel();
	  		initWidget(pWidget);
	    
	  
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
					getMarkersForMapSingleEndpoint(endpointID);
				}
			};

			LoadApi.go(onLoad, loadLibraries, sensor);
		
	  }
  
  private void draw() {
    pWidget.clear();
    
    FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget();
    pWidget.add(endpointFilter);
  
    getMarkersForMap();
  
    
  }

  
  private void setFilterHandlerListener()
  {
	  HandlerRegistration applyFilterHandler;
	  applyFilterHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(FilterAppliedEvent.TYPE, new FilterAppliedEventHandler()     {
			@Override
		    public void onFilterApplied(FilterAppliedEvent filterAppliedEvent) {
				HTML html = new HTML("FILTER APPLIED. Sphere ID: "+ filterAppliedEvent.getSphereID() + ", Endpoint ID: " + filterAppliedEvent.getEndpointID());
			    RootPanel.get().add(html);
				//applyFilterHandler.removeHandler();
			    pWidget.clear();
			    FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget(filterAppliedEvent.getSphereID(), filterAppliedEvent.getEndpointID());
			    pWidget.add(endpointFilter);
			    
			    
			    if (filterAppliedEvent.getEndpointID() != "0"){
			    	getMarkersForMapSingleEndpoint(filterAppliedEvent.getEndpointID());
			    	
			    	
			    } else if (filterAppliedEvent.getSphereID() != "0")
			    		
			    {
			    	getMarkersForMapSphere(filterAppliedEvent.getSphereID());		
			    	
			    }
			    
			    else getMarkersForMap(); 
			    	
			    				
			}		
		});
  }

  
  private void drawMarker( LinkedList<GeoLocationData> geoLocations) {
	  
		Iterator<GeoLocationData> it = geoLocations.iterator();
		while (it.hasNext()) {
			final GeoLocationData point = it.next();
	        //Map.Entry<String, GeoDataForMap> locationPair = (Map.Entry<String, GeoDataForMap>)it.next();
	        //final GeoDataForMap geoDataForMap = locationPair.getValue();
	        //drawMarker(Double.valueOf(geoDataForMap.getLat()), Double.valueOf(geoDataForMap.getLon()));
	        //drawMarker(-76.3, 37.08);
	   
	        
	        LatLng markerPosition = LatLng.newInstance(Double.parseDouble(point.getValue().substring(point.getValue().indexOf("{") + 1, point.getValue().indexOf("}"))), Double.parseDouble(point.getValue().substring(point.getValue().indexOf("[") + 1, point.getValue().indexOf("]"))));
		    MarkerOptions options = MarkerOptions.newInstance();
		    options.setPosition(markerPosition);
		    options.setTitle("Hello World");

		    final Marker marker = Marker.newInstance(options);
		    marker.setMap(mapWidget);

		    marker.addClickHandler(new ClickMapHandler() {
		      public void onEvent(ClickMapEvent event) {
		        drawInfoWindow(marker, event.getMouseEvent(), point);
		      }
		    });

	        
	        
	        
	        
	        
	        
	        
	        it.remove();
		}
	  
       	  
	  
	  	  
      }

  
 

  
  protected void drawInfoWindow(final Marker marker, MouseEvent mouseEvent, final GeoLocationData point) {
    if (marker == null || mouseEvent == null) {
      return;
    }
    
    
    
    
    
    
    SafeHtmlBuilder endpointInfoBuilder = new SafeHtmlBuilder();
    endpointInfoBuilder.appendHtmlConstant("<b>");
    endpointInfoBuilder.appendEscaped(point.getEndpointName());
    endpointInfoBuilder.appendHtmlConstant("</b><br>Latitude: ");
    endpointInfoBuilder.appendEscaped(point.getValue().substring(point.getValue().indexOf("{") + 1, point.getValue().indexOf("}")));
    endpointInfoBuilder.appendHtmlConstant("&deg;<br>Longitude ");
    endpointInfoBuilder.appendEscaped(point.getValue().substring(point.getValue().indexOf("[") + 1, point.getValue().indexOf("]")));
    endpointInfoBuilder.appendHtmlConstant("&deg; <br>Last position update: ");
    endpointInfoBuilder.appendEscaped(point.getTimeStamp());
    //Timestamp.valueOf(geoDataForMap.getTimeStamp());

   
    HTML html = new HTML(endpointInfoBuilder.toSafeHtml());
        
    
    Button b1 = new Button("Manage " + point.getEndpointName());
    b1.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
    	  AppController.openEndpoint(point.getEndpointID());
      }
    });

    Button b2 = new Button("Analyze");
    b2.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
    	  AppController.openAnalytics();
      }
    });

    Image epcImage = new Image();
    VerticalPanel vp = new VerticalPanel();
        
    vp.add(epcImage);
    vp.add(html);
    vp.add(b1);
    vp.add(b2);
    
    loadClassImage (epcImage, point.getEndpointClassID());

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
    
    AbsolutePanel aPanel = new AbsolutePanel();
    //aPanel.setSize("100%","350px");
    aPanel.setHeight("350px");
    aPanel.addStyleName("span8");
    mapWidget.setSize("100%", "100%");
    aPanel.add(mapWidget);
    
    
    pWidget.add(aPanel);
    
    //mapWidget.setSize(this.getOffsetWidth()-10+"px", "350px");
    
    //mapWidget.addStyleName("span4");
    mapWidget.setStyleName("map_canvas");
    //mapWidget.setWidth("100%");
    
    
    
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
		allGeoDataForMap = new LinkedList<GeoLocationData>();

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getAllGeoLocations(SessionHelper.getCurrentUserID(),
				new AsyncCallback<LinkedList<GeoLocationData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<GeoLocationData> result) {
						// TODO Auto-generated method stub

					
						allGeoDataForMap = result;

						Geolocation geolocation = Geolocation.getIfSupported();						
						   
						  geolocation.getCurrentPosition(new Callback<Position, PositionError>()
						  {
						    
						   @Override
						   public void onSuccess(Position result)
						   {
							   
							   drawMap(LatLng.newInstance(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude()));
							   drawMarker(allGeoDataForMap);
							   
						   }
						    
						   @Override
						   public void onFailure(PositionError reason)
						   {
							   
						    Window.alert(reason.getMessage());
						   }
						  });
					    
						
						
						
						
					}

				});

	}
  
  private void getMarkersForMapSingleEndpoint(String endpointID) {


		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getGeoLocationSingleEndpoint(SessionHelper.getCurrentUserID(), endpointID, 
				new AsyncCallback<LinkedList<GeoLocationData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println(caught);
					}

					@Override
					public void onSuccess(LinkedList<GeoLocationData> result) {
						// TODO Auto-generated method stub

						// RootPanel.get().add(new HTML("Length of List" +
						// result.size()));
						
						if (result.size() == 0){
							pWidget.add(new HTML("<b>No location data found</b><br><br>"
									+ "Possible reasons are that your selected endpoint do not contain any geolocation,"
									+ "has not reported its dynamic geolocation yet or the data is temporarily not available."));
						} else
						{
							allGeoDataForMap = result;
							GeoLocationData point = result.getFirst();
							
							drawMap(LatLng.newInstance(Double.parseDouble(point.getValue().substring(point.getValue().indexOf("{") + 1, point.getValue().indexOf("}"))), Double.parseDouble(point.getValue().substring(point.getValue().indexOf("[") + 1, point.getValue().indexOf("]")))));
							   drawMarker(allGeoDataForMap);
						
						}

							   
														
						
					}

				});

	}

private void getMarkersForMapSphere(String sphereID) {


		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getGeoLocationSphere(SessionHelper.getCurrentUserID(), sphereID, 
				new AsyncCallback<LinkedList<GeoLocationData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println(caught);
					}

					@Override
					public void onSuccess(LinkedList<GeoLocationData> result) {
						// TODO Auto-generated method stub

						// RootPanel.get().add(new HTML("Length of List" +
						// result.size()));

						allGeoDataForMap = result;
						Geolocation geolocation = Geolocation.getIfSupported();						
						   
						  geolocation.getCurrentPosition(new Callback<Position, PositionError>()
						  {
						    
						   @Override
						   public void onSuccess(Position result)
						   {
							   
							   drawMap(LatLng.newInstance(result.getCoordinates().getLatitude(), result.getCoordinates().getLongitude()));
							   drawMarker(allGeoDataForMap);
							   
						   }
						    
						   @Override
						   public void onFailure(PositionError reason)
						   {
							   
						    Window.alert(reason.getMessage());
						   }
						  });
					    
					}

				});

	}


  
  
  
  private void loadClassImage(final Image epcImage, String epcId)
  {
		EPCServiceAsync epcService = GWT
				.create(EPCService.class);
		
		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Class");
	
		
		epcService
				.getEndpointClassForEndpointClassID(
						epcId,
						new AsyncCallback<EPCData>() {

							@Override
							public void onFailure(
									Throwable caught) {
								// TODO Auto-generated method
								// stub
								animationLoading.removeLoadAnimation();
							}

							@Override
							public void onSuccess(EPCData result) {
								// TODO Auto-generated method
								// stub

								
								epcImage.setUrl(result.endpointclassImageURL);
								epcImage.setWidth("125px");
								animationLoading.removeLoadAnimation();
																				
								
							}

						});
  }
  
  
  

}