package com.velisphere.tigerspice.client.helper.widgets;

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
import com.google.gwt.visualization.client.visualizations.OrgChart.Size;
import com.google.gwt.visualization.client.visualizations.corechart.LineChart;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.CheckpathCalculatedEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.FilterAppliedEvent;
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
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;

public class FilterSphereEndpointWidget extends Composite {

	ListBox lbxSphereFilter;
	ListBox lbxEndpointFilter;
	Button btnApply;

	String userID;
	String sphereID;
	String endpointID;
	String propertyID;
	String endpointName;
	String propertyName;

	public FilterSphereEndpointWidget() {

		this.userID = SessionHelper.getCurrentUserID();
		this.sphereID = "0";
		this.endpointID = "0";
		buildWidget();

	}
	
	public FilterSphereEndpointWidget(String sphereID, String endpointID) {

		this.userID = SessionHelper.getCurrentUserID();
		this.sphereID = sphereID;
		this.endpointID = endpointID;
		buildWidget();

	}


	public void buildWidget() {
		// initWidget(uiBinder.createAndBindUi(this));

		final VerticalPanel vp = new VerticalPanel();

		initWidget(vp);

		// setup special filter layout
		
		
		Row filterRow1 = new Row();
		Column filterCol1 = new Column(2);
		Label lblSphereFilter = new Label("Sphere");
		filterCol1.add(lblSphereFilter);

		filterRow1.add(filterCol1);

		Row filterRow2 = new Row();
		Column filterCol2 = new Column(2);
		lbxSphereFilter = new ListBox();
		//lbxSphereFilter.setAlternateSize(AlternateSize.MEDIUM);
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
		//lbxEndpointFilter.setAlternateSize(AlternateSize.MEDIUM);
		lbxEndpointFilter.addStyleName("span2");
		
		filterCol4.add(lbxEndpointFilter);

		filterRow4.add(filterCol4);

		Row filterRow5 = new Row();
		Column filterCol5 = new Column(2);
		btnApply = new Button();
		btnApply.setSize(ButtonSize.SMALL);
		btnApply.setText("Apply");
		filterCol5.add(btnApply);

		filterRow5.add(filterCol5);


		
		
		

		vp.add(filterRow1);
		vp.add(filterRow2);
		vp.add(filterRow3);
		vp.add(filterRow4);
		vp.add(filterRow5);

		
		populateSphereList(userID);
		
		
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

		
			}
		});

		btnApply.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event){
				EventUtils.EVENT_BUS.fireEvent(new FilterAppliedEvent(lbxSphereFilter.getValue(), lbxEndpointFilter.getValue()));		
			}
		});
		
		

	}

	private void populateSphereList(String userID) {

		lbxSphereFilter.addItem("--- No Filter",
				"0");
		lbxEndpointFilter.addItem("--- No Filter",
				"0");


		
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
						
						
						lbxSphereFilter.setSelectedValue(sphereID);
						
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
						
						lbxEndpointFilter.addItem("--- No Filter",
								"0");

						
						Iterator<EndpointData> iT = result.iterator();
						while (iT.hasNext()) {
							EndpointData endpoint = iT.next();

							lbxEndpointFilter.addItem(endpoint.getName(),
									endpoint.getId());

						}

						
						lbxEndpointFilter.setSelectedValue(endpointID);
	
					}
				});

	}


	
	

}
