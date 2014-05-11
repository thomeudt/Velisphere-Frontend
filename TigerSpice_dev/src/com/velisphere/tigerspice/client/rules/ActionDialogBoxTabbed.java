package com.velisphere.tigerspice.client.rules;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.Tab;
import com.github.gwtbootstrap.client.ui.TabPanel;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.rules.ActionDialogBoxUNUSED.CheckEditorDialogBoxUiBinder;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.EndpointData;


public class ActionDialogBoxTabbed extends PopupPanel {

	
	
	public Boolean cancelFlag = false;
	public Boolean deleteFlag = false;
	private LinkedList<ActionObject> actions;
	private UuidServiceAsync rpcServiceUuid;
	private EndpointServiceAsync rpcServiceEndpoint;
	private String checkName;
	private PropertyServiceAsync rpcServiceProperty;
	private AnimationLoading animationLoading = new AnimationLoading();
    private TabPanel advanced;
    private boolean isMulticheck;

    

	  public ActionDialogBoxTabbed(LinkedList<ActionObject> actions, String checkName, boolean isMulticheck){
		 
		
  			 rpcServiceUuid = GWT.create(UuidService.class); 
  			 rpcServiceEndpoint = GWT.create(EndpointService.class);
  			 rpcServiceProperty = GWT.create(PropertyService.class);
			 this.actions = actions;
			 this.checkName = checkName;
			 this.isMulticheck = isMulticheck;
			 this.setWidget(tabWidget());
			 
		
		 

         
		  
	  }
	 
