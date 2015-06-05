package com.velisphere.tigerspice.client.endpoints.config;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Heading;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.amqp.AMQPService;
import com.velisphere.tigerspice.client.amqp.AMQPServiceAsync;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class EndpointConfiguratorWidget extends Composite  {

	private static EndpointConfiguratorWidgetUiBinder uiBinder = GWT
			.create(EndpointConfiguratorWidgetUiBinder.class);

	interface EndpointConfiguratorWidgetUiBinder extends
			UiBinder<Widget, EndpointConfiguratorWidget> {
	}


	String endpointID;
	HandlerRegistration submitClickReg;	
	@UiField
	ListBox lstConfigurators;
	@UiField
	Heading pgpPropertyName;
	@UiField
	Button btnSubmit;
	@UiField
	Button btnRefresh;
	@UiField
	Paragraph pgpLastValue;
	@UiField
	Paragraph pgpLastUpdate;
	@UiField
	Paragraph pgpUnit;
	@UiField
	Paragraph pgpCurrentValueHeader;
	@UiField
	Paragraph pgpNewValueHeader;
	@UiField
	Paragraph pgpLastUpdateHeader;
	@UiField
	Paragraph pgpUnitHeader;
	@UiField
	TextBox txtNewValue;
	

	public EndpointConfiguratorWidget(String endpointID) {
		this.endpointID = endpointID;
	
		initWidget(uiBinder.createAndBindUi(this));
		populateConfiguratorList();
		lstConfigurators.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				populateCurrentState(lstConfigurators.getValue());
				populatePropertyData(lstConfigurators.getValue());
			}
		});
		
		
	}
	
	private void populateConfiguratorList() {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Endpoint");

		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getConfiguratorPropertiesForEndpointID(endpointID,
				new AsyncCallback<LinkedList<PropertyData>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(LinkedList<PropertyData> result) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
						Iterator<PropertyData> it = result.iterator();
						if (it.hasNext() == false) {
							
							pgpPropertyName.setText("This endpoint does not allow configuration.");
							pgpUnitHeader.setText("");
							pgpCurrentValueHeader.setText("");
							pgpNewValueHeader.setText("");
							pgpLastUpdateHeader.setText("");
							pgpUnit.setText("");
							pgpLastValue.setText("");
							pgpLastUpdate.setText("");
							btnSubmit.setVisible(false);
							btnRefresh.setVisible(false);
							txtNewValue.setVisible(false);

						}
						while (it.hasNext()) {

							PropertyData propData = new PropertyData();
							propData = it.next();
							lstConfigurators.addItem(propData.propertyName,
									propData.propertyId);
							lstConfigurators.setSelectedIndex(0);
							populateCurrentState(lstConfigurators.getValue());
							populatePropertyData(lstConfigurators.getValue());

						}

					}

				});

		lstConfigurators.setVisibleItemCount(10);


	}
	
	
	private void populateCurrentState(final String propertyID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Current State");

		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);

		analyticsService.getCurrentSensorState(endpointID, propertyID,
				new AsyncCallback<AnalyticsRawData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(AnalyticsRawData result) {
						
						
						if (result.getPropertyValuePairs().get(propertyID) == null)
						{
							pgpLastValue.setText("not available");
							pgpLastUpdate.setText("not available");
						}
						else					
						{
							pgpLastValue.setText(result.getPropertyValuePairs().get(propertyID));
							pgpLastUpdate.setText(result.getTimeStamp());
						}
						
						animationLoading.removeLoadAnimation();
					}
				});
	}
	
	private void populatePropertyData(final String propertyID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Class-specific Data");


		PropertyServiceAsync propertyService = GWT
				.create(PropertyService.class);

		propertyService.getPropertyDetailsForPropertyID(propertyID, new AsyncCallback<PropertyData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PropertyData result) {
						animationLoading.removeLoadAnimation();
						pgpPropertyName.setText(result.getPropertyName());
						populatePropertyClassData(result.propertyclassId);
						addSubmitClickhandler(result.propertyId);
						
						
						
					}

				});
	}
	
	private void populatePropertyClassData(final String propertyClassID) {

		final AnimationLoading animationLoading = new AnimationLoading();

		animationLoading.showLoadAnimation("Loading Class-specific Data");


		PropertyClassServiceAsync propertyClassService = GWT
				.create(PropertyClassService.class);

		propertyClassService.getPropertyClassForPropertyClassID(propertyClassID, new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(PropertyClassData result) {
						animationLoading.removeLoadAnimation();

						pgpUnit.setText(result.getUnit());
					}

				});
	}

	private void addSubmitClickhandler(final String propertyID) 
	{
		if (submitClickReg != null) {
			submitClickReg.removeHandler();
		}

		submitClickReg = btnSubmit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				AMQPServiceAsync amqpService = GWT
						.create(AMQPService.class);

				amqpService.sendConfigMessage(endpointID, propertyID, txtNewValue.getText(), new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onSuccess(String result) {
								
								pgpLastValue.setText("New value submitted, refreshing data from endpoint. Please wait...");
								Timer t = new Timer() {
								      @Override
								      public void run() {
								    	  populateCurrentState(propertyID);   
								      }
								    };

								    // Schedule the timer to run once in 5 seconds.
								    t.schedule(3000);
								
								
								
							}

						});
				
			}

		});
	}
	
	@UiHandler("btnRefresh")
	void onClick(ClickEvent e) {
		AMQPServiceAsync amqpService = GWT
				.create(AMQPService.class);

		amqpService.sendGetAllProperties(endpointID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						AppController.openEndpoint(endpointID, VeliConstants.VIEWMODE_CONFIG);
						
					}

		
		});
	}

}

	