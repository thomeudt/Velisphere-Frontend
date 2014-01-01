package com.velisphere.tigerspice.shared;

import java.util.HashMap;
import java.util.HashSet;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class CheckPathObjectData extends VerticalLayoutContainer implements IsSerializable {

	public String text;
	public Boolean empty;
	public Anchor ancTextField;
	public String checkId;
	public Integer level;
	//HashSet<String> parentMultichecks;
	public HashSet<CheckPathObjectData> childMultichecks;
	public HashSet<CheckPathObjectData> childChecks;
	public String combination;
	public String ruleID;
	public Boolean isMulticheck;
	
	private CheckPathObjectData(){}
	
	public CheckPathObjectData (String checkID, String text, Boolean empty, Integer level)  {
		super();
		this.text = text;
		this.empty = empty;
		this.setBorders(true);
		this.setWidth(100);
		this.setHeight(40);
		this.level = level;
		this.setTitle(text);
		this.childMultichecks = new HashSet<CheckPathObjectData>();
		this.childChecks = new HashSet<CheckPathObjectData>();
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
	
	public void addChildMulticheck (CheckPathObjectData childMulticheck){
		this.childMultichecks.add(childMulticheck);
		
	}
	
	public void addChildCheck (CheckPathObjectData childCheck){
		this.childChecks.add(childCheck);
		
	}
	
}
