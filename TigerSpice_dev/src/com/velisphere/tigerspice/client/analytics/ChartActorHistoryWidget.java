package com.velisphere.tigerspice.client.analytics;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.AlternateSize;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.github.gwtbootstrap.datetimepicker.client.ui.DateTimeBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
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
	SimpleLineChart actorHistoryChart;
	String userID;
	String sphereID;
	String endpointID;
	String propertyID;
	String endpointName;
	String propertyName;

	public ChartActorHistoryWidget(String userID, String sphereID,
			String endpointID, String propertyID, String endpointName,
			String propertyName) {

		this.userID = userID;
		this.sphereID = sphereID;
		this.endpointID = endpointID;
		this.propertyID = propertyID;
		this.propertyName = propertyName;
		this.endpointName = endpointName;
		buildWidget();
		System.out.println("EPID " + endpointID);

	}

	public void buildWidget() {
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
		
		lbxSphereFilter.addStyleName("span2");
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
		
		lbxEndpointFilter.addStyleName("span2");
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
		
		lbxPropertyFilter.addStyleName("span2");
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
		dtbStart.addStyleName("span2");
		dtbStart.setShowTodayButton(true);
		dtbStart.setHighlightToday(true);
		// dtbStart.setBaseIcon(IconType.CALENDAR);
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
		dtbEnd.addStyleName("span2");
		dtbEnd.setShowTodayButton(true);
		dtbEnd.setHighlightToday(true);
		// dtbEnd.setBaseIcon(IconType.CALENDAR);
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

		btnDownloadChart.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actorHistoryChart.getImage(lbxEndpointFilter
						.getItemText(lbxEndpointFilter.getSelectedIndex()),
						lbxPropertyFilter.getItemText(lbxPropertyFilter
								.getSelectedIndex()));

			}

		});

		Row filterRow12 = new Row();
		Column filterCol12 = new Column(2);
		Button btnDownloadTable = new Button("Download Data");
		btnDownloadTable.setSize(ButtonSize.SMALL);
		btnDownloadTable.setType(ButtonType.PRIMARY);
		
		btnDownloadTable.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				actorHistoryChart.startDownload(lbxEndpointFilter
						.getItemText(lbxEndpointFilter.getSelectedIndex()),
						lbxPropertyFilter.getItemText(lbxPropertyFilter
								.getSelectedIndex()));

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
		
		// show message if nothing is selected or injected
		
		graphRow.add(new Paragraph("Please make a selection in the menu to the left."));

		// build graph if selection parameters were injected

		if (endpointID != null) {
			createGraph(endpointID, propertyID, endpointName, propertyName,
					null, null);
		}

		// add handlers for selection options

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

				createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter
						.getValue(), lbxEndpointFilter
						.getItemText(lbxEndpointFilter.getSelectedIndex()),
						lbxPropertyFilter.getItemText(lbxPropertyFilter
								.getSelectedIndex()), null, null);

				// VisualizationUtils.loadVisualizationApi(onLoadCallback,
				// LineChart.PACKAGE);

			}
		});

		dtbStart.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent event) {

				Timestamp tsStart = new Timestamp(dtbStart.getValue().getTime());
				Timestamp tsEnd = new Timestamp(dtbEnd.getValue().getTime());

				createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter
						.getValue(), lbxEndpointFilter
						.getItemText(lbxEndpointFilter.getSelectedIndex()),
						lbxPropertyFilter.getItemText(lbxPropertyFilter
								.getSelectedIndex()), tsStart, tsEnd);

				/**
				 * populateChartForRange(lbxEndpointFilter
				 * .getItemText(lbxEndpointFilter .getSelectedIndex()),
				 * lbxPropertyFilter .getItemText(lbxPropertyFilter
				 * .getSelectedIndex()), tsStart, tsEnd);
				 **/

			}
		});

		dtbEnd.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent event) {

				Timestamp tsStart = new Timestamp(dtbStart.getValue().getTime());
				Timestamp tsEnd = new Timestamp(dtbEnd.getValue().getTime());

				createGraph(lbxEndpointFilter.getValue(), lbxPropertyFilter
						.getValue(), lbxEndpointFilter
						.getItemText(lbxEndpointFilter.getSelectedIndex()),
						lbxPropertyFilter.getItemText(lbxPropertyFilter
								.getSelectedIndex()), tsStart, tsEnd);

				/**
				 * populateChartForRange(lbxEndpointFilter
				 * .getItemText(lbxEndpointFilter .getSelectedIndex()),
				 * lbxPropertyFilter .getItemText(lbxPropertyFilter
				 * .getSelectedIndex()), tsStart, tsEnd);
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

						if (sphereID != null) {
							lbxSphereFilter.setSelectedValue(sphereID);

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

						if (endpointID != null) {
							lbxEndpointFilter.setSelectedValue(endpointID);

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

						if (propertyID != null) {
							lbxPropertyFilter.setSelectedValue(propertyID);
							System.out.println("PROP " + propertyID);

						}

						createGraph(lbxEndpointFilter.getValue(),
								lbxPropertyFilter.getValue(), lbxEndpointFilter
										.getItemText(lbxEndpointFilter
												.getSelectedIndex()),
								lbxPropertyFilter.getItemText(lbxPropertyFilter
										.getSelectedIndex()), null, null);

					}
				});

	}

	private void createGraph(final String endpointID, final String propertyID,
			final String endpointName, final String propertyName,
			final Timestamp startDate, final Timestamp endDate) {

		graphRow.clear();
		graphRow.add(new Paragraph("Preparing Chart, please wait..."));

		Runnable onLoadCallback = new Runnable() {
			public void run() {

				final AnalyticsServiceAsync analyticsService = GWT
						.create(AnalyticsService.class);

				System.out.println(endpointID + " / " + propertyID);

				analyticsService.getActionExecutedLog(endpointID, propertyID,
						new AsyncCallback<LinkedList<AnalyticsRawData>>() {
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								System.out.println("VERTICA Error " + caught);

							}

							@Override
							public void onSuccess(
									LinkedList<AnalyticsRawData> result) {

								graphRow.clear();
								actorHistoryChart = new SimpleLineChart(result,
										endpointID, propertyID, propertyName,
										endpointName, startDate, endDate);

								graphRow.add(actorHistoryChart.getGraphColumn());

							}
						});

			}
		};
		
		VisualizationUtils.loadVisualizationApi(onLoadCallback, LineChart.PACKAGE);


	}

}
