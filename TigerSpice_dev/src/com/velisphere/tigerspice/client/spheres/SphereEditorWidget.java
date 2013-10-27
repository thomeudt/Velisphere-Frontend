/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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
package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.google.gwt.user.client.ui.Label;

 

public class SphereEditorWidget extends Composite {
	public SphereEditorWidget() {
	    FlowLayoutContainer con = new FlowLayoutContainer();
	    initWidget(con);
		 
	    HorizontalPanel hp = new HorizontalPanel();
	    hp.setSpacing(10);

	 
	    final FlowLayoutContainer container = new FlowLayoutContainer();
	    container.setBorders(true);
	    container.setPixelSize(200, 200);

	    
	    DropTarget target = new DropTarget(container) {
	      @Override
	      protected void onDragDrop(DndDropEvent event) {
	        super.onDragDrop(event);
	        HTML html = (HTML) event.getData();
	        container.add(html);
	      }
	 
	    };
	    target.setGroup("test");
	    target.setOverStyle("drag-ok");
	 
	    final FlowLayoutContainer sourceContainer = new FlowLayoutContainer();
	    sourceContainer.setWidth(100);
	 
	    addSources(sourceContainer);
	 
	    TextButton reset = new TextButton("Reset");
	    reset.addSelectHandler(new SelectHandler() {
	 
	      @Override
	      public void onSelect(SelectEvent event) {
	        container.clear();
	        sourceContainer.clear();
	        addSources(sourceContainer);
	      }
	    });
	 
	    hp.add(container);
	    hp.add(sourceContainer);
	    con.add(hp);
	    con.add(reset, new MarginData(10));
			
	  }
	 
	 
	  private void addSources(FlowLayoutContainer container) {
	    for (int i = 0; i < 5; i++) {
	      final SafeHtmlBuilder builder = new SafeHtmlBuilder();
	      builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
	          +  "\">");
	      builder.appendHtmlConstant("Drag Me " + i);
	      builder.appendHtmlConstant("</div>");
	      final HTML html = new HTML(builder.toSafeHtml());
	      
	      container.add(html, new MarginData(3));
	 
	      DragSource source = new DragSource(html) {
	        @Override
	        protected void onDragStart(DndDragStartEvent event) {
	          super.onDragStart(event);
	          // by default drag is allowed
	          event.setData(html);
	          event.getStatusProxy().update(builder.toSafeHtml());
	        }
	 
	      };
	      // group is optional
	      source.setGroup("test");
	    }
		

	  }

	}