	  public Widget tabWidget() {

   		

   		
   	    //Well well = new Well();
   	    //Legend legend = new Legend("Actions for " + checkName);
   	    //well.add(legend);
   	    //well.setStyleName("wellsilver");
   	    
	 
	    advanced = new TabPanel();
	    advanced.setTabPosition("left");
	    
	    if (actions != null)
		{
	    
	    Iterator<ActionObject> it = actions.iterator();
		  while(it.hasNext()){
		    	addTab(it.next());
		    }
		advanced.selectTab(actions.size()-1);
		}
		
		addEmptyTab();
		 
	    
	    // well.add(advanced);
	    
	    Row buttonRow = new Row();
	    Column buttonColA = new Column(1);
	    Column buttonColB = new Column(1);
	    
	    Button btnSave = new Button();
	    btnSave.setText("Save");
	    btnSave.addStyleName("btn-primary");
	    
	    btnSave.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				saveAction();
							
			}
			 
		 });
		
	    
	    
	    buttonColA.add(btnSave);
	    
	    
	    Button btnCancel = new Button();
	    btnCancel.setText("Cancel");
	    btnCancel.addStyleName("btn-default");
	    btnCancel.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				cancelAction();
							
			}
			 
		 });
			    
	    
	    buttonColB.add(btnCancel);
	    
	    
	    
	    
	    buttonRow.add(buttonColA);
	    buttonRow.add(buttonColB);
	    
	    advanced.add(buttonRow);

	    
      return advanced;
      
	  }
	 
	  
	   
	  
	  
	  private void addTab(final ActionObject action) {

		Row row0 = new Row(); 
		Row row1 = new Row();
		Row row2 = new Row();
		Row row3 = new Row();
		Row row4 = new Row();
		Row row5 = new Row();
		Row row6 = new Row();
		  
		
		Column column0A = new Column(2);
	    final Label lblActionName = new Label();
	    column0A.add(lblActionName);
	    lblActionName.setText("Action Name");
	    row0.add(column0A);
	    
	    
		Column column0B = new Column(3);
	    final TextBox txtActionName = new TextBox();
	    column0B.add(txtActionName);
	    txtActionName.setText(action.actionName);
	    row0.add(column0B);
	    	    	    
		
	    Column column1A = new Column(2);
	    final Label lblTargetEndpointName = new Label();
	    column1A.add(lblTargetEndpointName);
	    lblTargetEndpointName.setText("Endpoint (Actor)");
	    row1.add(column1A);
	    
	    
		Column column1B = new Column(3);
	    final TextBox txtTargetEndpointName = new TextBox();
	    column1B.add(txtTargetEndpointName);
	    txtTargetEndpointName.setText(action.endpointName);
	    row1.add(column1B);
		
	    Column column2A = new Column(2);
	    final Label lblTargetPropertyName = new Label();
	    column2A.add(lblTargetPropertyName);
	    lblTargetPropertyName.setText("Property on Actor to be set");
	    row2.add(column2A);
	    
	    
	    Column column2B = new Column(3);
	    final TextBox txtTargetPropertyName = new TextBox();
	    txtTargetPropertyName.setText(action.propertyName);
	    column2B.add(txtTargetPropertyName);
	    row2.add(column2B);

	    
	    Column column3A = new Column(2);
	    final Label lblSettingSource = new Label();
	    column3A.add(lblSettingSource);
	    lblSettingSource.setText("New Setting");
	    row3.add(column3A);

	    Column column3B = new Column(3);
		final ListBox lstSettingSource = new ListBox();
		column3B.add(lstSettingSource);
		row3.add(column3B);

		Column column4B = new Column(3, 2);
	    final ListBox lstValidValues = new ListBox();
	    column4B.add(lstValidValues);
	    row4.add(column4B);
		
		Column column5B = new Column(3, 2);
	    final ListBox lstSensorValue = new ListBox();
	    column5B.add(lstSensorValue);
	    row5.add(column5B);

	    Column column6B = new Column(3, 2);
		final TextBox txtManualValue = new TextBox();
		txtManualValue.setText(action.manualValue);
		column6B.add(txtManualValue);
		row6.add(column6B);

	    
		 		 
		  
		  
		 final Tab tab = new Tab();
		 tab.setIcon(IconType.GEARS);
		 tab.setHeading(action.actionName);
		 
		 tab.addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				action.actionName = txtActionName.getText();
			
				
			}
			 
		 });
		 
		    
		
		 
		 lstSettingSource.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub					
					action.settingSourceIndex = lstSettingSource.getValue();
				}		    	
		 });

		 txtManualValue.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub					
					action.manualValue = txtManualValue.getValue();
				}		    	
		 });
		
		 lstValidValues.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub					
					action.validValueIndex = lstValidValues.getValue();
				}		    	
		 });
		
		 lstSensorValue.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub					
					action.propertyIdIndex = lstSensorValue.getValue();
				}		    	
		 });
		 
		 
		 txtActionName.addChangeHandler(new ChangeHandler(){
				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub
					tab.setHeading(txtActionName.getText());
					action.actionName = txtActionName.getText();
				}
		    	
		 });

		 
	    
		tab.add(row0);
	    tab.add(row1);
	    tab.add(row2);
	    tab.add(row3);
	    tab.add(row4);
	    tab.add(row5);
	    tab.add(row6);
	    advanced.add(tab);
	    
	    
		// set initial state of source of value based on whether the dialog is called by a check of multicheck edit window

	    LinkedList<String> sources;
	    	    
		if (this.isMulticheck == true){
			sources = new ActionSourceConfig().getMulticheckSources();
		} else
		{
			sources = new ActionSourceConfig().getCheckSources();
		}
		Iterator<String> it = sources.iterator();
		while (it.hasNext()){
			lstSettingSource.addItem(it.next());
		}

		txtManualValue.setVisible(false);
		lstSensorValue.setVisible(true);
		populateSensePropertiesDropDown(lstSensorValue, action);
		lstValidValues.setVisible(false);
		txtTargetEndpointName.setEnabled(false);
		txtTargetPropertyName.setEnabled(false);
		
	
		// check if this is a new rule, if so, give it a new action ID
		
				if(action.actionID == ""){
					 action.actionName = "Unnamed Action";
					 rpcServiceUuid
						.getUuid(new AsyncCallback<String>() {

							@Override
							public void onFailure(

								Throwable caught) {
								// TODO Auto-generated
								// method stub

							}

							@Override
							public void onSuccess(
									String result) {

								action.actionID = result;
							}

						});

				 } else
				 { 
					 //if not new, set the action source index accordingly
					 
					 lstSettingSource.setSelectedValue(action.settingSourceIndex);
					 
					//also then get the name of the target endpoint
					 rpcServiceEndpoint.getEndpointForEndpointID(action.endpointID, new AsyncCallback<EndpointData>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(EndpointData result) {
							// TODO Auto-generated method stub
							txtTargetEndpointName.setText(result.endpointName);
							
						}
						
					 });
					 
					//also then get the name of the target property
					 
					 rpcServiceProperty.getPropertyName(action.propertyID, new AsyncCallback<String>(){

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onSuccess(String result) {
								
								txtTargetPropertyName.setText(result);
								
							}
							
						 });
					 
					 
				 }

		
		
		// change handler to update source of value options
		
		lstSettingSource.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					// TODO Auto-generated method stub
					
					System.out.println("Index:" + lstSettingSource.getSelectedIndex());
					switch(lstSettingSource.getSelectedIndex()){
					case 0: 
						txtManualValue.setVisible(true);
						lstSensorValue.setVisible(false);
						lstValidValues.setVisible(false);
						System.out.println("EINSER");
						
						
						break;
					
					case 1: 
						txtManualValue.setVisible(false);
						lstSensorValue.setVisible(false);
						lstValidValues.setVisible(false);
						System.out.println("ZWEIER");
							
						break;
					
					
					case 2: 
						txtManualValue.setVisible(false);
						lstSensorValue.setVisible(true);
						lstValidValues.setVisible(false);
						System.out.println("DREIER");
						
						break;
					case 3: 
						txtManualValue.setVisible(false);
						lstSensorValue.setVisible(false);
						lstValidValues.setVisible(true);
						System.out.println("VIERER");
						
						break;
					
					}
					
					
				}
				
		});

		
		// set settingSource to correct value
		lstSettingSource.setSelectedValue(action.settingSourceIndex);	
		DomEvent.fireNativeEvent(Document.get().createChangeEvent(), lstSettingSource);
		
	    
	    
	  }


	  private void addEmptyTab() {

			 // check if this is a new rule
			  		  
			  
			 final Tab tab = new Tab();
			 tab.setIcon(IconType.PLUS_SIGN);
			 tab.setHeading("Add new action");
			 
			 			 
		    
			
		    advanced.add(tab);
		    
		    
		    
		    
		  }

	  
	  
	  
	  
	  public void setParameters (String ruleID, String ruleName, String endpointName, String endpointID, String endpointClassID, String propertyName, String propertyID, int settingSourceIndex, String manualValue, int validValueIndex, int propertyIdIndex, String sensorEndpointID){
			
			/**
			TextBox EPName = (TextBox) advanced.getWidget(4);
			EPName.setText(endpointName);
			TextBox PropName = (TextBox) advanced.getWidget(5);
			PropName.setText(propertyName);
			this.sensorEndpointID = sensorEndpointID;
			SensePropertiesDropDown(sensorEndpointID);
			
			**/
			
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


		private void populateSensePropertiesDropDown(final ListBox lstSensorValue, final ActionObject action){

			
			rpcServiceProperty.getSensorPropertiesForEndpointID(action.sensorEndpointID, new AsyncCallback<LinkedList<PropertyData>>(){

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
						lstSensorValue.setSelectedValue(action.propertyIdIndex);
						
										
					}
									
				}
				
			});	

		}


		void saveAction () {

			System.out.println("ACCCT: " + this.actions.getFirst());
			this.hide();		
		}


		

		void cancelAction () {
			this.cancelFlag = true;
			this.hide();
		}

		public LinkedList<ActionObject> getActions(){

			return this.actions;
		}
	
	
	
}





	





