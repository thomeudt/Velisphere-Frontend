package com.velisphere.tigerspice.client.endpoints.info;



import java.sql.Timestamp;
import java.util.Date;











import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.amqp.AMQPService;
import com.velisphere.tigerspice.client.amqp.AMQPServiceAsync;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.users.NewAccountWidget;
import com.velisphere.tigerspice.client.vendors.VendorService;
import com.velisphere.tigerspice.client.vendors.VendorServiceAsync;
import com.velisphere.tigerspice.shared.AnalyticsRawData;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.VendorData;

public class EndpointInformationWidget extends Composite {
	
	String endpointID;
	String epcID;
	@UiField
	Paragraph pgpEndpointClassName;
	@UiField
	Paragraph pgpVendorName;
	@UiField
	Image imgVendorImage;
	@UiField
	Paragraph pgpProvDate;
	@UiField
	Paragraph pgpLastSignal;
	@UiField
	Paragraph pgpEndpointLogCount;
	@UiField
	Paragraph pgpActionLogCount;
	@UiField
	Paragraph pgpCurrentState;


	private static EndpointInformationWidgetUiBinder uiBinder = GWT
			.create(EndpointInformationWidgetUiBinder.class);

	interface EndpointInformationWidgetUiBinder extends
			UiBinder<Widget, EndpointInformationWidget> {
	}

	public EndpointInformationWidget(String endpointID) {
		this.endpointID = endpointID;
		
		initWidget(uiBinder.createAndBindUi(this));
		setEndpointBaseData(endpointID);
		setAnalyticsData(endpointID);
	}
	
	
	
	private void setEndpointBaseData(String endpointID) {

		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Endpoint");
	
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		
		endpointService.getEndpointForEndpointID(endpointID,
				new AsyncCallback<EndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(EndpointData result) {
						// TODO Auto-generated method stub
						pgpProvDate.setText(result.endpointProvDate);
						pgpCurrentState.setText(result.endpointState);								
						setEndpointClassData(result.endpointclassId);
						animationLoading.removeLoadAnimation();

		
											}

				});

	
	}
	
	private void setEndpointClassData(String epcID) {
		EPCServiceAsync epcService = GWT
				.create(EPCService.class);
		
		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Class");
	
		
		epcService
				.getEndpointClassForEndpointClassID(
						epcID,
						new AsyncCallback<EPCData>() {

							@Override
							public void onFailure(
									Throwable caught) {
								// TODO Auto-generated method
								// stub
								animationLoading.removeLoadAnimation();
							}

							@Override
							public void onSuccess(EPCData result) {
								// TODO Auto-generated method
								// stub

								pgpEndpointClassName
										.setText(result.endpointclassName);
								
								animationLoading.removeLoadAnimation();
								setVendorData(result.vendorID);
								
								
							}

						});


	}

	
	private void setVendorData(String vendorID) {
		VendorServiceAsync vendorService = GWT
				.create(VendorService.class);
		
		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Vendor");
	
		
		vendorService
				.getVendorForVendorID(vendorID,
						new AsyncCallback<VendorData>() {

							@Override
							public void onFailure(
									Throwable caught) {
								// TODO Auto-generated method
								// stub
								animationLoading.removeLoadAnimation();
							}

							@Override
							public void onSuccess(VendorData result) {
								// TODO Auto-generated method
								// stub

								pgpVendorName
										.setText(result.vendorName);
								
								imgVendorImage.setUrl(result.vendorImageURL);
								imgVendorImage.setHeight("40px");
								animationLoading.removeLoadAnimation();
								
								
							}

						});


	}

	private void setAnalyticsData(String endpointID) {

		final AnimationLoading animationLoading = new AnimationLoading();
		
		animationLoading.showLoadAnimation("Loading Analytics Data");
	
		AnalyticsServiceAsync analyticsService = GWT
				.create(AnalyticsService.class);
		
		
		analyticsService.getLastEndpointLogTime(endpointID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
						if(result==null)
							pgpLastSignal.setText("no messages received yet");
						else
							pgpLastSignal.setText(result);
						
						animationLoading.removeLoadAnimation();

		
											}

				});
		
		analyticsService.getEndpointLogCount(endpointID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
						if(result==null)
							pgpEndpointLogCount.setText("no messages received yet");
						else
							pgpEndpointLogCount.setText(result);
						
						animationLoading.removeLoadAnimation();

		
											}

				});

		analyticsService.getActionLogCount(endpointID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
						if(result==null)
							pgpActionLogCount.setText("no messages sent yet");
						else
							pgpActionLogCount.setText(result);
						
						animationLoading.removeLoadAnimation();

		
											}

				});


	
	}
	
	@UiHandler("btnPing")
	void sendPingRequest (ClickEvent event)  {
		AMQPServiceAsync amqpService = GWT
				.create(AMQPService.class);
		
		
		amqpService.sendIsAliveRequest(endpointID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
						pgpCurrentState.setText("Refreshing. Please wait...");
						Timer t = new Timer() {
						      @Override
						      public void run() {
						    	  updateEndpointState();   
						      }
						    };

						    // Schedule the timer to run once in 5 seconds.
						    t.schedule(3000);
						
						    
					}
		
	
	});
	}
	
	void updateEndpointState(){
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);
		
		
		endpointService.getEndpointForEndpointID(endpointID,
				new AsyncCallback<EndpointData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(EndpointData result) {
						// TODO Auto-generated method stub
						pgpCurrentState.setText(result.endpointState);
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
						AppController.openEndpoint(endpointID);
						
					}

		
		});
	}

}
