package com.velisphere.tigerspice.client.analytics;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.velisphere.tigerspice.shared.EndpointLogData;

public class ChartPropertyHistoryWidget extends Composite {

	
	
	public ChartPropertyHistoryWidget() {
		//initWidget(uiBinder.createAndBindUi(this));
	

		 
		final VerticalPanel vp = new VerticalPanel();
		
		initWidget(vp);
		
		
		// setup main analyzer layout
		Row mainRow = new Row();
		Column leftCol = new Column(2);
		mainRow.add(leftCol);
		Column rightCol = new Column(8);
		mainRow.add(rightCol);
		vp.add(mainRow);

		// setup special analyzer layout
		
				Row filterRow1 = new Row();
				Column filterCol1 = new Column(2);
				Label lblSphereFilter = new Label("Sphere");
				filterCol1.add(lblSphereFilter);
				
				filterRow1.add(filterCol1);
							
				Row filterRow2 = new Row();
				Column filterCol2 = new Column(2);
				ListBox lbxSphereFilter = new ListBox();
				lbxSphereFilter.setSize(2);
				filterCol2.add(lbxSphereFilter);
				
				filterRow2.add(filterCol2);
				
				Row filterRow3 = new Row();
				Column filterCol3 = new Column(2);
				Label lblEndpointFilter = new Label("Endpoint");
				filterCol3.add(lblEndpointFilter);
				
				filterRow3.add(filterCol3);
							
				Row filterRow4 = new Row();
				Column filterCol4 = new Column(2);
				ListBox lbxEndpointFilter = new ListBox();
				lbxEndpointFilter.setSize(2);
				filterCol4.add(lbxEndpointFilter);
				
				filterRow4.add(filterCol4);
				
				Row filterRow5 = new Row();
				Column filterCol5 = new Column(2);
				Label lblPropertyFilter = new Label("Property");
				filterCol5.add(lblPropertyFilter);
				
				filterRow5.add(filterCol5);
							
				Row filterRow6 = new Row();
				Column filterCol6 = new Column(2);
				ListBox lbxPropertyFilter = new ListBox();
				lbxPropertyFilter.setSize(2);
				filterCol6.add(lbxPropertyFilter);
				
				filterRow6.add(filterCol6);
				
				Row filterRow7 = new Row();
				Column filterCol7 = new Column(2);
				Button btnDownload = new Button("Download"); 
				btnDownload.setSize(ButtonSize.SMALL);
				btnDownload.setType(ButtonType.PRIMARY);
				filterCol7.add(btnDownload);
				
				filterRow7.add(filterCol7);
				
				
				
				
				
				
				
				
				Row graphRow = new Row();
				final Column graphCol = new Column(8);
				graphRow.add(graphCol);
				
		leftCol.add(filterRow1);
		leftCol.add(filterRow2);
		leftCol.add(filterRow3);
		leftCol.add(filterRow4);
		leftCol.add(filterRow5);
		leftCol.add(filterRow6);
		leftCol.add(filterRow7);
		
		rightCol.add(graphRow);
		
		
		
				
          final Options options = Options.create();
          
          
          options.setTitle("Endpoint Data Trail");
          //options.setWidth(vp.getOffsetWidth());
          options.setHeight((int) (RootPanel.get().getOffsetHeight()*0.3));
          options.setFontName("Source Sans Pro");
          options.setLineWidth(2);
         
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
        	                graphCol.add(lines);

          		          
          		          
          				
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
