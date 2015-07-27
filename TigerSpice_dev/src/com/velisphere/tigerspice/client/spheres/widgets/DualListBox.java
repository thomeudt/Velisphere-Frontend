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

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.allen_sauer.gwt.dnd.client.DragController;

import java.util.ArrayList;

/**
 * Example of two lists side by side for {@link DualListExample}.
 */
public class DualListBox extends AbsolutePanel {

  private static final String CSS_DEMO_DUAL_LIST_EXAMPLE_CENTER = "demo-DualListExample-center";

  private static final int LIST_SIZE = 10;

  private Button allLeft;

  private Button allRight;

  private ListBoxDragController dragController;

  private MouseListBox left;

  private Button oneLeft;

  private Button oneRight;

  private MouseListBox right;

  public DualListBox(int visibleItems) {
	  
	  
		FlowLayoutContainer con = new FlowLayoutContainer();
		add(con);

		Row hpMain = new Row();
		

		Row hpHeader = new Row();
		
		Column mainCol1 = new Column(4, 0);
		Column mainCol2 = new Column(4, 0);
		Column connectorCol = new Column(1, 0);
		Column headerCol1 = new Column(4, 0);
		Column headerCol2 = new Column(4, 1);
		
	  
	  
    

    VerticalPanel verticalPanel = new VerticalPanel();
    verticalPanel.addStyleName(CSS_DEMO_DUAL_LIST_EXAMPLE_CENTER);
    verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

    dragController = new ListBoxDragController(this);
    dragController.setBehaviorDragStartSensitivity(5);
    left = new MouseListBox(dragController, LIST_SIZE);
    right = new MouseListBox(dragController, LIST_SIZE);

    
    mainCol1.add(left);
    connectorCol.add(verticalPanel);
    mainCol2.add(right);
    
   
    
    
    oneRight = new Button(">");
    oneRight.setType(ButtonType.SUCCESS);
    oneLeft = new Button("<");
    oneLeft.setType(ButtonType.DANGER);
    verticalPanel.add(oneRight);
    verticalPanel.add(oneLeft);
    
    
    
    

    oneRight.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        moveItems(left, right, true);
      }
    });

    oneLeft.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        moveItems(right, left, true);
      }
    });

    ListBoxDropController leftDropController = new ListBoxDropController(left);
    ListBoxDropController rightDropController = new ListBoxDropController(right);
    dragController.registerDropController(leftDropController);
    dragController.registerDropController(rightDropController);
    
    
    HTML leftP = new HTML("Endpoints currently in this Sphere:");
	headerCol1.add(leftP);
	
	

	HTML rightP = new HTML("Endpoints currently outside of this Sphere:");
	headerCol2.add(rightP);

	
	
	hpHeader.add(headerCol1);
	hpHeader.add(headerCol2);
	
	

	hpMain.add(mainCol1);
	hpMain.add(connectorCol);
	hpMain.add(mainCol2);
		
	con.add(hpHeader);
	con.add(hpMain);
    
    
  }

  public void addLeft(String string) {
    left.add(string);
  }

  /**
   * Adds an widget to the left list box.
   * 
   * @param widget the text of the item to be added
   */
  public void addLeft(Widget widget) {
    left.add(widget);
 
  }
  
  public void addRight(String string) {
	  right.add(string);
	  }
  
  public void addRight(Widget widget) {
	    right.add(widget);
	  }


  public DragController getDragController() {
    return dragController;
  }

  protected void moveItems(MouseListBox from, MouseListBox to, boolean justSelectedItems) {
    ArrayList<Widget> widgetList = justSelectedItems ? dragController.getSelectedWidgets(from)
        : from.widgetList();
    for (Widget widget : widgetList) {
      // TODO let widget.removeFromParent() take care of from.remove()
      from.remove(widget);
      to.add(widget);
    }
  }
  
  
  
}