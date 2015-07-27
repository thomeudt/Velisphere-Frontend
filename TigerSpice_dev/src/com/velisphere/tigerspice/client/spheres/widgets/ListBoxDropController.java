package com.velisphere.tigerspice.client.spheres.widgets;

/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.allen_sauer.gwt.dnd.client.drop.AbstractDropController;

/**
 * DropController for {@link DualListExample}.
 */
class ListBoxDropController extends AbstractDropController {

  private MouseListBox mouseListBox;

  ListBoxDropController(MouseListBox mouseListBox) {
    super(mouseListBox);
    this.mouseListBox = mouseListBox;
  }

  @Override
  public void onDrop(DragContext context) {
    MouseListBox from = (MouseListBox) context.draggable.getParent().getParent();
    
     
   
    for (Widget widget : context.selectedWidgets) {
      if (widget.getParent().getParent() == from) {
    	  
    	  if(widget instanceof EndpointDataLabel)
    	  {
    		 RootPanel.get().add(new HTML ("Class is " + context.selectedWidgets.get(0).getClass()));
    		 EndpointDataLabel labelClone = new EndpointDataLabel((EndpointDataLabel) widget);
    		
    		 if(from.isInSphere())
    		 {
    			 mouseListBox.add(labelClone);
    			 mouseListBox.removeFromDatabase(labelClone);
    		 }
    		 else
    		 {
    			 mouseListBox.add(labelClone);
    			 mouseListBox.addToDatabase(labelClone);
    		 } 
    		  
    	  }
    	  
    	  
    	
      }
      
      
    }
   
    
    super.onDrop(context);
  }

  @Override
  public void onPreviewDrop(DragContext context) throws VetoDragException {
    MouseListBox from = (MouseListBox) context.draggable.getParent().getParent();
    if (from == mouseListBox) {
      throw new VetoDragException();
    }
    super.onPreviewDrop(context);
  }
}