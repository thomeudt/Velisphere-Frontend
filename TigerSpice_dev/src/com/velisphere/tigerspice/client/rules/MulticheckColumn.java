package com.velisphere.tigerspice.client.rules;

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;

@SuppressWarnings("serial")
public class MulticheckColumn<T> extends LinkedList<T>{


	Boolean empty;
	
	public MulticheckColumn (Boolean empty){
		super();
		
		this.empty = empty;
					
	}
	
	
	public void setEmpty (Boolean empty){
		this.empty = empty;
		
	}
	
	public boolean getEmpty(){
		return this.empty;
		
	}
	
}
