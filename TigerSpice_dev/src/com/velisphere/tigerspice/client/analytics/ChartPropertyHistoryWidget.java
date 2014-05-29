package com.velisphere.tigerspice.client.analytics;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.BarChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;

public class ChartPropertyHistoryWidget extends Composite {

	
	
	public ChartPropertyHistoryWidget() {
		//initWidget(uiBinder.createAndBindUi(this));
	

		 
		final VerticalPanel vp = new VerticalPanel();
          final Options options = Options.create();
          options.setHeight(800);
          options.setTitle("Endpoint Data Trail");
          options.setWidth(500);
         
          Runnable onLoadCallback = new Runnable() {
              public void run() {
                

                // Create a pie chart visualization.
                LineChart lines = new LineChart(createTable(), options);
                vp.add(lines);
              }
            };

            initWidget(vp);
            VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);
            
            

  }
    private AbstractDataTable createTable() {
          DataTable data = DataTable.create();
          data.addColumn(ColumnType.STRING, "Task");
          data.addColumn(ColumnType.NUMBER, "Hours per Day");
          data.addRows(2);
          data.setValue(0, 0, "Work");
          data.setValue(0, 1, 14);
          data.setValue(1, 0, "Sleep");
          data.setValue(1, 1, 10);
          return data;
        }
}
