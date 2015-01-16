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
package com.velisphere.tigerspice.client.spheres;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport;
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
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointView;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.event.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.NewAccountSuccessMessage;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.UserData;
import com.google.gwt.user.client.ui.Label;

public class SphereEditorWidget extends Composite {

	private EndpointServiceAsync rpcService;

	String sphereID;
	String sphereName;
	private HandlerRegistration sessionHandler; 

	Vector<String> assignedEndpointIDs;
	private VerticalLayoutContainer sourceContainer;
	private VerticalLayoutContainer container = new VerticalLayoutContainer();

	public SphereEditorWidget(final String sphereID, final String sphereName) {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();
		this.sphereID = sphereID;
		// this.sphereName = sphereName;
		setSphereName();

		FlowLayoutContainer con = new FlowLayoutContainer();
		initWidget(con);

		Row hpMain = new Row();
		

		Row hpHeader = new Row();
		
		Column mainCol1 = new Column(5, 0);
		Column mainCol2 = new Column(5, 0);
		Column headerCol1 = new Column(5, 0);
		Column headerCol2 = new Column(5, 0);
		
		container.setBorders(true);
		//container.setWidth((int)((RootPanel.get().getOffsetWidth())/4));
		container.setWidth("100%");
		container.setHeight((int)((RootPanel.get().getOffsetHeight())/2.5));
		container.setScrollMode(ScrollSupport.ScrollMode.AUTOY);

		sourceContainer = new VerticalLayoutContainer();
		//sourceContainer.setWidth((int)((RootPanel.get().getOffsetWidth())/4));
		sourceContainer.setWidth("100%");
		
		sourceContainer.setHeight((int)((RootPanel.get().getOffsetHeight())/2.5));
		sourceContainer.setBorders(true);
		sourceContainer.setScrollMode(ScrollSupport.ScrollMode.AUTOY);


		DropTarget sourceTarget = new DropTarget(sourceContainer) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);

				// do the drag and drop visual action

				final DynamicAnchor anchorAssigned = (DynamicAnchor) event
						.getData();
				
				Column column = new Column(2);
				column.add(anchorAssigned);
				Row row = new Row();
				row.add(column);
				sourceContainer.add(row);
				
