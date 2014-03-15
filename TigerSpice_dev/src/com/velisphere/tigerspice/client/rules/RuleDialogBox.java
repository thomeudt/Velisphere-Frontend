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
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
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
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.CombinationConfig;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class RuleDialogBox extends PopupPanel {

	@UiField
	ListBox lstLinkedChecksID;
	@UiField
	ListBox lstCombination;
	@UiField
	Button btnSave;
	@UiField
	TextBox txtManualValue;
	@UiField
	TextBox txtMulticheckTitle;
	@UiField
	Legend lgdHeader;
	@UiField
	Button btnDelete;
		
	private String multicheckID;
	public String multicheckTitle;
	public String combination;
	public String ruleID;
	public HashMap<String, String> linkedChecks;
	public Boolean deleteFlag = false;
	

	private PropertyClassServiceAsync rpcServicePropertyClass;
	private CheckServiceAsync rpcServiceCheck;
	
	private AnimationLoading animationLoading = new AnimationLoading();

	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, RuleDialogBox> {
	}

	public RuleDialogBox() {
		
		
		setWidget(uiBinder.createAndBindUi(this));
		HashSet<String> combinations = new CombinationConfig().getCombinations();
		Iterator<String> it = combinations.iterator();
		while (it.hasNext()){
			this.lstCombination.addItem(it.next());
		}
	
	}
	
	public void setParameters (String multicheckID, String multicheckTitle, String combination, String ruleID, HashMap<String, String> linkedChecks){
		this.multicheckID = multicheckID;
		this.multicheckTitle = multicheckTitle;
		this.combination = combination;
		this.ruleID = ruleID;
		this.linkedChecks = linkedChecks;
		
		
		lstCombination.setSelectedValue(combination);
		txtMulticheckTitle.setText(multicheckTitle);
		Iterator<Entry<String, String>> it = linkedChecks.entrySet().iterator();
		while (it.hasNext()){
			Map.Entry<String, String> checkPair = (Map.Entry<String, String>)it.next();
			lstLinkedChecksID.addItem(checkPair.getValue(), checkPair.getKey());
		}
		lstLinkedChecksID.setVisibleItemCount(5);
		
	}

	
	@UiHandler("btnSave")
	void saveNewCheck (ClickEvent event) {
		this.multicheckTitle = this.txtMulticheckTitle.getText();
		this.ruleID = this.txtManualValue.getText();
		this.combination = this.lstCombination.getValue();
		this.hide();		
	}
	
	@UiHandler("btnDelete")
	void deleteCheck (ClickEvent event) {
		this.deleteFlag = true;
		this.hide();
		
		
	}

	
	
}