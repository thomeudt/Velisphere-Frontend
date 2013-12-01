package com.velisphere.tigerspice.client.rules;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;

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
		
	
		ancTextField = new Anchor();
		ancTextField.setText(this.text);
		ancTextField.setHref("#");
		this.add(ancTextField);
		
				
	}
	
	public void setText (String text){
		this.text = text;
		ancTextField.setText(this.text);
	}
	
	public void setCheckID (String checkID){
		this.checkId = checkID;
		
	}
	
	public void setEmpty (Boolean empty){
		this.empty = empty;
		
	}
	
}
