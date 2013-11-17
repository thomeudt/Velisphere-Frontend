package com.velisphere.tigerspice.client.checks;

import java.util.Iterator;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.helper.DragobjectAccordion;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class CheckEditorWidget extends Composite {

	private String endpointID;
	private CheckServiceAsync rpcService;
	Accordion accordion = new Accordion();

	public CheckEditorWidget(final String endpointID) {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();
		this.endpointID = endpointID;

		FlowLayoutContainer con = new FlowLayoutContainer();
		initWidget(con);

		HorizontalPanel hpMain = new HorizontalPanel();
		hpMain.setSpacing(10);

		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setBorders(true);
		container.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
		// container.setHeight((int)((RootPanel.get().getOffsetHeight())/2.5));
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 4));

		hpMain.add(container);

		con.add(hpMain);

		rpcService = GWT.create(CheckService.class);
		updateCheckList(endpointID, container);

		DropTarget target = new DropTarget(accordion) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);

				// do the drag and drop visual action

				DragobjectAccordion dragAccordion = (DragobjectAccordion) event
						.getData();
				accordion.add(dragAccordion.accordionGroup);

				RootPanel rootPanel = RootPanel.get("main");
				rootPanel.getElement().getStyle()
						.setPosition(Position.RELATIVE);
				CheckNewDialogBox checkNewDialogBox = new CheckNewDialogBox(
						endpointID, dragAccordion.propertyID,
						dragAccordion.properyClassID,
						dragAccordion.propertyName);

				checkNewDialogBox.setModal(true);
				checkNewDialogBox.setAutoHideEnabled(true);

				checkNewDialogBox.setAnimationEnabled(true);

				checkNewDialogBox.setPopupPosition(
						(RootPanel.get().getOffsetWidth()) / 3,
						(RootPanel.get().getOffsetHeight()) / 4);
				checkNewDialogBox.show();

				checkNewDialogBox
						.addCloseHandler(new CloseHandler<PopupPanel>() {

							public void onClose(CloseEvent event) {

								updateCheckList(endpointID, container);
							}

						});

			}

		};
		target.setGroup("check");
		target.setOverStyle("drag-ok");

	}

	private void updateCheckList(final String endpointID,
			final VerticalLayoutContainer container) {
		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);

		container.clear();
		accordion.clear();

		rpcService.getChecksForEndpointID(

		endpointID, new AsyncCallback<Vector<CheckData>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method
				// stub
				removeLoadAnimation(animationLoading);
			}

			@Override
			public void onSuccess(Vector<CheckData> result) {

				Iterator<CheckData> it = result.iterator();

				while (it.hasNext()) {

					final CheckData currentItem = it.next();
					final AccordionGroup accordionGroup = new AccordionGroup();
					accordionGroup.setHeading(currentItem.getName());

					Row row0 = accordionRowBuilder(Images.INSTANCE.tag(),
							currentItem.getName(), "Name: ");
					accordionGroup.add(row0);

					Icon icnEditCheckName = new Icon();
					icnEditCheckName.setType(IconType.EDIT);
					row0.add(icnEditCheckName);
					final Anchor ancEditCheckName = new Anchor();
					ancEditCheckName.setText(" Edit");
					ancEditCheckName.setHref("#");
					row0.add(ancEditCheckName);
					final TextBox txtEditCheckName = new TextBox();
					final Button okButtonName = new Button();
					okButtonName.setText("OK");
					okButtonName.setType(ButtonType.SUCCESS);
					final PopupPanel editCheckNamePopup = new PopupPanel();

					ancEditCheckName.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							if (editCheckNamePopup.isShowing()) {

								editCheckNamePopup.removeFromParent();
							} else {
								txtEditCheckName.setText(currentItem.checkName);
								HorizontalPanel horizontalPanel = new HorizontalPanel();
								horizontalPanel.add(txtEditCheckName);
								horizontalPanel.add(okButtonName.asWidget());
								editCheckNamePopup.clear();
								editCheckNamePopup.add(horizontalPanel);
								editCheckNamePopup.setModal(true);
								editCheckNamePopup.setAutoHideEnabled(true);
								editCheckNamePopup.setAnimationEnabled(true);
								editCheckNamePopup
										.showRelativeTo(ancEditCheckName);
								txtEditCheckName.setFocus(true);
								okButtonName
										.addClickHandler(new ClickHandler() {
											public void onClick(ClickEvent event) {

												final String newCheckName = txtEditCheckName
														.getText();

												editCheckNamePopup.hide();
												final AnimationLoading animationLoading = new AnimationLoading();
												showLoadAnimation(animationLoading);

												rpcService
														.updateCheck(
																currentItem.checkId,
																newCheckName,
																currentItem.checkValue,
																currentItem.operator,
																new AsyncCallback<String>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		removeLoadAnimation(animationLoading);
																	}

																	@Override
																	public void onSuccess(
																			String result) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																		removeLoadAnimation(animationLoading);
																		updateCheckList(
																				endpointID,
																				container);

																	}

																});

											}
										});
							}
						}
					});

					Row row1 = accordionRowBuilder(Images.INSTANCE.tag(),
							currentItem.getPropertyId(),
							"Checking on Property: ");
					accordionGroup.add(row1);

					Row row2 = accordionRowBuilder(Images.INSTANCE.tag(),
							currentItem.getOperator(), "Operator: ");
					accordionGroup.add(row2);

					Icon icnEditOperator = new Icon();
					icnEditOperator.setType(IconType.EDIT);
					row2.add(icnEditOperator);
					final Anchor ancEditOperator = new Anchor();
					ancEditOperator.setText(" Edit");
					ancEditOperator.setHref("#");
					row2.add(ancEditOperator);
					final ListBox lstEditOperator = new ListBox();
					populateOperatorDropdown(currentItem.propertyId,
							lstEditOperator);

					final Button okButtonOperator = new Button();
					okButtonOperator.setText("OK");
					okButtonOperator.setType(ButtonType.SUCCESS);
					final PopupPanel editCheckOperatorPopup = new PopupPanel();

					ancEditOperator.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							if (editCheckNamePopup.isShowing()) {

								editCheckNamePopup.removeFromParent();
							} else {
								lstEditOperator
										.setSelectedValue(currentItem.operator);
								HorizontalPanel horizontalPanel = new HorizontalPanel();
								horizontalPanel.add(lstEditOperator);
								horizontalPanel.add(okButtonOperator.asWidget());
								editCheckOperatorPopup.clear();
								editCheckOperatorPopup.add(horizontalPanel);
								editCheckOperatorPopup.setModal(true);
								editCheckOperatorPopup.setAutoHideEnabled(true);
								editCheckOperatorPopup
										.setAnimationEnabled(true);
								editCheckOperatorPopup
										.showRelativeTo(ancEditCheckName);
								lstEditOperator.setFocus(true);
								okButtonOperator
										.addClickHandler(new ClickHandler() {
											public void onClick(ClickEvent event) {

												final String newCheckOperator = lstEditOperator
														.getValue();

												editCheckOperatorPopup.hide();
												final AnimationLoading animationLoading = new AnimationLoading();
												showLoadAnimation(animationLoading);

												rpcService
														.updateCheck(
																currentItem.checkId,
																currentItem.checkName,
																currentItem.checkValue,
																newCheckOperator,
																new AsyncCallback<String>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		removeLoadAnimation(animationLoading);
																	}

																	@Override
																	public void onSuccess(
																			String result) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																		removeLoadAnimation(animationLoading);
																		updateCheckList(
																				endpointID,
																				container);

																	}

																});

											}
										});
							}
						}
					});

					Row row3 = accordionRowBuilder(Images.INSTANCE.tag(),
							currentItem.getCheckValue(), "Trigger Value: ");
					accordionGroup.add(row3);

					Icon icnEditTriggerValue = new Icon();
					icnEditTriggerValue.setType(IconType.EDIT);
					// RootPanel.get().add(icnEditClassName,
					// pghEndpointName..getAbsoluteLeft(),
					// pghEndpointName.getAbsoluteTop());
					row3.add(icnEditTriggerValue);
					final Anchor ancEditTriggerValue = new Anchor();
					ancEditTriggerValue.setText(" Edit");
					ancEditTriggerValue.setHref("#");
					row3.add(ancEditTriggerValue);
					final TextBox txtEditTriggerValue = new TextBox();
					final Button okButtonCheckValue = new Button();
					okButtonCheckValue.setText("OK");
					okButtonCheckValue.setType(ButtonType.SUCCESS);
					final PopupPanel editTriggerValuePopup = new PopupPanel();

					ancEditTriggerValue.addClickHandler(new ClickHandler() {
						public void onClick(ClickEvent event) {

							if (editTriggerValuePopup.isShowing()) {

								editTriggerValuePopup.removeFromParent();
							} else {
								txtEditTriggerValue
										.setText(currentItem.checkValue);
								HorizontalPanel horizontalPanel = new HorizontalPanel();
								horizontalPanel.add(txtEditTriggerValue);
								horizontalPanel.add(okButtonCheckValue
										.asWidget());
								editTriggerValuePopup.clear();
								editTriggerValuePopup.add(horizontalPanel);
								editTriggerValuePopup.setModal(true);
								editTriggerValuePopup.setAutoHideEnabled(true);
								editTriggerValuePopup.setAnimationEnabled(true);
								editTriggerValuePopup
										.showRelativeTo(ancEditTriggerValue);
								txtEditTriggerValue.setFocus(true);
								okButtonCheckValue
										.addClickHandler(new ClickHandler() {
											public void onClick(ClickEvent event) {

												final String newTriggerValue = txtEditTriggerValue
														.getText();

												editTriggerValuePopup.hide();
												final AnimationLoading animationLoading = new AnimationLoading();
												showLoadAnimation(animationLoading);

												rpcService
														.updateCheck(
																currentItem.checkId,
																currentItem.checkName,
																newTriggerValue,
																currentItem.operator,
																new AsyncCallback<String>() {

																	@Override
																	public void onFailure(
																			Throwable caught) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub
																		removeLoadAnimation(animationLoading);
																	}

																	@Override
																	public void onSuccess(
																			String result) {
																		// TODO
																		// Auto-generated
																		// method
																		// stub

																		removeLoadAnimation(animationLoading);
																		updateCheckList(
																				endpointID,
																				container);

																	}

																});

											}
										});
							}
						}
					});

					String stateText = new String();
					if (currentItem.getState() == 1) {
						stateText = "True";
						accordionGroup.setBaseIcon(IconType.CHECK);
					} else {
						stateText = "Not True";
						accordionGroup.setBaseIcon(IconType.CHECK_EMPTY);
					}
					Row row4 = accordionRowBuilder(Images.INSTANCE.tag(),
							stateText, "Current State: ");
					accordionGroup.add(row4);

					Row row5 = new Row();
					Column column1 = new Column(1, 1);
					Button btnDeleteCheck = new Button();
					btnDeleteCheck.setText("Delete");
					btnDeleteCheck.addStyleName("btn-danger");
					btnDeleteCheck.addStyleName("btn-mini");
					column1.add(btnDeleteCheck);
					row5.add(column1);
					accordionGroup.add(row5);

					final String currentCheckID = currentItem.getCheckId();

					btnDeleteCheck.addClickHandler(new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {
							// TODO Auto-generated method stub

							showLoadAnimation(animationLoading);
							rpcService.deleteCheck(

							currentCheckID, new AsyncCallback<String>() {

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onSuccess(String result) {
									// TODO Auto-generated method stub
									updateCheckList(endpointID, container);
									removeLoadAnimation(animationLoading);
								}

							});
						}

					});

					accordion.add(accordionGroup);
					container.add(accordion);

				}

				AccordionGroup accordionGroupTarget = new AccordionGroup();
				accordionGroupTarget.setHeading("Drag here to add new check");
				accordionGroupTarget.setBaseIcon(IconType.PLUS);

				accordion.add(accordionGroupTarget);
				container.add(accordion);

				removeLoadAnimation(animationLoading);

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

	private Row accordionRowBuilder(ImageResource imRes, String checkItem,
			String itemDescriptor) {
		Column iconColumn = new Column(1);
		iconColumn.addStyleName("text-center");
		Image img = new Image();
		img.setResource(imRes);
		iconColumn.add(img);

		Column textColumn = new Column(2);
		DynamicAnchor propertyLine = new DynamicAnchor(itemDescriptor + " "
				+ checkItem, true, null);

		textColumn.add(propertyLine);
		Row row = new Row();
		row.add(iconColumn);
		row.add(textColumn);
		return row;

	}

	private void populateOperatorDropdown(String propertyID,
			final ListBox lstOperator) {

		final AnimationLoading animationLoading = new AnimationLoading();

		PropertyServiceAsync rpcServiceProperty;
		rpcServiceProperty = GWT.create(PropertyService.class);
		rpcServiceProperty.getPropertyClass(propertyID,
				new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			
					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

			
						String propertyClassID = result;
						
						PropertyClassServiceAsync rpcServicePropertyClass;
						rpcServicePropertyClass = GWT
								.create(PropertyClassService.class);
						rpcServicePropertyClass
								.getPropertyClassForPropertyClassID(
										propertyClassID,
										new AsyncCallback<PropertyClassData>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub
												removeLoadAnimation(animationLoading);
											}

											@Override
											public void onSuccess(
													PropertyClassData result) {
												// TODO Auto-generated method
												// stub

												DatatypeConfig dataTypeConfig = new DatatypeConfig();

												if (result.propertyClassDatatype
														.equalsIgnoreCase("string")) {
													Iterator<String> it = dataTypeConfig
															.getTextOperators()
															.iterator();
													while (it.hasNext()) {
														String listItem = it
																.next();
														lstOperator
																.addItem(listItem);

													}
												}

												else

												if (result.propertyClassDatatype
														.equalsIgnoreCase("byte")) {
													Iterator<String> it = dataTypeConfig
															.getNumberOperators()
															.iterator();
													while (it.hasNext()) {
														String listItem = it
																.next();
														lstOperator
																.addItem(listItem);

													}
												}

												else

												if (result.propertyClassDatatype
														.equalsIgnoreCase("long")) {
													Iterator<String> it = dataTypeConfig
															.getNumberOperators()
															.iterator();
													while (it.hasNext()) {
														String listItem = it
																.next();
														lstOperator
																.addItem(listItem);

													}
												}

												else

												if (result.propertyClassDatatype
														.equalsIgnoreCase("float")) {
													Iterator<String> it = dataTypeConfig
															.getNumberOperators()
															.iterator();
													while (it.hasNext()) {
														String listItem = it
																.next();
														lstOperator
																.addItem(listItem);

													}
												}

												else

												if (result.propertyClassDatatype
														.equalsIgnoreCase("double")) {
													Iterator<String> it = dataTypeConfig
															.getNumberOperators()
															.iterator();
													while (it.hasNext()) {
														String listItem = it
																.next();
														lstOperator
																.addItem(listItem);

													}
												}

												else

												if (result.propertyClassDatatype
														.equalsIgnoreCase("boolean")) {
													Iterator<String> it = dataTypeConfig
															.getBooleanOperators()
															.iterator();
													while (it.hasNext()) {
														String listItem = it
																.next();
														lstOperator
																.addItem(listItem);

													}
												}

												else
													lstOperator
															.addItem("Invalid Endpoint Configuration");

												lstOperator
														.setVisibleItemCount(1);

												// lstOperator.addItem(result.propertyClassName);
												removeLoadAnimation(animationLoading);

											}

										});

						lstOperator.setEnabled(true);
						lstOperator.setSelectedIndex(1);

					}

					
				});
	}
}
