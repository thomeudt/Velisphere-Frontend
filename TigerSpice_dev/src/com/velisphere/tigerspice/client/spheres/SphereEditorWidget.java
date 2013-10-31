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

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
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
import com.velisphere.tigerspice.client.endpoints.EndpointView;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.users.LoginService;
import com.velisphere.tigerspice.client.users.NewAccountSuccessMessage;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.UserData;
import com.google.gwt.user.client.ui.Label;

public class SphereEditorWidget extends Composite {

	private EndpointServiceAsync rpcService;

	String sphereID;
	String sphereName;
	String userID;
	Vector<String> assignedEndpointIDs;
	private VerticalLayoutContainer sourceContainer;

	public SphereEditorWidget(final String sphereID, final String sphereName) {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();
		this.sphereID = sphereID;
		this.sphereName = sphereName;

		FlowLayoutContainer con = new FlowLayoutContainer();
		initWidget(con);

		HorizontalPanel hpMain = new HorizontalPanel();
		hpMain.setSpacing(10);

		HorizontalPanel hpHeader = new HorizontalPanel();
		hpHeader.setSpacing(10);

		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setBorders(true);
		container.setPixelSize(350, 300);

		sourceContainer = new VerticalLayoutContainer();
		sourceContainer.setPixelSize(250, 300);
		sourceContainer.setBorders(true);

		sourceContainer.setPosition(50, 0);

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
		target.setGroup("test");
		target.setOverStyle("drag-ok");

		// add existing endpoints to target container

		assignedEndpointIDs = new Vector<String>();
		rpcService = GWT.create(EndpointService.class);
		refreshTargetEndpoints(this.sphereID, container);

		// add unassigned endpoint to sourceContainer; THIS IS NOW DONE ON
		// SUCCESS AFTER LOADING ASSIGNED ENDPOINTS

		// refreshSourceEndpoints(this.sphereID, sourceContainer);

		final VerticalLayoutContainer leftHeader = new VerticalLayoutContainer();
		leftHeader.setPixelSize(400, 30);
		Paragraph leftP = new Paragraph();
		leftP.setText("Currently assigned:");
		leftHeader.add(leftP);

		final VerticalLayoutContainer rightHeader = new VerticalLayoutContainer();
		rightHeader.setPixelSize(350, 30);
		Paragraph rightP = new Paragraph();
		rightP.setText("Unassigned endpoints available to you:");
		rightHeader.add(rightP);

		Button btnAddProvisioned = new Button();
		btnAddProvisioned.setText("Add auto provisoned Endpoint");
		btnAddProvisioned.setType(ButtonType.SUCCESS);
		btnAddProvisioned.setSize(ButtonSize.SMALL);

		hpHeader.add(leftHeader);
		hpHeader.add(rightHeader);
		hpMain.add(container);
		hpMain.add(sourceContainer);
		con.add(hpHeader);
		con.add(hpMain);

		con.add(btnAddProvisioned);

	}

	public void refreshSourceEndpoints(final String sphereID,
			final VerticalLayoutContainer container) {

		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);

		// get userID for current session

		LoginService.Util.getInstance().loginFromSessionServer(
				new AsyncCallback<UserData>() {

					@Override
					public void onFailure(Throwable caught) {
						userID = null;
					}

					@Override
					public void onSuccess(UserData result) {
						if (result == null) {
							userID = null;

						} else

						{
							if (result.getLoggedIn()) {

								userID = result.userID;

								// get all endpoints for user id in current
								// session

								rpcService
										.getEndpointsForUser(
												userID,
												new AsyncCallback<Vector<EndpointData>>() {
													public void onFailure(
															Throwable caught) {
														Window.alert("Error"
																+ caught.getMessage());
													}

													@Override
													public void onSuccess(
															Vector<EndpointData> result) {

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
																		sphereID, sphereName, currentItem.endpointId, currentItem.endpointName));


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

																container
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
																source.setGroup("test");
															}
														}

													}

												});

							} else {
								userID = null;
							}
						}
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
				new AsyncCallback<Vector<EndpointData>>() {
					public void onFailure(Throwable caught) {
						Window.alert("Error" + caught.getMessage());
					}

					@Override
					public void onSuccess(Vector<EndpointData> result) {

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
									sphereID, sphereName, currentItem.endpointId, currentItem.endpointName));

							final Button buttonRemoveAssigned = new Button();
							buttonRemoveAssigned.setType(ButtonType.DANGER);
							buttonRemoveAssigned.setSize(ButtonSize.MINI);
							buttonRemoveAssigned.setText("remove");
							buttonRemoveAssigned
									.addClickHandler(new RemoveAssignedClickHandler(
											currentItem.endpointId, sphereID,
											container));

							final SafeHtmlBuilder builder = new SafeHtmlBuilder();
							builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
									+ "\">");
							builder.appendHtmlConstant("Drag "
									+ anchorAssigned.getText()
									+ " into a Sphere");
							builder.appendHtmlConstant("</div>");
							// final HTML html = new HTML(builder.toSafeHtml());

							// container.add(html, new MarginData(3));
							Column column = new Column(2);
							column.add(anchorAssigned);
							Column buttonColumn = new Column(1);
							buttonColumn.add(buttonRemoveAssigned);
							Row row = new Row();
							row.add(column);
							row.add(buttonColumn);

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


	public class OpenEndpointClickHandler implements ClickHandler {

		String sphereID;
		String sphereName;
		String endpointID;
		String endpointName;
		

		OpenEndpointClickHandler(String sphereID, String sphereName, String endpointID, String endpointName) {
			super();
			this.sphereID = sphereID;
			this.endpointID = endpointID;
			this.sphereName = sphereName;
			this.endpointName = endpointName;
			

		}

		public void onClick(ClickEvent event) {
			// Window.alert("Hello World: " + sphereID + endpointID);

			final AnimationLoading animationLoading = new AnimationLoading();
			showLoadAnimation(animationLoading);

			RootPanel.get("main").clear();
			EndpointView endpointView = new EndpointView(sphereID, sphereName, endpointID, endpointName);
			RootPanel.get("main").add(endpointView);
			
			
		}

	}



}
