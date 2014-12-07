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

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapEvent;
import com.google.gwt.maps.client.events.tiles.TilesLoadedMapHandler;
import com.google.gwt.maps.client.geometrylib.EncodingUtils;
import com.google.gwt.maps.client.layers.TransitLayer;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayer;
import com.google.gwt.maps.client.visualizationlib.HeatMapLayerOptions;
import com.google.gwt.maps.client.visualizationlib.WeightedLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.helper.FilterSphereEndpointWidget;
import com.velisphere.tigerspice.client.locator.helpers.GeoDataForMap;
import com.velisphere.tigerspice.shared.GeoLocationData;

/**
 * See <a href=
 * "https://developers.google.com/maps/documentation/javascript/layers#JSHeatMaps"
 * >HeatMapLayer API Doc</a>
 */
public class HeatMapLayerWidget extends Composite {

  private final HorizontalPanel pWidget;
  private MapWidget mapWidget;
  private HashMap<String, GeoDataForMap> allGeoDataForMap;

  public HeatMapLayerWidget() {
    pWidget = new HorizontalPanel();
    initWidget(pWidget);

    draw();
  }

  private void draw() {

    pWidget.clear();
    FilterSphereEndpointWidget endpointFilter = new FilterSphereEndpointWidget();
        
    
    pWidget.add(endpointFilter);
   
    getMarkersForMap();
    
    //drawMap();
  }

  private void drawMap() {
    // zoom out for the clouds
    LatLng center = LatLng.newInstance(40.74, -73.94);
    MapOptions opts = MapOptions.newInstance();
    opts.setZoom(11);
    opts.setCenter(center);
    opts.setMapTypeId(MapTypeId.TERRAIN);

    mapWidget = new MapWidget(opts);
    pWidget.add(mapWidget);
    mapWidget.setSize("700px", "350px");
    mapWidget.setStyleName("map_canvas");
    
    mapWidget.addTilesLoadedHandler(new TilesLoadedMapHandler() {
        public void onEvent(TilesLoadedMapEvent event) {
          // Load something after the tiles load
      	  
      	  mapWidget.triggerResize();
      	  mapWidget.setZoom(mapWidget.getZoom());
      	  
        }
      });
    
    // show transit layer
    TransitLayer transitLayer = TransitLayer.newInstance();
    transitLayer.setMap(mapWidget);

    // create layer
    HeatMapLayerOptions options = HeatMapLayerOptions.newInstance();
    options.setOpacity(0.9);
    options.setRadius(5);
    options.setGradient(getSampleGradient());
    options.setMaxIntensity(3);
    options.setMap(mapWidget);

    HeatMapLayer heatMapLayer = HeatMapLayer.newInstance(options);
    // set data
    JsArray<LatLng> dataPoints = compileHeatMapData();
    heatMapLayer.setData(dataPoints);

    // the other way to set data
    // note JS array can be set from this method, but only MVCArray from the
    // options setData() method
    // MVCArray<WeightedLocation> weightedDataPoints =
    // MVCArray.newInstance(getSampleWeightedData());
    // heatMapLayer.setDataWeighted(weightedDataPoints);
    GWT.log("Plotting " + dataPoints.length() + " points on HeatMap");
  }

  /**
   * Sample gradient from <a href=
   * "https://google-developers.appspot.com/maps/documentation/javascript/examples/layer-heatmap"
   * >Google Maps Example</a>
   * 
   * @return
   */
  private JsArrayString getSampleGradient() {
    String[] sampleColors = new String[] { "rgba(0, 255, 255, 0)", "rgba(0, 255, 255, 1)", "rgba(0, 191, 255, 1)",
        "rgba(0, 127, 255, 1)", "rgba(0, 63, 255, 1)", "rgba(0, 0, 255, 1)", "rgba(0, 0, 223, 1)",
        "rgba(0, 0, 191, 1)", "rgba(0, 0, 159, 1)", "rgba(0, 0, 127, 1)", "rgba(63, 0, 91, 1)", "rgba(127, 0, 63, 1)",
        "rgba(191, 0, 31, 1)", "rgba(255, 0, 0, 1)" };
    return ArrayHelper.toJsArrayString(sampleColors);
  }

  
  
  private void getMarkersForMap() {
		allGeoDataForMap = new HashMap<String, GeoDataForMap>();

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getAllGeoLocationTrails(SessionHelper.getCurrentUserID(),
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

							
							if (allGeoDataForMap.containsKey(geoFromServer
									.getEndpointID())) {
								GeoDataForMap geoDataPoint = allGeoDataForMap
										.get(geoFromServer.getEndpointID());
								
								if (geoFromServer.getPropertyClassID() == "PC_GEO_LON") {
									geoDataPoint.setLon(geoFromServer
											.getValue());
									
							
								} else {
									geoDataPoint.setLat(geoFromServer
											.getValue());
									
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
								geoDataPoint.setEndpointClassID(geoFromServer
										.getEndpointClassID());
								geoDataPoint.setTimeStamp(geoFromServer.getTimeStamp());
								if (geoFromServer.getPropertyClassID() == "PC_GEO_LON") {
									geoDataPoint.setLon(geoFromServer
											.getValue());
								} else {
									geoDataPoint.setLat(geoFromServer
											.getValue());
															}

								allGeoDataForMap.put(
										geoFromServer.getEndpointID(),
										geoDataPoint);

							}

						}

						compileHeatMapData();
						drawMap();
						
					}

				});

	}
  
  
  
  
  /**
   * Sample spatial data from <a href=
   * "https://google-developers.appspot.com/maps/documentation/javascript/examples/layer-heatmap"
   * >Google Maps Example</a>
   * 
   * @return
   */
  private JsArray<LatLng> compileHeatMapData() {
    
	JsArray<LatLng> allPoints = JavaScriptObject.createArray().cast();
	Iterator<Entry<String, GeoDataForMap>> it = allGeoDataForMap.entrySet().iterator();
	while(it.hasNext()){
	
		Map.Entry<String, GeoDataForMap> pair = (Map.Entry<String, GeoDataForMap>)it.next();
		LatLng point = LatLng.newInstance(Double.parseDouble(pair.getValue().getLat()), Double.parseDouble(pair.getValue().getLon()));
		
		
		allPoints.push(point);
				
	}
    
	
	return allPoints;
  }

  /**
   * Get (randomly_ weighted spatial data for use in tests
   * 
   * @return
   */
  @SuppressWarnings("unused")
  // here as an example of WeightedDataPoints
  private JsArray<WeightedLocation> getSampleWeightedData() {
    JsArray<LatLng> samplePoints = compileHeatMapData();
    JsArray<WeightedLocation> sampleLocations = ArrayHelper.toJsArray(new WeightedLocation[] {});
    for (int n = 0, len = samplePoints.length(); n < len; n++) {
      sampleLocations.push(WeightedLocation.newInstance(samplePoints.get(n), 10 * Math.random()));
    }
    return sampleLocations;
  }

}