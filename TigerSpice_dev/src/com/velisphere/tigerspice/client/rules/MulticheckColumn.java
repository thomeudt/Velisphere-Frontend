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

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;

@SuppressWarnings("serial")
public class MulticheckColumn<SameLevelCheckpathObject> extends LinkedList<SameLevelCheckpathObject> implements IsSerializable{

	private MulticheckColumn(){}

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