				// update the database
				removeAssigned(sphereID, anchorAssigned.getStringQueryFirst());
				
				
			}

		};
		sourceTarget.setGroup("sphereToEndpoint");
		sourceTarget.setOverStyle("drag-ok");

		
		
		
		DropTarget target = new DropTarget(container) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);

				// do the drag and drop visual action

				final DynamicAnchor anchorUnassigned = (DynamicAnchor) event
						.getData();
				
				Column column = new Column(2);
				column.add(anchorUnassigned);
				Row row = new Row();
				row.add(column);
				container.add(row);

				// update the database
				final AnimationLoading animationLoading = new AnimationLoading();
				showLoadAnimation(animationLoading);
				rpcService.addEndpointToSphere(
						anchorUnassigned.getStringQueryFirst(), sphereID,
						new AsyncCallback<String>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								removeLoadAnimation(animationLoading);
							}

							@Override
							public void onSuccess(String result) {
								// TODO Auto-generated method stub
								removeLoadAnimation(animationLoading);

								Label resultMessage = new Label();
								resultMessage.setText(anchorUnassigned
										.getText() + " successfully added.");
								resultMessage.setStyleName("label-success");
								Column column = new Column(7, 1);
								column.add(resultMessage);
								Row row = new Row();
								row.add(column);
								if (RootPanel.get("main").getWidgetCount() > 1)
									RootPanel.get("main").remove(1);
								RootPanel.get("main").add(row);
								refreshTargetEndpoints(sphereID, container);

							}

						});

			}

		};
		target.setGroup("endpointToSphere");
		target.setOverStyle("drag-ok");

		// add existing endpoints to target container

		assignedEndpointIDs = new Vector<String>();
		rpcService = GWT.create(EndpointService.class);
		refreshTargetEndpoints(this.sphereID, container);

		// add unassigned endpoint to sourceContainer; THIS IS NOW DONE ON
		// SUCCESS AFTER LOADING ASSIGNED ENDPOINTS

		// refreshSourceEndpoints(this.sphereID, sourceContainer);

		HTML leftP = new HTML("Endpoints currently in this Sphere:");
		headerCol1.add(leftP);
		
		

		HTML rightP = new HTML("Endpoints currently outside of this Sphere:");
		headerCol2.add(rightP);

		
		
		hpHeader.add(headerCol1);
		hpHeader.add(headerCol2);
		
		
		mainCol1.add(container);
		mainCol2.add(sourceContainer);
		hpMain.add(mainCol1);
		hpMain.add(mainCol2);
			
		con.add(hpHeader);
		con.add(hpMain);

		

	}

	public void refreshSourceEndpoints(final String sphereID,
			final VerticalLayoutContainer sourceContainer) {

		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);

		sourceContainer.clear();
		
		// validate and get userID for current session
		
		SessionHelper.validateCurrentSession();
	
		sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
		    	
		@Override
	    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
			

								// get all endpoints for user id in current
								// session
								sessionHandler.removeHandler();
			
								rpcService
										.getEndpointsForUser(
												SessionHelper.getCurrentUserID(),
												new AsyncCallback<LinkedList<EndpointData>>() {
													public void onFailure(
															Throwable caught) {
														Window.alert("Error"
																+ caught.getMessage());
													}

													@Override
													public void onSuccess(
															LinkedList<EndpointData> result) {

														Iterator<EndpointData> it = result
																.iterator();
														removeLoadAnimation(animationLoading);

														while (it.hasNext()) {

															final EndpointData currentItem = it
																	.next();

															// this is the code
															// for the d&d
															// target, i.e.
															// enpoints already
															// part of the
															// sphere

															// this is the code
															// for the d&d
															// source, needs to
															// be
															// changed to only
															// these endpoints
															// not yet part of
															// the sphere

															if (assignedEndpointIDs
																	.contains(currentItem.endpointId) == false) {
																final DynamicAnchor anchorUnassigned = new DynamicAnchor(
																		currentItem.endpointName
																				+ " (owned) in:",
																		true,
																		currentItem.endpointId);
																
																anchorUnassigned.addClickHandler(new OpenEndpointClickHandler(
																		sphereID, sphereName, currentItem.endpointId, currentItem.endpointName, currentItem.endpointclassId));


																final SafeHtmlBuilder builder = new SafeHtmlBuilder();
																builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
																		+ "\">");
																builder.appendHtmlConstant("Drag "
																		+ anchorUnassigned
																				.getText()
																		+ " into a Sphere");
																builder.appendHtmlConstant("</div>");
																// final HTML
																// html = new
																// HTML(builder.toSafeHtml());

																// container.add(html,
																// new
																// MarginData(3));
																Column column = new Column(
																		3);
																column.add(anchorUnassigned);
																Row row = new Row();
																row.add(column);

																sourceContainer
																		.add(row);

																DragSource source = new DragSource(
																		anchorUnassigned) {
																	@Override
																	protected void onDragStart(
																			DndDragStartEvent event) {
																		super.onDragStart(event);
																		// by
																		// default
																		// drag
																		// is
																		// allowed
																		event.setData(anchorUnassigned);
																		event.getStatusProxy()
																				.update(builder
																						.toSafeHtml());
																	}

																};
																// group is
																// optional
																source.setGroup("endpointToSphere");
															}
														}

													}

												});

		}
		});
	}

	public void refreshTargetEndpoints(final String sphereID,
			final VerticalLayoutContainer container) {

		// show animation while rpc is processed

		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);

		// clear current list to avoid duplicates

		container.clear();

		// query endpoints that are already part of the current sphere

		rpcService.getEndpointsForSphere(sphereID,
				new AsyncCallback<LinkedList<EndpointData>>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error" + caught.getMessage());
					}

					@Override
					public void onSuccess(LinkedList<EndpointData> result) {

						Iterator<EndpointData> it = result.iterator();
						removeLoadAnimation(animationLoading);
						assignedEndpointIDs.clear();

						while (it.hasNext()) {

							final EndpointData currentItem = it.next();

							// this is the code for the d&d target, i.e.
							// enpoints already part of the sphere

							// this is the code for the d&d source, needs to be
							// changed to only these endpoints not yet part of
							// the sphere

							// First, we add this list to the vector of assigned
							// endpoint

							assignedEndpointIDs.add(currentItem.endpointId);

							final DynamicAnchor anchorAssigned = new DynamicAnchor(
									currentItem.endpointName, true,
									currentItem.endpointId);
							anchorAssigned.addClickHandler(new OpenEndpointClickHandler(
									sphereID, sphereName, currentItem.endpointId, currentItem.endpointName, currentItem.endpointclassId));

							/*
							final Button buttonRemoveAssigned = new Button();
							buttonRemoveAssigned.setType(ButtonType.DANGER);
							buttonRemoveAssigned.setSize(ButtonSize.MINI);
							buttonRemoveAssigned.setText("remove");
							buttonRemoveAssigned
									.addClickHandler(new RemoveAssignedClickHandler(
											currentItem.endpointId, sphereID,
											container));
											*/

							final SafeHtmlBuilder builder = new SafeHtmlBuilder();
							builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
									+ "\">");
							builder.appendHtmlConstant("Drag "
									+ anchorAssigned.getText()
									+ " into a Sphere");
							builder.appendHtmlConstant("</div>");

							DragSource source = new DragSource(
									anchorAssigned) {
								@Override
								protected void onDragStart(
										DndDragStartEvent event) {
									super.onDragStart(event);
									// by
									// default
									// drag
									// is
									// allowed
									event.setData(anchorAssigned);
									event.getStatusProxy()
											.update(builder
													.toSafeHtml());
								}

							};
							// group is
							// optional
							source.setGroup("sphereToEndpoint");

							
							
							Column column = new Column(4);
							column.add(anchorAssigned);
							// Column buttonColumn = new Column(1);
							//buttonColumn.add(buttonRemoveAssigned);
							Row row = new Row();
							row.add(column);
							// row.add(buttonColumn);

							container.add(row);

						}
						sourceContainer.clear();
						refreshSourceEndpoints(sphereID, sourceContainer);

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

	/*
	public class RemoveAssignedClickHandler implements ClickHandler {

		String sphereID;
		String endpointID;
		VerticalLayoutContainer assignedListContainer;

		RemoveAssignedClickHandler(String endpointID, String sphereID,
				VerticalLayoutContainer assignedListContainer) {
			super();
			this.sphereID = sphereID;
			this.endpointID = endpointID;
			this.assignedListContainer = assignedListContainer;

		}

		public void onClick(ClickEvent event) {
			// Window.alert("Hello World: " + sphereID + endpointID);

			final AnimationLoading animationLoading = new AnimationLoading();
			showLoadAnimation(animationLoading);

			rpcService.removeEndpointFromSphere(endpointID, sphereID,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							removeLoadAnimation(animationLoading);
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							removeLoadAnimation(animationLoading);

							refreshTargetEndpoints(sphereID,
									assignedListContainer);

						}

					});

		}

	}

*/
	public class OpenEndpointClickHandler implements ClickHandler {

		private String sphereID;
		private String sphereName;
		private String endpointID;
		private String endpointName;
		private String endpointClassID;
		

		OpenEndpointClickHandler(String sphereID, String sphereName, String endpointID, String endpointName, String endpointClassId) {
			super();
			this.sphereID = sphereID;
			this.endpointID = endpointID;
			this.sphereName = sphereName;
			this.endpointName = endpointName;
			this.endpointClassID = endpointClassId;
			

		}

		public void onClick(ClickEvent event) {
			// Window.alert("Hello World: " + sphereID + endpointID);

			final AnimationLoading animationLoading = new AnimationLoading();
			showLoadAnimation(animationLoading);

			RootPanel.get("main").clear();
			EndpointView endpointView = new EndpointView(sphereID, sphereName, endpointID, endpointName, endpointClassID);
			RootPanel.get("main").add(endpointView);
			
			
		}

	}
	
	public void removeAssigned(final String sphereID, String endpointID) {
	
		
			final AnimationLoading animationLoading = new AnimationLoading();
			showLoadAnimation(animationLoading);

			rpcService.removeEndpointFromSphere(endpointID, sphereID,
					new AsyncCallback<String>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							removeLoadAnimation(animationLoading);
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
						
							// refresh the views
							// target endpoint list first to ensure proper reflection of drag state
							
							refreshTargetEndpoints(sphereID, container);
								
							
							removeLoadAnimation(animationLoading);
						
		
						}

					});

		

	}

	private void setSphereName(){
		SphereServiceAsync rpcServiceSphere = GWT.create(SphereService.class);
		rpcServiceSphere.getSphereNameForSphereID(sphereID,
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						sphereName = result;
						
						

					}

				});

		
	}
	


}
