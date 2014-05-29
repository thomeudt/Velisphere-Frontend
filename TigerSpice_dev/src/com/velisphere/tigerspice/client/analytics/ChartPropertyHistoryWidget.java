package com.velisphere.tigerspice.client.analytics;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.velisphere.tigerspice.shared.EndpointLogData;

public class ChartPropertyHistoryWidget extends Composite {

	
	
	public ChartPropertyHistoryWidget() {
		//initWidget(uiBinder.createAndBindUi(this));
	

		 
		final VerticalPanel vp = new VerticalPanel();
		
		initWidget(vp);
		
		Row row = new Row();
		final Column col = new Column(8);
		row.add(col);
		vp.add(row);
		
		
          final Options options = Options.create();
          
          
          options.setTitle("Endpoint Data Trail");
          //options.setWidth(vp.getOffsetWidth());
          options.setHeight((int) (RootPanel.get().getOffsetHeight()*0.3));
         
          Runnable onLoadCallback = new Runnable() {
              public void run() {
        
            	  
            	  final AnalyticsServiceAsync analyticsService = GWT
          				.create(AnalyticsService.class);
          		  	   	

              	analyticsService.getEndpointLog("E1", "PR2", new AsyncCallback<LinkedList<EndpointLogData>>(){
              		@Override
              		public void onFailure(Throwable caught) {
              			// TODO Auto-generated method stub
              			
              		}

          			@Override
          			public void onSuccess(LinkedList<EndpointLogData> result) {
          				// TODO Auto-generated method stub
          		
          				DataTable data = DataTable.create();
          		          data.addColumn(ColumnType.STRING, "Time");
          		          data.addColumn(ColumnType.NUMBER, "Value");
          		          		          	          
          		          data.addRows(result.size()+1);
          		          
          		          
          		          int i = 1;
          		          Iterator<EndpointLogData> it = result.iterator();
          		          
          		          while (it.hasNext()){
          		        	  
          		        	  EndpointLogData logItem = it.next();
          		        	  data.setValue(i, 0, logItem.getTimeStamp());          			          
        			          data.setValue(i, 1, Integer.parseInt(logItem.getValue()));
        			          i = i +1;
          	                          		        	  
          		          }
          		          
          		          // Create a line chart visualization.
        	                LineChart lines = new LineChart(data, options);
        	                col.add(lines);

          		          
          		          
          				
          			}
              		});


              }
            };

            
            VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);
            
            

  }
    private AbstractDataTable createTable() {
          
    	    	
    	
    	return null;
    	
    	
    	
        }
}
