package com.velisphere.tigerspice.client.dash;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import com.github.gwtbootstrap.client.ui.AlertBlock;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.AbstractDataTable.ColumnType;
import com.google.gwt.visualization.client.DataTable;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.google.gwt.visualization.client.visualizations.Gauge;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class GaugeBox extends Composite {

	HTML description = new HTML("<br><b><i>New Gauge</b></i>");
	VerticalPanel panel;
	String gaugeLabel = "---";
	Gauge numericGauge;
	AlertBlock alphaGauge;
	ListBox lbxProperties = new ListBox();
	ListBox lbxEndpoints = new ListBox();
	Button btnOK = new Button("OK");
	HashMap<String, PropertyData> propertyDataMap = new HashMap<String, PropertyData>();
	int gaugeType;
	String endpointID;
	String propertyID;
	String readout;
	int[] greenRange;
	int[] yellowRange;
	int[] redRange;
	double[] minMax;
	RangePanel greenPanel;
	RangePanel yellowPanel;
	RangePanel redPanel;
	RangePanel minMaxPanel;
	Timer refreshTimer;	
	static int EMPTY_GAUGE = 0;
	static int NUMERIC_GAUGE = 1;
	static int ALPHANUMERIC_GAUGE = 2;
	static int BOOLEAN_GAUGE = 3;

	

	public GaugeBox() {
		panel = new VerticalPanel();
		gaugeType = EMPTY_GAUGE;
		greenRange = new int[2];
		yellowRange = new int[2];
		redRange = new int[2];
		minMax = new double[2];
		initWidget(panel);
		setConfigOkButton();
		configure();
		
	}

	public GaugeBox(String endpointID, String propertyID, int gaugeType, int[] greenRange, int[] yellowRange, int[] redRange, double[] minMax) {
		panel = new VerticalPanel();
		this.endpointID = endpointID;
		this.propertyID = propertyID;
		this.gaugeType = gaugeType;
		this.greenRange = greenRange;
		this.yellowRange = yellowRange;
		this.redRange = redRange;
		this.minMax = minMax;
		initWidget(panel);
		setConfigOkButton();
		setTimer();
		getData();
	}
	

	// Setters and Getters to allow storing and loading of data
	
	public String getEndpointID() {
		return endpointID;
	}

	public String getPropertyID() {
		return propertyID;
	}

	public int[] getGreenRange() {
		return greenRange;
	}

	public void setGreenRange(int[] greenRange) {
		this.greenRange = greenRange;
	}

	public int[] getYellowRange() {
		return yellowRange;
	}

	public void setYellowRange(int[] yellowRange) {
		this.yellowRange = yellowRange;
	}

	public int[] getRedRange() {
		return redRange;
	}

	public void setRedRange(int[] redRange) {
		this.redRange = redRange;
	}

	public double[] getMinMax() {
		return minMax;
	}

	public void setMinMax(double[] minMax) {
		this.minMax = minMax;
	}

	public int getGaugeType() {
		return gaugeType;
	}

	public void setGaugeType(int gaugeType) {
		this.gaugeType = gaugeType;
	}



	
	
	// Timer to refresh gauge data in pre-determined intervals
	
	private void setTimer()
	{
		refreshTimer = new Timer() {
	        @Override
	        public void run() {
	          updateData();
	        }
	      };
	      refreshTimer.scheduleRepeating(5000);

	}
	
	// all other methods
	
	private void addGauge() {
		panel.add(description);
		if (gaugeType == EMPTY_GAUGE)
		{
			showEmptyGauge();
		}
		else if (gaugeType == NUMERIC_GAUGE)
		{
			VisualizationUtils.loadVisualizationApi(onLoadCallbackNumericGauge, Gauge.PACKAGE);	
		}
		else if (gaugeType == ALPHANUMERIC_GAUGE)
		{
			showAlphaNumericGauge();	
		}
		else if (gaugeType == BOOLEAN_GAUGE)
		{
			showBooleanGauge();
		}
		
		
		
	}

	private void showAlphaNumericGauge()
	{
		
		
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		alphaGauge = new AlertBlock();
		alphaGauge.setHeading("Readout:");
		alphaGauge.setType(AlertType.DEFAULT);
		alphaGauge.setClose(false);
		alphaGauge.setText(readout);
		alphaGauge.setHeight("140px");
		alphaGauge.setWidth("140px");
		
		simplePanel.add(alphaGauge);
		panel.add(simplePanel);
		panel.add(controls());	
	}

	private void showBooleanGauge()
	{
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		alphaGauge = new AlertBlock();
		alphaGauge.setHeading("Readout:");
		alphaGauge.setType(AlertType.INFO);
		alphaGauge.setClose(false);
		if (readout == "true")
			alphaGauge.setText("1 (On)");
		else
			alphaGauge.setText("0 (Off)");
		
		alphaGauge.setHeight("140px");
		alphaGauge.setWidth("140px");
		
		simplePanel.add(alphaGauge);
		panel.add(simplePanel);
		panel.add(controls());
	}
	
	private void showEmptyGauge()
	{
		SimplePanel simplePanel = new SimplePanel();
		simplePanel.setHeight("180px");
		simplePanel.setWidth("180px");
		HTML html = new HTML("We are sorry - no data available for this sensor.");
		simplePanel.add(html);
		panel.add(simplePanel);
		panel.add(controls());
	}

	
	private Runnable onLoadCallbackNumericGauge = new Runnable() {
		public void run() {
			
			Double gaugeValue = Double.parseDouble(readout);
			
			DataTable data = DataTable.create();

			data.addColumn(ColumnType.STRING, "Label");
			data.addColumn(ColumnType.NUMBER, "Value");
			data.addRows(1);
			data.setValue(0, 0, gaugeLabel);
			data.setValue(0, 1, gaugeValue);
			Gauge.Options option = Gauge.Options.create();

			option.setHeight(180);
			option.setWidth(180);
			option.setGreenRange(greenRange[0], greenRange[1]);
			option.setMinorTicks(10);
			option.setRedRange(redRange[0], redRange[1]);
			option.setYellowRange(yellowRange[0], yellowRange[1]);
			option.set("min", minMax[0]);
			option.set("max", minMax[1]);
			option.set("animation.duration", Double.valueOf(800));
			
			numericGauge = new Gauge(data, option);
			panel.add(numericGauge);
			panel.add(controls());

		}
	};

	private HorizontalPanel controls() {

		HorizontalPanel panel = new HorizontalPanel();
		Button configureButton = new Button("Configure");
		configureButton.setSize(ButtonSize.MINI);
		configureButton.setType(ButtonType.PRIMARY);
		configureButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				configure();

			}

		});

		panel.add(configureButton);

		Button removeButton = new Button("Remove");
		removeButton.setSize(ButtonSize.MINI);
		removeButton.setType(ButtonType.DANGER);

		final Composite gauge = this;

		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				gauge.getParent().removeFromParent();
				if (refreshTimer != null)
					refreshTimer.cancel();

			}

		});

		panel.add(removeButton);
		return panel;

	}

	private void configure() {
		showConfigureBox();

	}
	
	private void showConfigureBox()
	{
		panel.clear();
		panel.add(new HTML("<br><strong>Configure Gauge</strong>"));
		panel.add(lbxEndpoints);
		lbxEndpoints.setWidth("100%");
		if(lbxEndpoints.getItemCount()==0)
		{
			fillEndpointDropDown(lbxEndpoints);		
		}
			
			
		
	}
	
	private void fillEndpointDropDown(final ListBox lbxEndpoints)
	{
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

	
		
		endpointService.getEndpointsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
	
				Iterator<EndpointData> it = result.iterator();
				while(it.hasNext())
				{
					EndpointData endpointData = it.next();
					lbxEndpoints.addItem(endpointData.endpointName, endpointData.endpointId);
								
				}
				
				lbxEndpoints.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						endpointID = lbxEndpoints.getSelectedValue();
						
						if (lbxProperties.isAttached())
						{
							lbxProperties.removeFromParent();
							
						}
						if(btnOK.isAttached())
						{
							btnOK.removeFromParent();
						}
						panel.add(lbxProperties);
						lbxProperties.setWidth("100%");
						fillPropertyDropDown(lbxEndpoints.getSelectedValue());		
					}
					
				});

				
			}
			
		});
	}

	
	private void fillPropertyDropDown(String endpointID)
	{
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

	
		
		propertyService.getSensorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {

				lbxProperties.clear();
				lbxProperties.addItem("---");
				propertyDataMap.clear();
				Iterator<PropertyData> it = result.iterator();
				while(it.hasNext())
				{
					PropertyData propertyData = it.next();
					propertyDataMap.put(propertyData.propertyId, propertyData);
					lbxProperties.addItem(propertyData.propertyName, propertyData.propertyId);
					
				}
				
				description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");

				
				lbxProperties.addChangeHandler(new ChangeHandler(){

					@Override
					public void onChange(ChangeEvent event) {
						

						description = new HTML("<br><b><i>"+lbxProperties.getSelectedItemText()+"</b></i>");
						getPropertyClassData(propertyDataMap.get(lbxProperties.getSelectedValue()).propertyclassId);
						
						propertyID = lbxProperties.getSelectedValue();
								
						
					
					}
					
				});

							
			}
			
		});
	}

	
	private void getPropertyClassData(String propertyClassID)
	{



		RootPanel.get().add(new HTML("Searching for " + propertyClassID));

		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(propertyClassID, new AsyncCallback<PropertyClassData>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(PropertyClassData result) {

				RootPanel.get().add(new HTML(result.propertyClassName));
				gaugeLabel = result.propertyClassUnit;
			
				
				if (result.propertyClassDatatype == "Double")
				{
					gaugeType = NUMERIC_GAUGE;
					setNumericGaugeProps();
				} else
					if (result.propertyClassDatatype == "String")
					{
						gaugeType = ALPHANUMERIC_GAUGE;	
					} else
						if (result.propertyClassDatatype == "Byte")
						{
							gaugeType = BOOLEAN_GAUGE;	
						}
							
				
				
		
				if(btnOK.isAttached())
				{
					btnOK.removeFromParent();
				}
				panel.add(btnOK);
		
			}
		});
		
		

	}
	
	private void setNumericGaugeProps()
	{
		if(minMaxPanel==null)
		{
			minMaxPanel = new RangePanel("Minimum/Maximum values");
			greenPanel = new RangePanel("Green range");
			yellowPanel = new RangePanel("Yellow range");
			redPanel = new RangePanel("Red range");
		}	
			panel.add(minMaxPanel);
			panel.add(greenPanel);
			panel.add(yellowPanel);
			panel.add(redPanel);
					
	}

	
	
	private void setConfigOkButton()
	{
		btnOK.setSize(ButtonSize.SMALL);
		btnOK.setType(ButtonType.PRIMARY);
		btnOK.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				panel.clear();
				setTimer();
				getData();
			}
			
		});
	}

	private void getData()
	{
		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);
		
		analyticsService.getCurrentSensorState(endpointID, propertyID, new AsyncCallback<AnalyticsRawData>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						panel.add(new HTML("Error obtaining data."));
					}

					@Override
					public void onSuccess(AnalyticsRawData result) {
						// TODO Auto-generated method stub
						
						RootPanel.get().add(new HTML("Data Size: " + result.getPropertyValuePairs().size()));
						
						if(result.getPropertyValuePairs().size() > 0)
						{
							Map.Entry<String, String> entry = result.getPropertyValuePairs().entrySet().iterator().next();
							
							// if gauge type is numeric, round value 2 two decimals
							
							if (gaugeType == NUMERIC_GAUGE)
							{
								Float numericalValue=Float.parseFloat(entry.getValue());
								readout = NumberFormat.getFormat("0.00").format(numericalValue);
								minMax[0] = Double.parseDouble(minMaxPanel.startBox.getValue());
								minMax[1] = Double.parseDouble(minMaxPanel.endBox.getValue());
								greenRange[0] = Integer.parseInt(greenPanel.startBox.getValue());
								greenRange[1] = Integer.parseInt(greenPanel.endBox.getValue());
								yellowRange[0] = Integer.parseInt(yellowPanel.startBox.getValue());
								yellowRange[1] = Integer.parseInt(yellowPanel.endBox.getValue());
								redRange[0] = Integer.parseInt(redPanel.startBox.getValue());
								redRange[1] = Integer.parseInt(redPanel.endBox.getValue());
								
							}
							else
							{

								readout = entry.getValue();	
							}
								
						}
						else
						{
							// re-set gauge type to empty
							gaugeType = EMPTY_GAUGE;
						}
						
						addGauge();
					}
			
				});

		
	}


	private void updateData()
	{
		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);
		
		analyticsService.getCurrentSensorState(endpointID, propertyID, new AsyncCallback<AnalyticsRawData>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						panel.add(new HTML("Error obtaining data."));
					}

					@Override
					public void onSuccess(AnalyticsRawData result) {
						// TODO Auto-generated method stub
						
						RootPanel.get().add(new HTML("Data Size: " + result.getPropertyValuePairs().size()));
						
						if(result.getPropertyValuePairs().size() > 0)
						{
							Map.Entry<String, String> entry = result.getPropertyValuePairs().entrySet().iterator().next();
							
							// if gauge type is numeric, round value 2 two decimals
							
							if (gaugeType == NUMERIC_GAUGE)
							{
						
								
								Float numericalValue=Float.parseFloat(entry.getValue());
								readout = NumberFormat.getFormat("0.00").format(numericalValue);
								Double gaugeValue = Double.parseDouble(readout);
								
								DataTable data = DataTable.create();

								data.addColumn(ColumnType.STRING, "Label");
								data.addColumn(ColumnType.NUMBER, "Value");
								data.addRows(1);
								data.setValue(0, 0, gaugeLabel);
								data.setValue(0, 1, gaugeValue);
								Gauge.Options option = Gauge.Options.create();

								option.setHeight(180);
								option.setWidth(180);
								option.setGreenRange(greenRange[0], greenRange[1]);
								option.setMinorTicks(10);
								option.setRedRange(redRange[0], redRange[1]);
								option.setYellowRange(yellowRange[0], yellowRange[1]);
								option.set("min", minMax[0]);
								option.set("max", minMax[1]);
								option.set("animation.duration", Double.valueOf(800));
								
								numericGauge.draw(data, option);
							}
							else if (gaugeType == BOOLEAN_GAUGE)
								
							{
								readout = entry.getValue();
								if (readout == "true")
									alphaGauge.setText("1 (On)");
								else
									alphaGauge.setText("0 (Off)");
							}
							else if (gaugeType == ALPHANUMERIC_GAUGE)
							{
								readout = entry.getValue();
								alphaGauge.setText(readout);
							}
								
						}
						else
						{
							// re-set gauge type to empty
							gaugeType = EMPTY_GAUGE;
						}
	
						
					}

				});

		
	}
	
	
	 @Override 
     public void onUnload(){
         if(this.isAttached() && refreshTimer != null){
             refreshTimer.cancel();
         }
         super.onUnload();
     }

	
	

	private class RangePanel extends VerticalPanel
	{

		
		TextBox startBox = new TextBox();
		TextBox endBox = new TextBox();
		
		public RangePanel(String text)
		{
			super();
			
			this.add(new HTML(text));
			HorizontalPanel horizontalPanel = new HorizontalPanel();
			startBox.setWidth("50px");
			endBox.setWidth("50px");
			startBox.setValue("0");
			endBox.setValue("0");
			horizontalPanel.add(startBox);
			horizontalPanel.add(new HTML("&nbsp to &nbsp"));
			horizontalPanel.add(endBox);
			this.add(horizontalPanel);			
		}

	}


}
