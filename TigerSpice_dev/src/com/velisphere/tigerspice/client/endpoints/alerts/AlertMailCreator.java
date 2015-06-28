package com.velisphere.tigerspice.client.endpoints.alerts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.JSONreadyEvent;
import com.velisphere.tigerspice.client.helper.HelperService;
import com.velisphere.tigerspice.client.helper.HelperServiceAsync;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.MessageData;

public class AlertMailCreator {
	
	private String text;
	private String recipient;
	private String value;
	private String operator;
	private String alertName;
	private String endpointID;
	private String endpointName;
	private String propertyName;
	private String propertyID;
	private String messageJSON;
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAlertName() {
		return alertName;
	}

	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}

	public String getEndpointID() {
		return endpointID;
	}

	public void setEndpointID(String endpointID) {
		this.endpointID = endpointID;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyID() {
		return propertyID;
	}

	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}

	public void createJSON()
	{
		
		messageJSON = new String();
		
		EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getEndpointForEndpointID(endpointID, new AsyncCallback<EndpointData>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(EndpointData result) {
				endpointName = result.endpointName;

				HelperServiceAsync helperService = GWT
						.create(HelperService.class);
		
				MessageData messageData = new MessageData();
				messageData.setRecipient(recipient);
				messageData.setPropertyID(propertyID);
				messageData.setEndpointID(endpointID);
				messageData.setSubject("Velisphere Alert Triggered: " + alertName);
				messageData.setText(getHTMLasString());
				
				helperService.createMessageJson(messageData, new AsyncCallback<String>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						
						EventUtils.RESETTABLE_EVENT_BUS.fireEvent(new JSONreadyEvent(result));
					}
					
				});
				
			}
			
		});
		
		
	}
	
	public String getHTMLasString()
	{
		String mailHTML = new String();
		
		
		mailHTML = "<p><span style=font-size:16px;><strong>VELISPHERE ALERT NOTIFICATION</strong></span></p>"+
				"<p>Dear recepient,<br>"+
				"you are receiving this message as you have been added as a subscriber to the alert named " + alertName +".</p>"+
				"<p>This alert has been triggered due to the event<br>"+
				"<b>"+propertyName+" "+ operator +" " + value +"</b></p>"+
				"<p>on Endpoint "+ endpointName +".</p>"+
				"<p><b>Additional information:<br></b>" + text +"</p>"+
				"<p>Log on to www.velisphere.com to manage this endpoint.</p>"+
				"<p>The current status of the of the endpoint is:</p>";
		
		return mailHTML;
		
	}

}
