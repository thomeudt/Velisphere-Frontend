package com.velisphere.tigerspice.client.rules;

import java.util.HashMap;
import java.util.HashSet;

import com.github.gwtbootstrap.client.ui.Image;
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
	public Image actionIcon;
	public Image andIcon;
	public Image orIcon;
	public Hyperlink linkActionIcon;
	public String checkpathID;
	
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
		actionIcon = new Image();
		actionIcon.setResource(Images.INSTANCE.action());
		this.add(actionIcon);
		actionIcon.setVisible(false);
		linkActionIcon = new Hyperlink();
		linkActionIcon.getElement().appendChild(actionIcon.getElement());
		this.add(linkActionIcon);
		
		andIcon = new Image();
		andIcon.setResource(Images.INSTANCE.and());
		this.add(andIcon);
		andIcon.setVisible(false);

		orIcon = new Image();
		orIcon.setResource(Images.INSTANCE.or());
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
