/*
 * Copyright 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.gwt.visualization.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.visualization.client.events.OnMouseOutHandler;
import com.google.gwt.visualization.client.events.OnMouseOverHandler;
import com.google.gwt.visualization.client.visualizations.corechart.AreaChart;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;

/**
 * Tests for the AreaChart class.
 */
public class CoreAreaChartTest extends VisualizationTest {
  /**
   * This test case will try creating a simple Area chart. It first asserts that
   * the Visualization API has been correctly loaded (see ajax_loader.html).
   */
  public void testASimpleAreaChart() {
    loadApi(new Runnable() {
      public void run() {
        DataTable data = createDailyActivities();

        // Create a minimal area chart.
        Options options = Options.create();
        options.setWidth(400);
        options.setHeight(240);
        RootPanel.get().add(new AreaChart(data, options));
      }
    });
  }

  public void testOnMouseOver() {
    loadApi(new Runnable() {
      public void run() {
        Options options = Options.create();
        AreaChart chart = new AreaChart(createCompanyPerformance(), options);
        chart.addOnMouseOverHandler(new OnMouseOverHandler() {
          @Override
          public void onMouseOverEvent(OnMouseOverEvent event) {
            assertNotNull(event);
            assertEquals(1, event.getRow());
            assertEquals(1, event.getColumn());
            finishTest();
          }
        });
        triggerOnMouseOver(chart.getJso());
      }
    }, false);
  }

  public void testOnMouseOut() {
    loadApi(new Runnable() {
      public void run() {
        Options options = Options.create();
        AreaChart chart = new AreaChart(createCompanyPerformance(), options);
        chart.addOnMouseOutHandler(new OnMouseOutHandler() {
          @Override
          public void onMouseOutEvent(OnMouseOutEvent event) {
            assertNotNull(event);
            assertEquals(1, event.getRow());
            assertEquals(1, event.getColumn());
            finishTest();
          }
        });
        triggerOnMouseOut(chart.getJso());
      }
    }, false);
  }

  @Override
  protected String getVisualizationPackage() {
    return CoreChart.PACKAGE;
  }

  private native void triggerOnMouseOut(JavaScriptObject jso) /*-{
    $wnd.google.visualization.events.trigger(jso, 'onmouseout',
      {'row':1, 'column':1});
  }-*/;

  private native void triggerOnMouseOver(JavaScriptObject jso) /*-{
    $wnd.google.visualization.events.trigger(jso, 'onmouseover',
      {'row':1, 'column':1});
  }-*/;
}
