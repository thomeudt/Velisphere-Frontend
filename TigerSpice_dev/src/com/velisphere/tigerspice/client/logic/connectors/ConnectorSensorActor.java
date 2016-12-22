package com.velisphere.tigerspice.client.logic.connectors;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.event.ConnectionSaveEvent;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.logic.draggables.DraggableButton;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SerializableLogicConnector;
import com.velisphere.tigerspice.shared.SharedConstants;

public class ConnectorSensorActor extends Connector {

	PhysicalItem actor;
	PhysicalItem sensor;
	DraggableButton openingButton;
	ListBox lbxSource;
	TextBox txtManualEntry;
	ListBox lbxValueFromSensor;
	ListBox lbxTypicalValues;
	TextBox txtCheckValue;
	ListBox lbxOperator;
	boolean layoutCreated;
	String txtManualEntryContent = "";	
	String txtCheckValueContent = "";
	int type;
	String lbxSourceValue;
	String lbxValueFromSensorValue;
	String lbxTypicalValuesValue;
	String lbxOperatorValue;
	
	

	public ConnectorSensorActor(PhysicalItem sensor,
			PhysicalItem actor) {
		super();
		createActionUUID();
		createCheckUUID();

		this.sensor = sensor;
		this.actor = actor;
		createBaseLayout();
		createOpenerWidget();
		
	}
	
	public ConnectorSensorActor(PhysicalItem sensor,
			PhysicalItem actor, String actionID, String checkID, final String lbxOperatorValue, final String lbxSourceValue, final String lbxTypicalValuesValue, final String lbxValueFromSensorValue, 
			final String txtCheckValueContent, final String txtManualEntryContent) {
		super();
		this.sensor = sensor;
		this.actor = actor;
		this.txtManualEntryContent = txtManualEntryContent;	
		this.txtCheckValueContent = txtCheckValueContent;
		this.lbxSourceValue = lbxSourceValue;
		this.lbxValueFromSensorValue = lbxValueFromSensorValue;
		this.lbxTypicalValuesValue = lbxTypicalValuesValue;
		this.lbxOperatorValue = lbxOperatorValue;
		this.actionUUID = actionID;
		this.checkUUID = checkID;
		createOpenerWidget();
					
		
	}
	

	@Override
	public void show()
	{
		super.show();
		if(layoutCreated == false) 
			{
			createBaseLayout();
			}
		setVisibleValueFields();
		
	}
	
	@Override
	public void center()
	{
		super.center();
		if(layoutCreated == false) 
		{
		createBaseLayout();
		}
		
		setVisibleValueFields();	
		
	}


	public void save()
	{
		
		lbxOperatorValue = lbxOperator.getValue();
		lbxSourceValue = lbxSource.getValue();
		lbxTypicalValuesValue = lbxTypicalValues.getValue();
		lbxValueFromSensorValue = lbxValueFromSensor.getValue();
		txtCheckValueContent = txtCheckValue.getValue();
		txtManualEntryContent = txtManualEntry.getValue();	
	}

	private void fillFields()
	{
		setVisibleValueFields();
		lbxOperator.setSelectedValue(lbxOperatorValue);
		lbxSource.setSelectedValue(lbxSourceValue);
		lbxTypicalValues.setSelectedValue(lbxTypicalValuesValue);
		lbxValueFromSensor.setSelectedValue(lbxValueFromSensorValue);
		txtCheckValue.setValue(txtCheckValueContent);
		txtManualEntry.setValue(txtManualEntryContent);
		
	}
	
