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
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
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
import com.google.gwt.visualization.client.ChartArea;
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
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SphereData;
import com.velisphere.tigerspice.shared.TableRowData;
import com.velisphere.tigerspice.client.analytics.SimpleLineChart;

public class ChartActorHistoryWidget extends Composite {

	ListBox lbxSphereFilter;
	ListBox lbxEndpointFilter;
	ListBox lbxPropertyFilter;

	DateTimeBox dtbStart;
	DateTimeBox dtbEnd;
	DataTable data;
	LinkedList<TableRowData> tableData;
	LineChart lines;
	Row graphRow;
	SimpleLineChart sensorHistoryChart;
	

	public ChartActorHistoryWidget(String userID) {
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
		Label lblPropertyFilter = new Label("Actor");
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
		
		btnDownloadChart.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event) {
				sensorHistoryChart.getImage(lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), 
						lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));
				
				
			}
			
		});
		
		
		Row filterRow12 = new Row();
		Column filterCol12 = new Column(2);
		Button btnDownloadTable = new Button("Download Data");
		btnDownloadTable.setSize(ButtonSize.SMALL);
		btnDownloadTable.setType(ButtonType.PRIMARY);
		btnDownloadTable.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event) {
				sensorHistoryChart.startDownload(lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), 
						lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()));
				
				
			}
			
		});
		
		filterCol12.add(new Paragraph(" "));
		filterCol12.add(btnDownloadTable);
		filterRow12.add(filterCol12);
		
		graphRow = new Row();
		
		

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

				populateActorList(lbxEndpointFilter.getValue());
			}
		});

		lbxPropertyFilter.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				
				graphRow.add(new Paragraph("Loading chart, please wait..."));

				Runnable onLoadCallback = new Runnable() {
					public void run() {
						
						createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter.getValue(), lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), 
								lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()), null, null);
						
						
						/**
						populateChart(lbxEndpointFilter
								.getItemText(lbxEndpointFilter
										.getSelectedIndex()), lbxPropertyFilter
								.getItemText(lbxPropertyFilter
										.getSelectedIndex()));
						**/
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
				
				
				createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter.getValue(), lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), 
						lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()), tsStart, tsEnd);
				
				
				/**
				populateChartForRange(lbxEndpointFilter
						.getItemText(lbxEndpointFilter
								.getSelectedIndex()), lbxPropertyFilter
						.getItemText(lbxPropertyFilter
								.getSelectedIndex()), tsStart, tsEnd);
				**/
				
			}
		});

		dtbEnd.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent event) {

				Timestamp tsStart = new Timestamp(dtbStart.getValue().getTime());
				Timestamp tsEnd = new Timestamp(dtbEnd.getValue().getTime());
				
				createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter.getValue(), lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), 
						lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()), tsStart, tsEnd);
				
				
				
				
				/**
				populateChartForRange(lbxEndpointFilter
						.getItemText(lbxEndpointFilter
								.getSelectedIndex()), lbxPropertyFilter
						.getItemText(lbxPropertyFilter
								.getSelectedIndex()), tsStart, tsEnd);
					**/		
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
						populateActorList(lbxEndpointFilter.getValue());
					}
				});

	}

	private void populateActorList(String endpointID) {

		final PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getActorPropertiesForEndpointID(endpointID,
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
						
						createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter.getValue(), lbxEndpointFilter.getItemText(lbxEndpointFilter.getSelectedIndex()), 
								lbxPropertyFilter.getItemText(lbxPropertyFilter.getSelectedIndex()), null, null);
						
						
						

						/**
						populateChart(lbxEndpointFilter
								.getItemText(lbxEndpointFilter
										.getSelectedIndex()), lbxPropertyFilter
								.getItemText(lbxPropertyFilter
										.getSelectedIndex()));
						**/
					}
				});

	}

	private void createGraph(final String endpointID, final String propertyID, final String endpointName, final String propertyName, final Timestamp startDate, final Timestamp endDate)
	{
	
		
		
	final AnalyticsServiceAsync analyticsService = GWT
			.create(AnalyticsService.class);

	System.out.println(endpointID + " / "
			+ propertyID);
	
	analyticsService.getEndpointLog(endpointID,
			propertyID,
			new AsyncCallback<LinkedList<AnalyticsRawData>>() {
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					System.out.println("VERTICA Error " + caught);

				}

				@Override
				public void onSuccess(LinkedList<AnalyticsRawData> result) {

					graphRow.clear();
					sensorHistoryChart = new SimpleLineChart(result, endpointID, propertyID, propertyName, endpointName, startDate, endDate);
					
					
					graphRow.add(sensorHistoryChart.getGraphColumn());
					
				}
		});
	}
	
}
