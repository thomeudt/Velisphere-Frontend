package com.velisphere.tigerspice.client.rules;

import java.util.HashSet;

import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class SameLevelCheckpathObject extends VerticalLayoutContainer{

	String text;
	Boolean empty;
	Anchor ancTextField;
	String checkId;
	Integer level;
	//HashSet<String> parentMultichecks;
	HashSet<String> childMultichecks;
	HashSet<String> childChecks;
	String combination;
	String ruleID;
	Boolean isMulticheck;
	
	public SameLevelCheckpathObject (String checkID, String text, Boolean empty, Integer level){
		super();
		this.text = text;
		this.empty = empty;
		this.setBorders(true);
		this.setWidth(100);
		this.setHeight(40);
		this.level = level;
		this.setTitle(text);
		this.childMultichecks = new HashSet<String>();
		this.childChecks = new HashSet<String>();
		this.isMulticheck = false;
	
		ancTextField = new Anchor();
		
		if(this.text.length()>28)
		{
			ancTextField.setText(this.text.substring(0, 28)+" (...)");
		}
		else
		{
			ancTextField.setText(this.text);
		}
		ancTextField.setHref("#");
		this.add(ancTextField);
		
				
	}
	
	public void setText (String text){
		this.text = text;
		if(this.text.length()>28)
		{
			ancTextField.setText(this.text.substring(0, 28)+" (...)");
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
	
	public void setRuleID (String ruleID){
		this.ruleID = ruleID;
		
	}
	
	public void setCombination (String combination){
		this.combination = combination;
		
	}
	
	public void setIsMulticheck (boolean isMulticheck){
		this.isMulticheck = isMulticheck;
		
	}
	
	public void addChildMulticheck (String childMulticheck){
		this.childMultichecks.add(childMulticheck);
		
	}
	
	public void addChildCheck (String childCheck){
		this.childChecks.add(childCheck);
		
	}
	
}