	private void createBaseLayout() {
		this.layoutCreated = true;
		this.setStyleName("wellwhite");
		getElement().getStyle().setPadding(10, Unit.PX);
	
		getElement().getStyle().setBackgroundColor("#ffffff");

		VerticalPanel verticalPanel = new VerticalPanel();

		Row row0 = new Row();
		Column col0a = new Column(1);
		HorizontalPanel imageHeader = new HorizontalPanel();
		imageHeader.add(this.sensor.getImage());
		imageHeader.add(new Icon(IconType.CHEVRON_SIGN_RIGHT));
		imageHeader.add(this.actor.getImage());
		col0a.add(imageHeader);
		row0.add(col0a);
		Column col0b = new Column(4);
		col0b.add(new HTML("<b>Define Connection between "
				+ sensor.getContentRepresentation() + " " + "and "
				+ actor.getContentRepresentation() + "</b> "));
		row0.add(col0b);

		verticalPanel.add(row0);

		Row row1 = new Row();
		Column col1 = new Column(5);
		HTML headerHTML = new HTML(
				"<br><br>"
						+ "You can define the condition under which the connection will be triggered based "
						+ "on the state of <b>"
						+ sensor.getContentRepresentation() + "</b>, "
						+ "and then define what data will be send to <b>"
						+ actor.getContentRepresentation()
						+ "</b>. This could be a static "
						+ "value defined in this dialog,"
						+ "or values dynamically pulled from either <b>"
						+ sensor.getContentRepresentation()
						+ "</b> or any other sensor you have access to."
						+ "<br><br>&nbsp;");
		col1.add(headerHTML);
		row1.add(col1);
		verticalPanel.add(row1);

		Row row2 = new Row();
		Column col2A = new Column(1);
		col2A.add(new HTML("Trigger:"));

		Column col2B = new Column(1);
		lbxOperator = new ListBox();
		lbxOperator.setWidth("100%");
		col2B.add(lbxOperator);
		populateLbxOperator(lbxOperator);

		Column col2C = new Column(3);
		txtCheckValue = new TextBox();
		col2C.add(txtCheckValue);

		row2.add(col2A);
		row2.add(col2B);
		row2.add(col2C);

		verticalPanel.add(row2);

		Row row3 = new Row();
		Column col3A = new Column(1);
		col3A.add(new HTML("Push Value:"));

		Column col3B = new Column(1);
		lbxSource = new ListBox();
		lbxSource.setWidth("100%");
		col3B.add(lbxSource);
		populateLbxSource(lbxSource);

		Column col3C = new Column(3);
		txtManualEntry = new TextBox();
		col3C.add(txtManualEntry);

		lbxValueFromSensor = new ListBox();
		lbxValueFromSensor.setWidth("100%");
		col3C.add(lbxValueFromSensor);
		populateLbxValueFromSensor(lbxValueFromSensor);
		lbxValueFromSensor.setVisible(false);

		lbxTypicalValues = new ListBox();
		lbxTypicalValues.setWidth("100%");
		col3C.add(lbxTypicalValues);
		lbxTypicalValues.setVisible(false);

		row3.add(col3A);
		row3.add(col3B);
		row3.add(col3C);

		verticalPanel.add(row3);

		Row row4 = new Row();
		Column col4A = new Column(1, 4);
		Button btnSave = new Button("Close");
		btnSave.setType(ButtonType.SUCCESS);
		col4A.add(btnSave);

		
		row4.add(col4A);
		
		verticalPanel.add(row4);

		this.add(verticalPanel);

		// set the various handlers
		
		btnSave.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				save();
				close();
			}

		});

		lbxSource.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				setVisibleValueFields();
				
			}

		});
		
		

	}
	
	private void setVisibleValueFields()
	{
		if (lbxSource.getValue() == ActionSourceConfig.manual) {
			txtManualEntry.setVisible(true);
			lbxValueFromSensor.setVisible(false);
			lbxTypicalValues.setVisible(false);

		}

		if (lbxSource.getValue() == ActionSourceConfig.currentSensorValue) {
			txtManualEntry.setVisible(false);
			lbxValueFromSensor.setVisible(true);
			lbxTypicalValues.setVisible(false);
		}

		if (lbxSource.getValue() == ActionSourceConfig.otherSensorValue) {
			txtManualEntry.setVisible(false);
			lbxValueFromSensor.setVisible(false);
			lbxTypicalValues.setVisible(false);

		}

		if (lbxSource.getValue() == ActionSourceConfig.typicalEntries) {
			txtManualEntry.setVisible(false);
			lbxValueFromSensor.setVisible(false);
			lbxTypicalValues.setVisible(true);

		}
		
	}

	private void populateLbxValueFromSensor(final ListBox lbxValueFromSensor) {
		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getSensorPropertiesForEndpointID(
				sensor.getEndpointID(),
				new AsyncCallback<LinkedList<PropertyData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<PropertyData> result) {
						// TODO Auto-generated method stub

						Iterator<PropertyData> it = result.iterator();

						while (it.hasNext()) {

							PropertyData sensorProperty = new PropertyData();
							sensorProperty = it.next();

							lbxValueFromSensor.addItem(
									sensorProperty.propertyName,
									sensorProperty.propertyId);

						}
						fillFields();

					}

				});

	}

	private void populateLbxSource(final ListBox lbxSource) {
		LinkedList<String> sources;

		sources = new ActionSourceConfig().getCheckSources();

		Iterator<String> it = sources.iterator();
		while (it.hasNext()) {
			lbxSource.addItem(it.next());
		}

	}

	private void populateLbxOperator(final ListBox lbxOperator) {
		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(
				this.sensor.getPropertyClassID(),
				new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PropertyClassData result) {
						// TODO Auto-generated method stub

						DatatypeConfig dataTypeConfig = new DatatypeConfig();

						if (result.propertyClassDatatype
								.equalsIgnoreCase("string")) {
							Iterator<String> it = dataTypeConfig
									.getTextOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("byte")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("long")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("float")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("double")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("boolean")) {
							Iterator<String> it = dataTypeConfig
									.getBooleanOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lbxOperator.addItem(listItem);

							}
						}

						else
							lbxOperator
									.addItem("Invalid Endpoint Configuration");

						fillFields();
					}

				});

	}

	private void createOpenerWidget() {
		openingButton = new DraggableButton(this, this.getClass());
		openingButton.setBaseIcon(IconType.FILTER);

		openingButton.setStyleName("connbtn");

	}

	public DraggableButton getOpenerWidget() {
		return this.openingButton;
	}

	public SerializableLogicConnector getSerializableRepresentation() {
				
		SerializableLogicConnector serializable = new SerializableLogicConnector();
		serializable.setLeftID(sensor.getUUID());
		serializable.setRightID(actor.getUUID());
		serializable.setLbxOperatorValue(this.lbxOperatorValue);
		serializable.setLbxSourceValue(this.lbxSourceValue);
		serializable.setLbxTypicalValuesValue(this.lbxTypicalValuesValue);
		serializable.setLbxValueFromSensorValue(this.lbxValueFromSensorValue);
		serializable.setTxtCheckValueContent(this.txtCheckValueContent);
		serializable.setTxtManualEntryContent(this.txtManualEntryContent);
		serializable.setType(SharedConstants.CONP2P);
		serializable.setActionID(actionUUID);
		serializable.setCheckID(checkUUID);
		return serializable;

	}

	public PhysicalItem getSensor() {
		return sensor;
	}

	public PhysicalItem getActor() {
		return actor;
	}
	
	public String getOperator() {
		return String.valueOf(lbxOperatorValue);
	}

	public String getCheckValue() {
		return txtCheckValueContent;
	}
	
	public String getSourceValue() {
		return String.valueOf(lbxSourceValue);
	}
	
	public String getTypicalValueValue() {
		return String.valueOf(lbxTypicalValuesValue);
	}
	
	public String getManualValue() {
		return txtManualEntryContent;
	}
	
	public String getValueFromSensorValue() {
		return String.valueOf(lbxValueFromSensorValue);
	}
	

	
	

}
