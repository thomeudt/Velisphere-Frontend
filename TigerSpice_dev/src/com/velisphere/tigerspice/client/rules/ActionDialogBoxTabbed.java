package com.velisphere.tigerspice.client.rules;


import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

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
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.ActionSourceConfig;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.rules.ActionDialogBox.CheckEditorDialogBoxUiBinder;
import com.velisphere.tigerspice.shared.PropertyData;


public class ActionDialogBoxTabbed extends PopupPanel {

	
	
	public Boolean cancelFlag = false;
	public Boolean deleteFlag = false;
	private HashSet<ActionObject> actions;



	private PropertyServiceAsync rpcServiceProperty;
	private AnimationLoading animationLoading = new AnimationLoading();

	
	  
	  private TabPanel advanced;
	  

	  public ActionDialogBoxTabbed(HashSet<ActionObject> actions){
		 
		 this.actions = actions;		
	     this.setWidget(tabWidget());

         
		  
	  }
	 
	  public Widget tabWidget() {

   		rpcServiceProperty = GWT.create(PropertyService.class);

   	    Well well = new Well();
   	    Legend legend = new Legend("Actions");
   	    well.add(legend);
   	    well.setStyleName("wellsilver");
   	    
	 
	    advanced = new TabPanel();
	    
	    Iterator<ActionObject> it = actions.iterator();
		  while(it.hasNext()){
		    	addTab(it.next());
		    }
		 advanced.selectTab(0);    
		 
	    
	    well.add(advanced);
	    	    

	    
      return well;
      
	  }
	 
	  
	 
	  
	  
	  
	  private void addTab(ActionObject action) {

		 Tab tab = new Tab();
		 tab.setIcon(IconType.GEARS);
		 tab.setHeading("New Action");
		 
		 tab.addClickHandler(new ClickHandler(){

			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				
				
			}
			 
		 }	 
				 );
		 

		 
		Row row1 = new Row();
		Row row2 = new Row();
		Row row3 = new Row();
		Row row4 = new Row();
		Row row5 = new Row();
		Row row6 = new Row();
		  
	    Column column6B = new Column(3);
		final TextBox txtManualValue = new TextBox();
		column6B.add(txtManualValue);
		row6.add(column6B);
		
	    Column column3B = new Column(3);
		final ListBox lstSettingSource = new ListBox();
		column3B.add(lstSettingSource);
		row3.add(column3B);
		
		Column column5B = new Column(3);
	    final ListBox lstSensorValue = new ListBox();
	    column5B.add(lstSensorValue);
	    row5.add(column5B);
	    
		Column column4B = new Column(3);
	    final ListBox lstValidValues = new ListBox();
	    column4B.add(lstValidValues);
	    row4.add(column4B);
	    
		Column column1B = new Column(3);
	    final TextBox txtTargetEndpointName = new TextBox();
	    column1B.add(txtTargetEndpointName);
	    txtTargetEndpointName.setText(action.endpointName);
	    row1.add(column1B);
	    
		Column column2B = new Column(3);
	    final TextBox txtTargetPropertyName = new TextBox();
	    column2B.add(txtTargetPropertyName);
	    row2.add(column2B);
	    
	    
	    tab.add(row1);
	    tab.add(row2);
	    tab.add(row3);
	    tab.add(row4);
	    tab.add(row5);
	    tab.add(row6);
	    advanced.add(tab);
	    
	    HashSet<String> sources = new ActionSourceConfig().getSources();
		Iterator<String> it = sources.iterator();
		while (it.hasNext()){
			lstSettingSource.addItem(it.next());
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
			
			/**
			TextBox EPName = (TextBox) advanced.getWidget(4);
			EPName.setText(endpointName);
			TextBox PropName = (TextBox) advanced.getWidget(5);
			PropName.setText(propertyName);
			this.sensorEndpointID = sensorEndpointID;
			populateSensePropertiesDropDown(sensorEndpointID);
			
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
						ListBox lstSensorValue = (ListBox) advanced.getWidget(4);
											
						lstSensorValue.addItem(sensorProperty.propertyName, sensorProperty.propertyId);
										
					}
									
				}
				
			});	

		}


		void saveNewCheck (ClickEvent event) {

			//this.ruleName = this.txtRuleName.getText();
			//this.hide();		
		}


		void deleteCheck (ClickEvent event) {
			//this.deleteFlag = true;
			//this.hide();
			
			
		}


		void cancelCheck (ClickEvent event) {
			//this.cancelFlag = true;
			//this.hide();
		}

		public HashSet<ActionObject> getActions(){
			return this.actions;
		}
	
	
	
}





	





