package com.velisphere.tigerspice.client.dash;

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.analytics.SimpleLineChart;
import com.velisphere.tigerspice.shared.AnalyticsRawData;

public class GaugeBox extends Composite {
	

	VerticalPanel panel;
	
	public GaugeBox()
	{
		panel = new VerticalPanel();
		
		initWidget(panel);
		panel.add(new HTML("Widget"));
		
		Button button = new Button("Configure");
		button.setSize(ButtonSize.MINI);
		button.setType(ButtonType.PRIMARY);
		VisualizationUtils.loadVisualizationApi(onLoadCallback, Gauge.PACKAGE);

		panel.add(button);
		
		
	}
	
		
		private Runnable onLoadCallback = new Runnable() {
			public void run() {
				DataTable data = DataTable.create();

			     data.addColumn(ColumnType.STRING, "Label");
			     data.addColumn(ColumnType.NUMBER, "Value");
			     data.addRows(1);
			     data.setValue(0, 0, "CPU(%)");
			     data.setValue(0, 1, 50);
			     Gauge.Options option = Gauge.Options.create();

			     option.setHeight(180);
			     option.setWidth(180);
			     option.setGreenRange(71, 80);
			     option.setMinorTicks(10);
			     option.setRedRange(81, 100);
			     option.setYellowRange(61, 70);
			     
			     panel.add(new Gauge(data, option));

			}
		};
		

		
		
		
		
			
	
}
