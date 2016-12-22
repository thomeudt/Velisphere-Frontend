package com.velisphere.tigerspice.client.dashboard;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.actions.ActionService;
import com.velisphere.tigerspice.client.actions.ActionServiceAsync;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.shared.ActionData;
import com.velisphere.tigerspice.shared.AlertData;
import com.velisphere.tigerspice.shared.EndpointData;


public class AlertsState extends HorizontalPanel{

	FlexTable alertTable;
	
	public AlertsState() {
		super();
		
		alertTable = new FlexTable();
		this.add(alertTable);
		getAlertStates();
		
		
	}
	
	
	private void getAlertStates()
	{
		
	
		String userID = SessionHelper.getCurrentUserID();
		
		
	
		final EndpointServiceAsync endpointService = GWT
				.create(EndpointService.class);

		endpointService.getAllAlertsForUser(userID, new AsyncCallback<LinkedList<AlertData>>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<AlertData> result) {
						
						alertTable.setWidget(0, 0, new HTML ("<b>Alert Name</b>"));
						alertTable.setWidget(0, 1, new HTML ("<b>Last Execution</b>"));
						
						Iterator<AlertData> it = result.iterator();
						
						while (it.hasNext())
						{
							final AlertData alert = it.next();
													
							
							
							
							final ActionServiceAsync actionService = GWT
									.create(ActionService.class);
							
							actionService.getActionsForCheckpathID(alert.getCheckpathID(), new AsyncCallback<LinkedList<ActionData>>()
									{

										@Override
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method stub
											
										}

										@Override
										public void onSuccess(
												LinkedList<ActionData> result) {
											
											Iterator<ActionData> it = result.iterator();
											while(it.hasNext())
											{
												
												final AnalyticsServiceAsync analyticsService = GWT
														.create(AnalyticsService.class);
												
												analyticsService.getLastActionExecuted(it.next().getActionID(), new AsyncCallback<ActionData>()
														{

															@Override
															public void onFailure(
																	Throwable caught) {
																// TODO Auto-generated method stub
																
															}

															@Override
															public void onSuccess(
																	ActionData result) {
																int numRows = alertTable.getRowCount();
																alertTable.setWidget(numRows, 0, new HTML ("<b>"+alert.getAlertName()+" </b>"));
																if(result.getTimestamp() != null)
																{
																	alertTable.setWidget(numRows, 1, new HTML ("Last Execution: "+result.getTimestamp()));	
																	alertTable.setWidget(numRows, 2, addMessageButton(result.getPayload()));
																}
																else
																{
																	alertTable.setWidget(numRows, 1, new HTML ("Never"));
																	alertTable.setWidget(numRows, 2, addMessageButton(result.getPayload()));
																}
																
																
																				
															}
													
														});
												
												
												
												
											}
											
											
										}
								
									});
								
						}
						
						
					}

				});
	}
	
	private Button addMessageButton(final String messageText)
	{
		Button messageButton = new Button();
		messageButton.setText("See Details");
		messageButton.setType(ButtonType.PRIMARY);
		messageButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				final DialogBox detailDialog = new DialogBox();
				detailDialog.setStyleName("wellappleblue");
				detailDialog.setText("The following message has been sent");
				VerticalPanel panel = new VerticalPanel();
				
				
				panel.add(new HTML(messageText));
				Button closeButton = new Button();
				closeButton.setType(ButtonType.PRIMARY);
				closeButton.setText("OK");
				panel.add(closeButton);
				closeButton.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						detailDialog.removeFromParent();
					}
					
				});
					
				detailDialog.add(panel);
				detailDialog.center();
			}
			
		});
		
		return messageButton;
		
	}
	

}
