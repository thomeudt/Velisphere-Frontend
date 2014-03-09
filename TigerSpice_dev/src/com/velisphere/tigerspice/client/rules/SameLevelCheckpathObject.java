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

import java.util.HashMap;
import java.util.HashSet;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.base.IconAnchor;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Hyperlink;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.images.Images;

public class SameLevelCheckpathObject extends VerticalLayoutContainer implements IsSerializable {

	public String text;
	public Boolean empty;
	public Anchor ancTextField;
	public String checkId;
	public Integer level;
	//HashSet<String> parentMultichecks;
	public HashSet<SameLevelCheckpathObject> childMultichecks;
	public HashSet<SameLevelCheckpathObject> childChecks;
	public String combination = "notSet";
	public String ruleID;
	public Boolean isMulticheck;
	public IconAnchor actionIcon;
	public Image andIcon;
	public Image orIcon;
	public Icon sensorIcon;
	public Hyperlink linkActionIcon;
	public String checkpathID;
	public String propertyID;
	public String endpointID;
	public String triggerValue; 
	public String operator;
	
	private SameLevelCheckpathObject(){}
	
	public SameLevelCheckpathObject (String checkID, String text, Boolean empty, Integer level)  {
		super();
		this.text = text;
		this.empty = empty;
		this.setBorders(true);
		this.setWidth(100);
		this.setHeight(40);
		this.level = level;
		this.setTitle(text);
		this.childMultichecks = new HashSet<SameLevelCheckpathObject>();
		this.childChecks = new HashSet<SameLevelCheckpathObject>();
		this.isMulticheck = false;
		this.setCheckID(checkID);
		this.getElement().setAttribute("style", "background-color:aliceblue;");

		
	
		ancTextField = new Anchor();
		
		if(this.text.length()>25)
		{
			ancTextField.setText(this.text.substring(0, 25)+" (...)");
		}
		else
		{
			ancTextField.setText(this.text);
		}
		ancTextField.setHref("#");
		this.add(ancTextField);
		
		actionIcon = new IconAnchor();
		actionIcon.setIcon(IconType.PLAY);
		actionIcon.getElement().setAttribute("style", "color:orangered;");
		actionIcon.setTitle("There is at least one action triggered if this check is true");
		this.add(actionIcon);
		actionIcon.setVisible(false);
		
		
		// sensor icon is only used for base layer checks
		sensorIcon = new Icon();
		sensorIcon.setIcon(IconType.RSS);
		this.add(sensorIcon);
		sensorIcon.getElement().setAttribute("style", "color:cornflowerblue;");
		sensorIcon.setTitle("This is a sensor check. Click on it for more details.");
		sensorIcon.setVisible(false);
		

		
		
		linkActionIcon = new Hyperlink();
		linkActionIcon.getElement().appendChild(actionIcon.getElement());
		this.add(linkActionIcon);
		
		andIcon = new Image();
		andIcon.setResource(Images.INSTANCE.and());
		andIcon.setTitle("This logic check is true if all checks linked to this logic check are true");
		this.add(andIcon);
		andIcon.setVisible(false);

		orIcon = new Image();
		orIcon.setResource(Images.INSTANCE.or());
		orIcon.setTitle("This logic check is true if at least one check linked to this logic check is true");
		this.add(andIcon);
		orIcon.setVisible(false);
				
	}
	
	public void setText (String text){
		this.text = text;
		if(this.text.length()>25)
		{
			ancTextField.setText(this.text.substring(0, 25)+" (...)");
		}
		else
		{
			ancTextField.setText(this.text);
		}		
		this.setTitle(text);
	}
	
	public void setCheckID (String checkID){
		this.checkId = checkID;
		
	}
	
	public void setEmpty (Boolean empty){
		this.empty = empty;
		
	}
	
	public void setCheckpathID (String checkpathID){
		this.checkpathID = checkpathID;
		
	}
	
	public void setRuleID (String ruleID){
		this.ruleID = ruleID;
		
	}
	
	public void setCombination (String combination){
		this.combination = combination;
		
	}
	
	public void setIsMulticheck (boolean isMulticheck){
		this.isMulticheck = isMulticheck;
		
		
	}
	
	
	public void setPropertyID (String propertyID){
		this.propertyID = propertyID;
		
	}
	
	public void setEndpointID (String endpointID){
		this.endpointID = endpointID;
		
	}
	
	public void setTriggerValue (String triggerValue){
		this.triggerValue = triggerValue;
		
	}

	public void setOperator (String operator){
		this.operator = operator;
		
	}

	public String getPropertyID (){
		return this.propertyID;
		
	}
	
	public String getEndpointID (){
		return this.endpointID;
		
	}
	
	public String getTriggerValue (){
		return this.triggerValue;
		
	}

	public String getOperator (){
		return this.operator;
		
	}
	
	
	
	
	
	public void addChildMulticheck (SameLevelCheckpathObject childMulticheck){
		this.childMultichecks.add(childMulticheck);
		
	}
	
	public void addChildCheck (SameLevelCheckpathObject childCheck){
		this.childChecks.add(childCheck);
		
	}
	
	public void showActionIcon (){
		this.actionIcon.setVisible(true);
		
		
	}
	
	public void hideActionIcon (){
		this.actionIcon.setVisible(false);
		
	}
	
	public void showSensorIcon (){
		this.actionIcon.setVisible(true);
		
	}
	
	public void hideSensorIcon (){
		this.actionIcon.setVisible(false);
		
	}
	
	public void showAndIcon (){
		this.andIcon.setVisible(true);
		
		
	}
	
	public void showOrIcon (){
		this.orIcon.setVisible(true);
		
		
	}
	
	public void hideAndIcon (){
		this.andIcon.setVisible(false);
		
	}
	
	public void hideOrIcon (){
		this.orIcon.setVisible(false);
		
	}
	
	public Image getAndIcon (){
		return this.andIcon;
		
	}
	
}
