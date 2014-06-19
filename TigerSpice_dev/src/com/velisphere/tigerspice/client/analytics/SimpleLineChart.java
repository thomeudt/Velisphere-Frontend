package com.velisphere.tigerspice.client.analytics;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.shared.EndpointLogData;
import com.velisphere.tigerspice.shared.TableRowData;



public class SimpleLineChart {


	DataTable data;
	LinkedList<TableRowData> tableData;
	LineChart lines;
	Column graphCol;

	
	SimpleLineChart(String endpointID, String propertyID, final String endpointName, final String propertyName, final Timestamp startDate, final Timestamp endDate) {

		graphCol = new Column(8);
		
		// clear tableData before loading new data
		tableData = new LinkedList<TableRowData>();
		


		// container class for cell table

		class SensorTrail {
			private String timeStamp;
			private Double value;
			private Integer itemNumber;
		}

		final Options options = Options.create();

		options.setTitle("Sensor Data Trail for Sensor '" + propertyName
				+ "' on Endpoint '" + endpointName + "'");
		// options.setWidth(vp.getOffsetWidth());
		options.setHeight((int) (RootPanel.get().getOffsetHeight() * 0.3));
		options.setFontName("Source Sans Pro");
		options.setLineWidth(2);
		

		final AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		System.out.println(endpointID + " / "
				+ propertyID);
		
		analyticsService.getEndpointLog(endpointID,
				propertyID,
				new AsyncCallback<LinkedList<EndpointLogData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointLogData> result) {
						// TODO Auto-generated method stub

						// prep the datatable

						final CellTable<SensorTrail> table = new CellTable<SensorTrail>(10, GWT.<CellTable.SelectableResources>create(CellTable.SelectableResources.class));

						TextColumn<SensorTrail> timeStampColumn = new TextColumn<SensorTrail>() {
							@Override
							public String getValue(SensorTrail entry) {
								return entry.timeStamp;
							}
						};
						
						timeStampColumn.setSortable(true);

												
						com.google.gwt.user.cellview.client.Column<SensorTrail, Number> valueColumn = 
								new com.google.gwt.user.cellview.client.Column<SensorTrail, Number>(new NumberCell(NumberFormat.getFormat("#,##0.00;-#,##0.00"))) {
							@Override
							public Number getValue(SensorTrail entry) {
								return entry.value;
							}
						};
						
						valueColumn.setSortable(true);

						// Add the columns.
						table.addColumn(timeStampColumn, "Time Stamp");
						table.addColumn(valueColumn, propertyName);
						table.setRowCount(result.size(), true);
						//table.setVisibleRange(0, 10);

						// Create a data provider.
						ListDataProvider<SensorTrail> dataProvider = new ListDataProvider<SensorTrail>();

						// Connect the table to the data provider.
						dataProvider.addDataDisplay(table);
					

						// create the datatable for chart and celltable for
						// table

						data = DataTable.create();
						data.addColumn(ColumnType.STRING, "Time");
						data.addColumn(ColumnType.NUMBER, propertyName);

						data.addRows(result.size() + 1);
						
						

						int i = 1;
						Iterator<EndpointLogData> it = result.iterator();
						List<SensorTrail> list = dataProvider.getList();

						TableRowData introRow = new TableRowData();
						introRow.setRow("VeliSphere Data Trail Export for " + propertyName
								+ " on " + endpointName, "", "", "", "", "", "", "");
						
						tableData.add(introRow);
				
						TableRowData spacerRow = new TableRowData();
						spacerRow.setRow("---------------------------------------", "", "", "", "", "", "", "");
						tableData.add(spacerRow);
				
						
						TableRowData headerRow = new TableRowData();
						headerRow.setRow("Timestamp ", propertyName, "", "", "", "", "", "");
						tableData.add(headerRow);
						
						
						
						if (startDate != null && endDate != null)
						{
							
							// populate table with date filter applied
							while (it.hasNext()) {

								
								EndpointLogData logItem = it.next();
																						
								if(Timestamp.valueOf(logItem.getTimeStamp()).after(startDate) && Timestamp.valueOf(logItem.getTimeStamp()).before(endDate))
								{
								
								data.setValue(i, 0, logItem.getTimeStamp());
								data.setValue(i, 1,	logItem.getValue());
								
								TableRowData row = new TableRowData();
								row.setRow(logItem.getTimeStamp(),String.valueOf(logItem.getValue()), "", "", "", "", "", "");
								tableData.add(row);
							
							

								// Add the data to the data provider, which
								// automatically pushes it to the
								// widget.
								
								SensorTrail trailItem = new SensorTrail();
								trailItem.timeStamp = logItem.getTimeStamp();
								trailItem.value = logItem.getValue();
								trailItem.itemNumber = i;
								list.add(trailItem);
								}

								i = i + 1;
							}
							
						} else
						{
							// populate table without date filter applied
							while (it.hasNext()) {

								
								EndpointLogData logItem = it.next();
																						
								
								data.setValue(i, 0, logItem.getTimeStamp());
								data.setValue(i, 1,	logItem.getValue());
								
								TableRowData row = new TableRowData();
								row.setRow(logItem.getTimeStamp(),String.valueOf(logItem.getValue()), "", "", "", "", "", "");
								tableData.add(row);
							
							

								// Add the data to the data provider, which
								// automatically pushes it to the
								// widget.
								
								SensorTrail trailItem = new SensorTrail();
								trailItem.timeStamp = logItem.getTimeStamp();
								trailItem.value = logItem.getValue();
								trailItem.itemNumber = i;
								list.add(trailItem);
								

								i = i + 1;
							}
							
							
						}
						
						
						
						
						
						System.out.println(tableData.toString());
						
						// Create a line chart visualization.
						lines = new LineChart(data, options);
						graphCol.clear();
						
						graphCol.add(lines);
						
						


						
						
											
						
						// Add a ColumnSortEvent.ListHandler to connect sorting to the
					    // java.util.List.
					    ListHandler<SensorTrail> columnSortHandler = new ListHandler<SensorTrail>(list);
					    // Sort by TimeStamp
					    columnSortHandler.setComparator(timeStampColumn,
					        new Comparator<SensorTrail>() {
					          public int compare(SensorTrail o1, SensorTrail o2) {
					            if (o1 == o2) {
					              return 0;
					            }

					            // Compare the timestamp columns.
					            if (o1 != null) {
					              return (o2 != null) ? o1.timeStamp.compareTo(o2.timeStamp) : 1;
					            }
					            return -1;
					          }
					        });
					    
					    // Sort by Value
					    
					    columnSortHandler.setComparator(valueColumn,
						        new Comparator<SensorTrail>() {
						          public int compare(SensorTrail o1, SensorTrail o2) {
						            if (o1 == o2) {
						              return 0;
						            }

						            // Compare the timestamp columns.
						            if (o1 != null) {
						              return (o2 != null) ? o1.value.compareTo(o2.value) : 1;
						            }
						            return -1;
						          }
						        });

					    table.addColumnSortHandler(columnSortHandler);
					    
					    
					    
					    table.setStriped(false);
					    table.setHover(true);
					    
					    table.getColumnSortList().push(timeStampColumn);
					    table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);
					    
					    
					    // add selection model
					    
					    final SingleSelectionModel<SensorTrail> selectionModel = new SingleSelectionModel<SensorTrail>();
					    table.setSelectionModel(selectionModel);
					    selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
					      public void onSelectionChange(SelectionChangeEvent event) {
					    	  SensorTrail selected = selectionModel.getSelectedObject();
					    	  
					        if (selected != null) {
					          //Window.alert("You selected: " + selected.value);
					        	
					        	
					        	JsArray arr = JavaScriptObject.createArray().cast();
								arr.push(newJsEntry(selected.itemNumber.toString(), "1")); 
								lines.setSelections(arr);
					         
					          
					        }
					      }
					    });
					    
						
						
						graphCol.add(table);
						
						SimplePager pager = new SimplePager();
						pager.setDisplay(table);						
						
						graphCol.add(pager);
						graphCol.add(new Paragraph(" "));
						
					}
				});
		
	}

	

	public void startDownload(final String endpointName, final String propertyName)
	{
		final AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		
		
		analyticsService.getEndpointLogAsFile(tableData,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						AppController.openDirectLink("/tigerspice_dev/tigerspiceDownloads?privateURL="+result+"&outboundFileName=Sensortrail_for_"+endpointName
								+"_"+propertyName+".csv");
						
					}

		});
			
	}
	
	public void getImage(final String endpointName, final String propertyName){
		System.out.println(lines.getElement().getInnerHTML());
		
		final AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		
		final String dataUri = nativeGetUrl(lines.getJso());
		
		
		analyticsService.getEndpointLogChartAsFile(dataUri,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						AppController.openDirectLink("/tigerspice_dev/tigerspiceDownloads?privateURL="+result+"&outboundFileName=Sensortrail_for_"+endpointName
								+"_"+propertyName+".png");
						
					}

		});

		
		
	}
	
	public Column getGraphColumn(){
		return graphCol;
	}
	
	
	
	private final native JavaScriptObject newJsEntry(String rowNo, 
            String colNo)/*-{
	return {row: rowNo, col: colNo};
		
	
	}-*/;
	
	 private final native String nativeGetUrl(JavaScriptObject jso) /*-{
	    return jso.getImageURI();
	  }-*/;

	
	
	
}
