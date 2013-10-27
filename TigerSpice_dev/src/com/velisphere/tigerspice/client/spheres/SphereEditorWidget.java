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
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Anchor;
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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.shared.EndpointData;
import com.google.gwt.user.client.ui.Label;

public class SphereEditorWidget extends Composite {

	private EndpointServiceAsync rpcService;

	String sphereID;
	
	
	
	public SphereEditorWidget(String sphereID) {
		
		super(); 
		this.sphereID = sphereID;
		
		FlowLayoutContainer con = new FlowLayoutContainer();
		initWidget(con);

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(10);

		final FlowLayoutContainer container = new FlowLayoutContainer();
		container.setBorders(true);
		container.setPixelSize(300, 200);

		DropTarget target = new DropTarget(container) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				Anchor anchorUnassigned = (Anchor) event.getData();
				
				Column column = new Column(2);
				column.add(anchorUnassigned);
				Row row = new Row();
				row.add(column);
		
				container.add(row);

			}

		};
		target.setGroup("test");
		target.setOverStyle("drag-ok");

		final VerticalLayoutContainer sourceContainer = new VerticalLayoutContainer();
		sourceContainer.setWidth(100);

		// addSources(sourceContainer);
		rpcService = GWT.create(EndpointService.class);
		refreshEndpoints(this.sphereID, sourceContainer);

		TextButton reset = new TextButton("Reset");
		reset.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				container.clear();
				sourceContainer.clear();
				refreshEndpoints("1000", sourceContainer);

			}
		});

		hp.add(container);
		hp.add(sourceContainer);
		con.add(hp);
		con.add(reset, new MarginData(10));

	}

		public void refreshEndpoints(String sphereID,
			final VerticalLayoutContainer container) {

		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);
		
		rpcService.getEndpointsForSphere(sphereID,
				new AsyncCallback<Vector<EndpointData>>() {

			
					public void onFailure(Throwable caught) {
						Window.alert("Error" + caught.getMessage());
					}

					@Override
					public void onSuccess(Vector<EndpointData> result) {

						Iterator<EndpointData> it = result.iterator();
						removeLoadAnimation(animationLoading);

						while (it.hasNext()) {

							final EndpointData currentItem = it.next();
							final Anchor anchorUnassigned = new Anchor();
							anchorUnassigned.setText(currentItem.endpointName);
							anchorUnassigned.setHref("#");

							final SafeHtmlBuilder builder = new SafeHtmlBuilder();
							builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
									+ "\">");
							builder.appendHtmlConstant("Drag "
									+ anchorUnassigned.getText()
									+ " into a Sphere");
							builder.appendHtmlConstant("</div>");
							// final HTML html = new HTML(builder.toSafeHtml());

							// container.add(html, new MarginData(3));
							Column column = new Column(2);
							column.add(anchorUnassigned);
							Row row = new Row();
							row.add(column);
							
							container.add(row);

							DragSource source = new DragSource(anchorUnassigned) {
								@Override
								protected void onDragStart(
										DndDragStartEvent event) {
									super.onDragStart(event);
									// by default drag is allowed
									event.setData(anchorUnassigned);
									event.getStatusProxy().update(
											builder.toSafeHtml());
								}

							};
							// group is optional
							source.setGroup("test");
						}

					}

			

				});

	}

	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.removeFromParent();
	}

}
