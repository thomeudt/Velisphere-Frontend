/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client.checks;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TabPane;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.properties.PropertyEditorWidget;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.client.rules.ActionDialogBoxTabbed;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class CheckDialogBox extends PopupPanel {

	@UiField
	Legend lgdLegend;
	@UiField
	ListBox lstPropertyID;
	@UiField
	ListBox lstOperator;
	@UiField
	Button btnSave;
	@UiField
	Button btnDelete;
	@UiField
	TextBox txtTriggerValue;
	@UiField
	TextBox txtCheckTitle;
	@UiField
	Paragraph txtEndpointName;
	@UiField
	TabPane tbpActions;
	@UiField
	TabPane tbpSensor;
	@UiField
	ActionDialogBoxTabbed wgtActionDialogBox;
	
	
	private String endpointID;
	private String propertyID;
	private String propertyClassID;
	private String propertyName;
	private String checkTitle;
	private String operator;
	private String triggerValue;
	private String endpointName;
	private PropertyClassServiceAsync rpcServicePropertyClass;
	private EndpointServiceAsync rpcServiceEndpoint;
	private PropertyServiceAsync rpcServiceProperty;
	private AnimationLoading animationLoading = new AnimationLoading();
	public boolean deleteFlag = false;
	public boolean cancelFlag = false;
	private LinkedList<ActionObject> actions;

	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, CheckDialogBox> {
	}

	public CheckDialogBox(String endpointID, String propertyID,
			String propertyClassIDCalled, String propertyNameCalled, String endpointNameCalled, String checkTitle, String triggerValue, String operator, LinkedList<ActionObject> actions) {
		
		this.endpointID = endpointID;
		this.propertyID = propertyID;
		this.propertyClassID = propertyClassIDCalled;
		this.propertyName = propertyNameCalled;
		this.endpointName = endpointNameCalled;
		this.checkTitle = checkTitle;
		this.triggerValue = triggerValue;
		this.operator = operator;
		this.actions = actions;


				
		setWidget(uiBinder.createAndBindUi(this));
		
				
				
			
		if ( operator == ""){
			btnDelete.setVisible(false);
		}
		
		
		rpcServicePropertyClass = GWT.create(PropertyClassService.class);
		rpcServiceEndpoint = GWT.create(EndpointService.class);
		rpcServiceProperty = GWT.create(PropertyService.class);

		txtEndpointName.setText(this.endpointName);
		
		txtCheckTitle.setText(this.checkTitle);
		txtTriggerValue.setText(this.triggerValue);
						
		showLoadAnimation(animationLoading);
		
		// fill missing fields if check is re-opened and loaded from a checkpath, because then PCID, PropName and epName will be empty
		
		
		
		if (endpointName==""){
			rpcServiceEndpoint.getEndpointForEndpointID(endpointID, new AsyncCallback<EndpointData>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(EndpointData result) {
					endpointName = result.endpointName;
					txtEndpointName.setText(result.endpointName);			
				}
			});
		

		final String localPropId = propertyID;
		
		if (propertyClassID==""){
			showLoadAnimation(animationLoading);
			rpcServiceProperty.getPropertyClass(propertyID, new AsyncCallback<String>(){

				
				
				@Override
				public void onFailure(Throwable caught) {
					System.out.println("ERR " +caught);
					
				}

				@Override
				public void onSuccess(String result) {
					propertyClassID = result;

					
					rpcServiceProperty.getPropertyName(localPropId, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							System.out.println("ERR " +caught);
							
						}

						@Override
						public void onSuccess(String result) {
							propertyName = result;
							fillDialogBox();
							removeLoadAnimation(animationLoading);
							
						}
					});

					

					
				}
			});
			
						
			
		}

		}
		
		else {
			fillDialogBox();
		}
		
		

		
		
		
	}

	
	private void fillDialogBox(){
	
		
		System.out.println("FILLER");
		
		
		
		rpcServicePropertyClass.getPropertyClassForPropertyClassID(
				propertyClassID, new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						System.out.println("ERR " +caught);
						removeLoadAnimation(animationLoading);
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
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("byte")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("long")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("float")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("double")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("boolean")) {
							Iterator<String> it = dataTypeConfig
									.getBooleanOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}
						
						else lstOperator.addItem("Invalid Endpoint Configuration");

						lstOperator.setVisibleItemCount(1);
						lstOperator.setSelectedValue(operator);

						// lstOperator.addItem(result.propertyClassName);
						removeLoadAnimation(animationLoading);

					}

				});

		lstOperator.setEnabled(true);
		 
		
		
		if (this.operator != "") {
			lstOperator.setSelectedValue(operator);
		}
		else
		{
			lstOperator.setSelectedIndex(1);	
		}
		
		
		if (lstPropertyID.getItemCount() != 0)
		{
			lstPropertyID.removeItem(0);
		}
		lstPropertyID.addItem(propertyName, propertyID);
		lstPropertyID.setVisibleItemCount(1);
		lstPropertyID.setEnabled(false);
		

	}
	
	
	
	@UiHandler("btnSave")
	void saveNewCheck (ClickEvent event) {
		
		this.checkTitle = txtCheckTitle.getText();
		this.operator = lstOperator.getValue();
		this.triggerValue = txtTriggerValue.getText();
		this.hide();
				
	}

	@UiHandler("btnDelete")
	void deleteCheck (ClickEvent event) {
		
		this.deleteFlag = true;
		this.hide();
				
	}

	@UiHandler("btnCancel")
	void cancelCheck (ClickEvent event) {
		this.cancelFlag = true;
		this.hide();
	}	
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.removeFromParent();
	}
	
	public String getCheckTitle(){
		return this.checkTitle;
	}
	
	public String getOperator(){
		return this.operator;
	}
	public String getTriggerValue(){
		return this.triggerValue;
	}
	
	public LinkedList<ActionObject> getActions(){
		return this.wgtActionDialogBox.getActions();
	}
	
	public void setActionsTabEnabled(){
		tbpActions.setActive(true);
		tbpSensor.setActive(false);
	}
	
	@UiFactory ActionDialogBoxTabbed makeActionEditor() { // method name is insignificant
				
		
		return new ActionDialogBoxTabbed(this.actions, this.checkTitle, false);
	  }

}
