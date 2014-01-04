package com.velisphere.tigerspice.shared;


import java.util.HashSet;



import java.util.LinkedHashSet;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class CheckPathObjectData implements IsSerializable {

	public String text;
	public Boolean empty;
	
	public String checkId;
	public Integer level;
	
	public HashSet<String> childMultichecks;
	public HashSet<String> childChecks;
	public String combination;
	public String ruleID;
	
	public Boolean isMulticheck;
	
	public CheckPathObjectData(){}
	
	public CheckPathObjectData (String checkId, String text, Boolean empty, Integer level)  {
		super();
		this.text = text;
		this.empty = empty;
		this.level = level;
		this.checkId = checkId;
		
		this.childMultichecks = new HashSet<String>();
		this.childChecks = new HashSet<String>();
		this.isMulticheck = false;
		
	
				
	}
	
	public void setText (String text){
		this.text = text;
	
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
	
	
}
