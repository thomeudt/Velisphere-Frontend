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
package com.velisphere.tigerspice.client.rules;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.CombinationConfig;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;
import com.velisphere.tigerspice.shared.PropertyData;

public class ActionDialogBoxUNUSED extends PopupPanel {

	@UiField
	TextBox txtTargetPropertyName;
	@UiField
	TextBox txtTargetEndpointName;
	@UiField
	Button btnSave;
	@UiField
	TextBox txtManualValue;
	@UiField
	ListBox lstValidValues;
	@UiField
	ListBox lstSensorValue;
	@UiField
	TextBox txtRuleName;
	@UiField
	Legend lgdHeader;
	@UiField
	Button btnDelete;
	@UiField
	ListBox lstSettingSource;
		
	public String ruleID;
	public Boolean cancelFlag = false;
	public Boolean deleteFlag = false;
	public String sensorEndpointID;
	public String ruleName;
	public String endpointName;
	public String endpointID;
	public String endpointClassID;
	public String propertyName;
	public String propertyID;
	public int settingSourceIndex;
	public String manualValue;
	public int validValueIndex;
	public int propertyIdIndex;
	

	
		
	
	

    private PropertyServiceAsync rpcServiceProperty;
	

	
	private AnimationLoading animationLoading = new AnimationLoading();

	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, ActionDialogBoxUNUSED> {
	}

	public ActionDialogBoxUNUSED() {

		rpcServiceProperty = GWT.create(PropertyService.class);
		
		setWidget(uiBinder.createAndBindUi(this));
		LinkedList<String> sources = new ActionSourceConfig().getCheckSources();
		Iterator<String> it = sources.iterator();
		while (it.hasNext()){
			this.lstSettingSource.addItem(it.next());
		}

		// set initial state of source of value
		
		txtManualValue.setVisible(false);
		lstSensorValue.setVisible(true);
		lstValidValues.setVisible(false);
		txtTargetEndpointName.setEnabled(false);
		txtTargetPropertyName.setEnabled(false);
		
		
		// change handler to update source of value options
		
		lstSettingSource.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub
					
					System.out.println("Index:" + lstSettingSource.getSelectedIndex());
					switch(lstSettingSource.getSelectedIndex()){
					case 0: 
						txtManualValue.setVisible(false);
						lstSensorValue.setVisible(true);
						lstValidValues.setVisible(false);
						System.out.println("EINSER");
						break;
					case 1: 
						txtManualValue.setVisible(false);
						lstSensorValue.setVisible(false);
						lstValidValues.setVisible(true);
						System.out.println("ZWEIER");
						break;
					case 2: 
						txtManualValue.setVisible(false);
						lstSensorValue.setVisible(false);
						lstValidValues.setVisible(false);
						System.out.println("DREIER");
						break;
					case 3: 
						txtManualValue.setVisible(true);
						lstSensorValue.setVisible(false);
						lstValidValues.setVisible(false);
						System.out.println("VIERER");
						break;
					}
					
				}
		});
		
	
	}
	
	public void setParameters (String ruleID, String ruleName, String endpointName, String endpointID, String endpointClassID, String propertyName, String propertyID, int settingSourceIndex, String manualValue, int validValueIndex, int propertyIdIndex, String sensorEndpointID){
		
		
		txtTargetEndpointName.setText(endpointName);
		txtTargetPropertyName.setText(propertyName);
		this.sensorEndpointID = sensorEndpointID;
		populateSensePropertiesDropDown(sensorEndpointID);
		
		/**
		txtMulticheckTitle.setText(multicheckTitle);
		Iterator<Entry<String, String>> it = linkedChecks.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, String> checkPair = (Map.Entry<String, String>)it.next();
			lstLinkedChecksID.addItem(checkPair.getValue(), checkPair.getKey());
		}
		lstLinkedChecksID.setVisibleItemCount(5);
		**/
	}

	
private void populateSensePropertiesDropDown(final String endpointID){

		
		rpcServiceProperty.getSensorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {
				// TODO Auto-generated method stub
				
				Iterator<PropertyData> it = result.iterator();
				
				while(it.hasNext()){
				
					PropertyData sensorProperty = new PropertyData();
					sensorProperty = it.next();
					lstSensorValue.addItem(sensorProperty.propertyName, sensorProperty.propertyId);
									
				}
								
			}
			
		});	

	}

	
	
	@UiHandler("btnSave")
	void saveNewCheck (ClickEvent event) {
	
		this.ruleName = this.txtRuleName.getText();
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

	
	
}
