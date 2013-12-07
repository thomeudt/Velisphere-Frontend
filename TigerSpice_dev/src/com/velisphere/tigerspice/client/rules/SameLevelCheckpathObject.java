package com.velisphere.tigerspice.client.rules;

import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class SameLevelCheckpathObject extends VerticalLayoutContainer{

	String text;
	Boolean empty;
	Anchor ancTextField;
	String checkId;
	Integer level;
	
	public SameLevelCheckpathObject (String checkID, String text, Boolean empty, Integer level){
		super();
		this.text = text;
		this.empty = empty;
		this.setBorders(true);
		this.setWidth(100);
		this.level = level;
		this.setTitle(text);
		
	
		ancTextField = new Anchor();
		
		if(this.text.length()>11)
		{
			ancTextField.setText(this.text.substring(0, 11)+" (...)");
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
		if(this.text.length()>11)
		{
			ancTextField.setText(this.text.substring(0, 11)+" (...)");
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
	
}
