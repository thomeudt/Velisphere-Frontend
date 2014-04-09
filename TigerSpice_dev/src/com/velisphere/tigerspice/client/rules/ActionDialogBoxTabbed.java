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

	
	  private VerticalPanel vp;
	  private TabPanel advanced;
	  private int index = 0;

	  public ActionDialogBoxTabbed(){
		  this.setWidget(tabWidget());
	  }
	 
	  public Widget tabWidget() {

   		rpcServiceProperty = GWT.create(PropertyService.class);

   	    Well well = new Well();
   	    Legend legend = new Legend("Actions");
   	    well.add(legend);
   	    well.setStyleName("wellsilver");
   	    
	 
	    advanced = new TabPanel();
	    
	    
	    addTab();
	    addTab();
	    advanced.selectTab(0);
	    well.add(advanced);
	    

	      return well;
	  }
	 
	  
	 
	  private void addTab() {

		 Tab vpTab = new Tab();
		 vpTab.setIcon(IconType.GEARS);
		 vpTab.setHeading("New Action");
		  
	    final TextBox txtManualValue = new TextBox();
		 vpTab.add(txtManualValue);
	    final ListBox lstSettingSource = new ListBox();
	    vpTab.add(lstSettingSource);
	    final ListBox lstSensorValue = new ListBox();
	    vpTab.add(lstSensorValue);
	    final ListBox lstValidValues = new ListBox();
	    vpTab.add(lstValidValues);
	    final TextBox txtTargetEndpointName = new TextBox();
	    vpTab.add(txtTargetEndpointName);
	    final TextBox txtTargetPropertyName = new TextBox();
	    vpTab.add(txtTargetPropertyName);
	    
	    advanced.add(vpTab);
	    
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

	
	
	
}





	





