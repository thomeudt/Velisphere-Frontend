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

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
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
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.CombinationConfig;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.ActionObject;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class MulticheckDialogBox extends PopupPanel {

	@UiField
	ListBox lstLinkedChecksID;
	@UiField
	ListBox lstCombination;
	@UiField
	Button btnSave;	
	@UiField
	TextBox txtMulticheckTitle;
	@UiField
	Legend lgdHeader;
	@UiField
	Button btnDelete;
	@UiField
	TabPane tbpActions;
	@UiField
	TabPane tbpLogic;
	@UiField
	ActionDialogBoxTabbed wgtActionDialogBox;

	
	private String multicheckID;
	public String multicheckTitle;
	public String combination;
	public String ruleID;
	public HashMap<String, String> linkedChecks;
	public Boolean deleteFlag = false;
	public Boolean cancelFlag = false;
	private LinkedList<ActionObject> actions;
	

	

	private PropertyClassServiceAsync rpcServicePropertyClass;
	private CheckServiceAsync rpcServiceCheck;
	
	private AnimationLoading animationLoading = new AnimationLoading();

	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, MulticheckDialogBox> {
	}

	public MulticheckDialogBox(String multicheckID, String multicheckTitle, String combination, String ruleID, HashMap<String, String> linkedChecks, LinkedList<ActionObject> actions) {
		this.multicheckID = multicheckID;
		this.multicheckTitle = multicheckTitle;
		this.combination = combination;
		this.ruleID = ruleID;
		this.linkedChecks = linkedChecks;
		this.actions = actions;
		
				

		
		setWidget(uiBinder.createAndBindUi(this));
		btnDelete.setVisible(false);
		
		if (multicheckTitle != "") btnDelete.setVisible(true);
		lstCombination.setSelectedValue(combination);
		txtMulticheckTitle.setText(multicheckTitle);
		
		if (linkedChecks != null){
			Iterator<Entry<String, String>> it = linkedChecks.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry<String, String> checkPair = (Map.Entry<String, String>)it.next();
				lstLinkedChecksID.addItem(checkPair.getValue(), checkPair.getKey());
			}
			lstLinkedChecksID.setVisibleItemCount(5);
		}
		
		
		HashSet<String> combinations = new CombinationConfig().getCombinations();
		Iterator<String> cit = combinations.iterator();
		while (cit.hasNext()){
			this.lstCombination.addItem(cit.next());
		}
	
	}
	
/**
	public void setParameters (String multicheckID, String multicheckTitle, String combination, String ruleID, HashMap<String, String> linkedChecks, LinkedList<ActionObject> actions){
		this.multicheckID = multicheckID;
		this.multicheckTitle = multicheckTitle;
		this.combination = combination;
		this.ruleID = ruleID;
		this.linkedChecks = linkedChecks;
		this.actions = actions;
		
				
		if (multicheckTitle != "") btnDelete.setVisible(true);
		lstCombination.setSelectedValue(combination);
		txtMulticheckTitle.setText(multicheckTitle);
		Iterator<Entry<String, String>> it = linkedChecks.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, String> checkPair = (Map.Entry<String, String>)it.next();
			lstLinkedChecksID.addItem(checkPair.getValue(), checkPair.getKey());
		}
		lstLinkedChecksID.setVisibleItemCount(5);
		
	}
	**/

	public LinkedList<ActionObject> getActions(){
		return this.actions;
	}
	
	@UiHandler("btnSave")
	void saveNewCheck (ClickEvent event) {
		this.multicheckTitle = this.txtMulticheckTitle.getText();
		this.combination = this.lstCombination.getValue();
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
	
	public void setActionsTabEnabled(){
		tbpActions.setActive(true);
		tbpLogic.setActive(false);
	}
	
@UiFactory ActionDialogBoxTabbed makeActionEditor() { // method name is insignificant
				
		
		return new ActionDialogBoxTabbed(this.actions, this.multicheckTitle, true);
	  }

	
	
}
