package com.velisphere.tigerspice.client.analytics;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.james.mime4j.field.datetime.DateTime;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.CellTable;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.AlternateSize;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBox;
import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBoxAppended;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.CellPreviewEvent;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.DefaultSelectionEventManager.SelectAction;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.visualization.client.AbstractDataTable;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.Query.Callback;
import com.google.gwt.visualization.client.Query.SendMethod;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.Query;
import com.google.gwt.visualization.client.QueryResponse;
import com.google.gwt.visualization.client.Selection;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.events.PageHandler;
import com.google.gwt.visualization.client.events.SelectHandler;
import com.google.gwt.visualization.client.events.SelectHandler.SelectEvent;
import com.google.gwt.visualization.client.formatters.ArrowFormat;
import com.google.gwt.visualization.client.visualizations.Table;
import com.google.gwt.visualization.client.visualizations.corechart.CoreChart;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.google.gwt.visualization.client.visualizations.corechart.Options;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.spheres.SphereService;
import com.velisphere.tigerspice.client.spheres.SphereServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.EndpointLogData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SphereData;
import com.velisphere.tigerspice.shared.TableRowData;

public class ChartSensorHistoryWidget extends Composite {

	ListBox lbxSphereFilter;
	ListBox lbxEndpointFilter;
	ListBox lbxPropertyFilter;
	Column graphCol;
	DateTimeBox dtbStart;
	DateTimeBox dtbEnd;
	DataTable data;
	LinkedList<TableRowData> tableData;
	

	public ChartSensorHistoryWidget(String userID) {
		// initWidget(uiBinder.createAndBindUi(this));

		final VerticalPanel vp = new VerticalPanel();

		
		
		initWidget(vp);

		tableData = new LinkedList<TableRowData>();
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
		lbxSphereFilter = new ListBox();
		lbxSphereFilter.setAlternateSize(AlternateSize.MEDIUM);
		filterCol2.add(lbxSphereFilter);

		filterRow2.add(filterCol2);

		Row filterRow3 = new Row();
		Column filterCol3 = new Column(2);
		Label lblEndpointFilter = new Label("Endpoint");
		filterCol3.add(lblEndpointFilter);

		filterRow3.add(filterCol3);

		Row filterRow4 = new Row();
		Column filterCol4 = new Column(2);
		lbxEndpointFilter = new ListBox();
		lbxEndpointFilter.setAlternateSize(AlternateSize.MEDIUM);
		filterCol4.add(lbxEndpointFilter);

		filterRow4.add(filterCol4);

		Row filterRow5 = new Row();
		Column filterCol5 = new Column(2);
		Label lblPropertyFilter = new Label("Sensor");
		filterCol5.add(lblPropertyFilter);

		filterRow5.add(filterCol5);

		Row filterRow6 = new Row();
		Column filterCol6 = new Column(2);
		lbxPropertyFilter = new ListBox();
		lbxPropertyFilter.setAlternateSize(AlternateSize.MEDIUM);
		filterCol6.add(lbxPropertyFilter);
		filterRow6.add(filterCol6);

		Row filterRow7 = new Row();
		Column filterCol7 = new Column(2);
		Label lblStartTimeFilter = new Label("Range start");
		filterCol7.add(lblStartTimeFilter);
		filterRow7.add(filterCol7);

		Row filterRow8 = new Row();
		Column filterCol8 = new Column(2);
		dtbStart = new DateTimeBox();
		dtbStart.setAlternateSize(AlternateSize.MEDIUM);
		dtbStart.setShowTodayButton(true);
		dtbStart.setHighlightToday(true);
		//dtbStart.setBaseIcon(IconType.CALENDAR);
		dtbStart.setAutoClose(true);
		
		filterCol8.add(dtbStart);				
		filterRow8.add(filterCol8);

		
		Row filterRow9 = new Row();
		Column filterCol9 = new Column(2);
		Label lblEndTimeFilter = new Label("Range end");
		filterCol9.add(lblEndTimeFilter);
		filterRow9.add(filterCol9);

		Row filterRow10 = new Row();
		Column filterCol10 = new Column(2);
		dtbEnd = new DateTimeBox();
		dtbEnd.setAlternateSize(AlternateSize.MEDIUM);
		dtbEnd.setShowTodayButton(true);
		dtbEnd.setHighlightToday(true);
		//dtbEnd.setBaseIcon(IconType.CALENDAR);
		dtbEnd.setAutoClose(true);
		
		filterCol10.add(dtbEnd);
		filterRow10.add(filterCol10);

		Row filterRow11 = new Row();
		Column filterCol11 = new Column(2);
		Button btnDownloadChart = new Button("Download Chart");
		btnDownloadChart.setSize(ButtonSize.SMALL);
		btnDownloadChart.setType(ButtonType.PRIMARY);
		filterCol11.add(btnDownloadChart);
		filterRow11.add(filterCol11);
		
		Row filterRow12 = new Row();
		Column filterCol12 = new Column(2);
		Button btnDownloadTable = new Button("Download Data");
		btnDownloadTable.setSize(ButtonSize.SMALL);
		btnDownloadTable.setType(ButtonType.PRIMARY);
		btnDownloadTable.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event) {
				startDownload();
				
			}
			
		});
		
		filterCol12.add(new Paragraph(" "));
		filterCol12.add(btnDownloadTable);
		filterRow12.add(filterCol12);
		
		Row graphRow = new Row();
		graphCol = new Column(8);
		graphRow.add(graphCol);

		leftCol.add(filterRow1);
		leftCol.add(filterRow2);
		leftCol.add(filterRow3);
		leftCol.add(filterRow4);
		leftCol.add(filterRow5);
		leftCol.add(filterRow6);
		leftCol.add(filterRow7);
		leftCol.add(filterRow8);
		leftCol.add(filterRow9);
		leftCol.add(filterRow10);
		leftCol.add(filterRow11);
		leftCol.add(filterRow12);

		rightCol.add(graphRow);

		populateSphereList(userID);
		
		
		lbxSphereFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				populateEndpointList(lbxSphereFilter.getValue());
			}
		});
		lbxEndpointFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				populateSensorList(lbxEndpointFilter.getValue());
			}
		});

		lbxPropertyFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				graphCol.add(new Paragraph("Loading chart..."));

				Runnable onLoadCallback = new Runnable() {
					public void run() {

						populateChart(lbxEndpointFilter
								.getItemText(lbxEndpointFilter
										.getSelectedIndex()), lbxPropertyFilter
								.getItemText(lbxPropertyFilter
										.getSelectedIndex()));
						
					}
				};

				VisualizationUtils.loadVisualizationApi(onLoadCallback,
						LineChart.PACKAGE);
				
				
			}
		});
		
		
		
		dtbStart.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent event) {

				
				Timestamp tsStart = new Timestamp(dtbStart.getValue().getTime());
				Timestamp tsEnd = new Timestamp(dtbEnd.getValue().getTime());
				
				populateChartForRange(lbxEndpointFilter
						.getItemText(lbxEndpointFilter
								.getSelectedIndex()), lbxPropertyFilter
						.getItemText(lbxPropertyFilter
								.getSelectedIndex()), tsStart, tsEnd);
			
				
			}
		});

		dtbEnd.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent event) {

				Timestamp tsStart = new Timestamp(dtbStart.getValue().getTime());
				Timestamp tsEnd = new Timestamp(dtbEnd.getValue().getTime());
				
				populateChartForRange(lbxEndpointFilter
						.getItemText(lbxEndpointFilter
								.getSelectedIndex()), lbxPropertyFilter
						.getItemText(lbxPropertyFilter
								.getSelectedIndex()), tsStart, tsEnd);
							
			}
		});

		
		
	}

	private void populateSphereList(String userID) {

		final SphereServiceAsync sphereService = GWT
				.create(SphereService.class);

		sphereService.getAllSpheresForUserID(userID,
				new AsyncCallback<LinkedList<SphereData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<SphereData> result) {
						Iterator<SphereData> iT = result.iterator();
						while (iT.hasNext()) {
							SphereData sphere = iT.next();
							lbxSphereFilter.addItem(sphere.getName(),
									sphere.getId());

						}
						populateEndpointList(lbxSphereFilter.getValue());

					}
				});
	}

	private void populateEndpointList(String sphereID) {

		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getEndpointsForSphere(sphereID,
				new AsyncCallback<LinkedList<EndpointData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {
						lbxEndpointFilter.clear();

						Iterator<EndpointData> iT = result.iterator();
						while (iT.hasNext()) {
							EndpointData endpoint = iT.next();

							lbxEndpointFilter.addItem(endpoint.getName(),
									endpoint.getId());

						}
						populateSensorList(lbxEndpointFilter.getValue());
					}
				});

	}

	private void populateSensorList(String endpointID) {

		final PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getSensorPropertiesForEndpointID(endpointID,
				new AsyncCallback<LinkedList<PropertyData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<PropertyData> result) {
						lbxPropertyFilter.clear();

						Iterator<PropertyData> iT = result.iterator();
						while (iT.hasNext()) {
							PropertyData property = iT.next();

							lbxPropertyFilter.addItem(property.getName(),
									property.getId());

						}

						populateChart(lbxEndpointFilter
								.getItemText(lbxEndpointFilter
										.getSelectedIndex()), lbxPropertyFilter
								.getItemText(lbxPropertyFilter
										.getSelectedIndex()));
						
					}
				});

	}

	private void populateChart(String endpointName, String propertyName) {

		// clear tableData before loading new data
		
		tableData.clear();

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

		System.out.println(lbxEndpointFilter.getValue() + " / "
				+ lbxPropertyFilter.getValue());
		
		analyticsService.getEndpointLog(lbxEndpointFilter.getValue(),
				lbxPropertyFilter.getValue(),
				new AsyncCallback<LinkedList<EndpointLogData>>() {
					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<EndpointLogData> result) {
						// TODO Auto-generated method stub

						// prep the datatable

						if (result.size() > 0){
							
						
						
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
						table.addColumn(valueColumn, lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));
						table.setRowCount(result.size(), true);
						//table.setVisibleRange(0, 10);

						// Create a data provider.
						ListDataProvider<SensorTrail> dataProvider = new ListDataProvider<SensorTrail>();

						// Connect the table to the data provider.
						dataProvider.addDataDisplay(table);
					

						// create the datatable for chart and celltable for
						// table

						final DataTable data = DataTable.create();
						data.addColumn(ColumnType.STRING, "Time");
						data.addColumn(ColumnType.NUMBER, lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));

						data.addRows(result.size() + 1);

						int i = 1;
						Iterator<EndpointLogData> it = result.iterator();
						List<SensorTrail> list = dataProvider.getList();
						
						TableRowData introRow = new TableRowData();
						introRow.setRow("*** VeliSphere Data Trail Export for " + lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex())
								+ " on " + lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), "", "", "", "", "", "", "");
						
						tableData.add(introRow);
				
						TableRowData spacerRow = new TableRowData();
						spacerRow.setRow(" ", "", "", "", "", "", "", "");
						tableData.add(spacerRow);
				
						
						TableRowData headerRow = new TableRowData();
						headerRow.setRow("Time Stamp", lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()), "", "", "", "", "", "");
						tableData.add(headerRow);
						
						
					

						while (it.hasNext()) {

							EndpointLogData logItem = it.next();
							data.setValue(i, 0, logItem.getTimeStamp());
							data.setValue(i, 1,	logItem.getValue());
							TableRowData row = new TableRowData();
							row.setRow(logItem.getTimeStamp(), String.valueOf(logItem.getValue()), "", "", "", "", "", "");
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

						// Create a line chart visualization.
						final LineChart lines = new LineChart(data, options);
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
						
						SimplePager test = new SimplePager();
						test.setDisplay(table);						
						
						graphCol.add(test);
						graphCol.add(new Paragraph(" "));
						
					}
						else
						{
							graphCol.clear();
							graphCol.add(new Paragraph("No data to display - data out of date range, nothing tracked for this sensor or sensor is not providing numeric values."));
						}
					}
					
				});
	}


	private void populateChartForRange(String endpointName, String propertyName, final Timestamp startDate, final Timestamp endDate) {

		// clear tableData before loading new data
		
				tableData.clear();


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

		System.out.println(lbxEndpointFilter.getValue() + " / "
				+ lbxPropertyFilter.getValue());
		
		analyticsService.getEndpointLog(lbxEndpointFilter.getValue(),
				lbxPropertyFilter.getValue(),
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
						table.addColumn(valueColumn, lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));
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
						data.addColumn(ColumnType.NUMBER, lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));

						data.addRows(result.size() + 1);
						
						

						int i = 1;
						Iterator<EndpointLogData> it = result.iterator();
						List<SensorTrail> list = dataProvider.getList();

						TableRowData introRow = new TableRowData();
						introRow.setRow("VeliSphere Data Trail Export for " + lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex())
								+ " on " + lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), "", "", "", "", "", "", "");
						
						tableData.add(introRow);
				
						TableRowData spacerRow = new TableRowData();
						spacerRow.setRow("---------------------------------------", "", "", "", "", "", "", "");
						tableData.add(spacerRow);
				
						
						TableRowData headerRow = new TableRowData();
						headerRow.setRow("VeliSphere Data Trail Export for " + lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex())
								+ " on " + lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), "", "", "", "", "", "", "");
						tableData.add(headerRow);
						
						
						
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

						System.out.println(tableData.toString());
						
						// Create a line chart visualization.
						final LineChart lines = new LineChart(data, options);
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
						
						SimplePager test = new SimplePager();
						test.setDisplay(table);						
						
						graphCol.add(test);
						graphCol.add(new Paragraph(" "));
						
					}
				});
	}

	

	private void startDownload()
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
						AppController.openDirectLink("/tigerspice_dev/tigerspiceDownloads?privateURL="+result+"&outboundFileName=Sensortrail_for_"+lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex())
								+"_"+lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex())+".csv");
						
					}

		});

	
		
		
	}
	
	private final native JavaScriptObject newJsEntry(String rowNo, 
            String colNo)/*-{
	return {row: rowNo, col: colNo};
}-*/; 
	
}
