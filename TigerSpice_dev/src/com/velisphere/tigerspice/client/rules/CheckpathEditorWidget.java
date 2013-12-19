package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.UUID;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.checks.CheckNewDialogBox;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.helper.ColorHelper;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;

public class CheckpathEditorWidget extends Composite {

	Accordion accordion = new Accordion();
	Paragraph pgpFirstCheck;
	Paragraph pgpAddSameLevel;
	HorizontalLayoutContainer firstCheckRow;
	FlowLayoutContainer con;
	VerticalLayoutContainer addNextLevelField;
	// VerticalLayoutContainer checkColumn;
	LinkedHashSet<SameLevelCheckpathObject> checkHashSet;
	// LinkedList<SameLevelCheckpathObject> multicheckLinkedList;
	LinkedList<MulticheckColumn<SameLevelCheckpathObject>> multicheckColumns;

	private UuidServiceAsync rpcServiceUuid; 
	
	public CheckpathEditorWidget() {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();


		rpcServiceUuid = GWT.create(UuidService.class);
		
		con = new FlowLayoutContainer();
		initWidget(con);

		checkHashSet = new LinkedHashSet<SameLevelCheckpathObject>();
 
		multicheckColumns = new LinkedList<MulticheckColumn<SameLevelCheckpathObject>>();

		rebuildCheckpathDiagram();

	}

	private void rebuildCheckpathDiagram() {

		// Draw basic layout boxes

		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setBorders(true);
		container.setScrollMode(ScrollSupport.ScrollMode.AUTO);
		container.setHeight((int) ((RootPanel.get().getOffsetHeight()) / 2.5));
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 4));

		firstCheckRow = new HorizontalLayoutContainer();
		firstCheckRow.setBorders(false);
		// firstCheckRow.setScrollMode(ScrollSupport.ScrollMode.AUTOX);

		Iterator<SameLevelCheckpathObject> it = checkHashSet.iterator();

		// draw first d&d target field if checkHashSet is empty

		if (it.hasNext() == false) {

			final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
					null, "drag check here", true, 0);
			firstCheckRow.add(addCheckField);

			DropTarget target = new DropTarget(addCheckField) {
				@Override
				protected void onDragDrop(DndDropEvent event) {
					super.onDragDrop(event);

					// do the drag and drop visual action

					DragobjectContainer dragAccordion = (DragobjectContainer) event
							.getData();

					addCheckField.setText(dragAccordion.checkName);
					addCheckField.setCheckID(dragAccordion.checkID);
					checkHashSet.add(addCheckField);

					SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
							null, "drag check here", true, 1);

					// also add an entire multicheck column
					MulticheckColumn<SameLevelCheckpathObject> multicheckList = new MulticheckColumn<SameLevelCheckpathObject>(
							true);
					multicheckList.add(addNextLevelField);
					multicheckColumns.add(multicheckList);
					rebuildCheckpathDiagram();

				}

			};
			target.setGroup("firstlevel");
			target.setOverStyle("drag-ok");

		}

		// draw the first level - "checks" - as the foundation layer by reading
		// the data stored in the checkHashSet

		while (it.hasNext()) {
			final SameLevelCheckpathObject currentObject = it.next();

			final SafeHtmlBuilder builder = new SafeHtmlBuilder();
			builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
					+ "\">");
			builder.appendHtmlConstant("Drag " + currentObject.text
					+ " into next level to build a tree");
			builder.appendHtmlConstant("</div>");

			DragSource source = new DragSource(currentObject) {
				@Override
				protected void onDragStart(DndDragStartEvent event) {
					super.onDragStart(event);
					// by
					// default
					// drag
					// is
					// allowed

					DragobjectContainer dragAccordion = new DragobjectContainer();
					dragAccordion.checkID = currentObject.checkId;
					dragAccordion.checkName = currentObject.text;

					event.setData(dragAccordion);
					event.getStatusProxy().update(builder.toSafeHtml());
				}

			};
			source.setGroup("multicheck");

			firstCheckRow.add(currentObject);
			VerticalLayoutContainer horizontalSpacer = new VerticalLayoutContainer();
			horizontalSpacer.setBounds(13, 20, 13, 20);
			firstCheckRow.add(horizontalSpacer);
			firstCheckRow.add(new Icon(IconType.CHEVRON_RIGHT));

			// if no more items are left in the check hashset, draw a new drag
			// target box as the last step to allow for adding a new check to
			// the checkpath

			if (it.hasNext() == false) {

				final SameLevelCheckpathObject addCheckField = new SameLevelCheckpathObject(
						null, "drag check here", true, 0);
				firstCheckRow.add(addCheckField);

				DropTarget target = new DropTarget(addCheckField) {
					@Override
					protected void onDragDrop(DndDropEvent event) {
						super.onDragDrop(event);

						// do the drag and drop visual action

						DragobjectContainer dragAccordion = (DragobjectContainer) event
								.getData();

						addCheckField.setText(dragAccordion.checkName);
						addCheckField.setCheckID(dragAccordion.checkID);
						checkHashSet.add(addCheckField);

						/*
						 * addNextLevelField = new VerticalLayoutContainer();
						 * addNextLevelField.setBorders(true);
						 * addNextLevelField.setWidth((int) (100));
						 * 
						 * Paragraph pghAddNextLevel = new Paragraph();
						 * pghAddNextLevel.setText("add next level");
						 * addNextLevelField.add(pghAddNextLevel);
						 */
						rebuildCheckpathDiagram();

					}

				};
				target.setGroup("firstlevel");
				target.setOverStyle("drag-ok");

			}

		}

		// check if new column needs to be added

		if (multicheckColumns.size() > 0) {
			System.out.println("Spaltenzahl:" + multicheckColumns.size());

			if (multicheckColumns.getLast().getEmpty() == false) {

				SameLevelCheckpathObject addNextColumnField = new SameLevelCheckpathObject(
						null, "drag check here", true, 1);

				MulticheckColumn<SameLevelCheckpathObject> newMulticheckList = new MulticheckColumn<SameLevelCheckpathObject>(
						true);
				newMulticheckList.add(addNextColumnField);
				multicheckColumns.add(newMulticheckList);

				DropTarget columnTarget = new DropTarget(addNextColumnField) {
					@Override
					protected void onDragDrop(DndDropEvent event) {
						super.onDragDrop(event);

						// do the drag and drop visual action

						DragobjectContainer dragAccordion = (DragobjectContainer) event
								.getData();

						// addCheckField.setText(dragAccordion.checkName);
						// addCheckField.setCheckID(dragAccordion.checkID);
						// checkHashSet.add(addCheckField);

						addNextLevelField = new VerticalLayoutContainer();
						addNextLevelField.setBorders(true);
						addNextLevelField.setWidth((int) (100));

						Paragraph pghAddNextLevel = new Paragraph();
						pghAddNextLevel.setText("add next level");
						addNextLevelField.add(pghAddNextLevel);

						multicheckColumns.getLast().setEmpty(false);

						rebuildCheckpathDiagram();

					}

				};
				columnTarget.setGroup("multicheck");
				columnTarget.setOverStyle("drag-ok");
				rebuildCheckpathDiagram();

			}

		}

		// when the first layer is done, now add the vertical layers containing
		// multichecks, in the reverse order of their addition

		HorizontalLayoutContainer hLC = new HorizontalLayoutContainer();

		if (multicheckColumns.size() > 0) {
			hLC.setHeight((4 * 65));
		}

		int countOfMulticheckColumns = multicheckColumns.size();

		for (int i = 1; i <= countOfMulticheckColumns; i = i + 1) {
			hLC.add(drawMulticheckColumn(i - 1));
		}

		// firstCheckColumn.add(firstCheckRow);

		// container.add(checkColumn);
		container.add(hLC);
		container.add(firstCheckRow);

		con.clear();
		con.add(container);

	}

	private VerticalLayoutContainer drawMulticheckColumn(final int columnElement) {
		VerticalLayoutContainer checkColumn = new VerticalLayoutContainer();
		checkColumn.setWidth((int) (100));
		checkColumn.setBounds(100, 20, 120, 20);

		LinkedList<SameLevelCheckpathObject> multicheckLinkedList = multicheckColumns
				.get(columnElement);

		Iterator<SameLevelCheckpathObject> mit = multicheckLinkedList
				.iterator();

		for (int i = multicheckLinkedList.size(); i < 4; i = i + 1)
			if (multicheckLinkedList.size() < 4) {
				SameLevelCheckpathObject spacer = new SameLevelCheckpathObject(
						null, "", true, i);
				spacer.getElement().getStyle().setBorderColor("#FFFFFF");
				spacer.setBorders(false);
				spacer.setHeight(60);
				checkColumn.add(spacer);
				// checkColumn.add(new Icon(IconType.CHEVRON_UP));
			}

		
		while (mit.hasNext()) {
			final SameLevelCheckpathObject currentObject = mit.next();

			checkColumn.add(currentObject);
			checkColumn.add(new Icon(IconType.CHEVRON_UP));

			DropTarget target = new DropTarget(currentObject) {
				@Override
				protected void onDragDrop(DndDropEvent event) {
					super.onDragDrop(event);

					// do the drag and drop visual action

					final DragobjectContainer dragAccordion = (DragobjectContainer) event
							.getData();

					// action that is triggered if check is dragged into an
					// empty target field
					if (currentObject.checkId == null) {
						
						
						multicheckColumns.get(columnElement).remove(
								currentObject);
						currentObject.setText(dragAccordion.checkName);

						currentObject.setEmpty(false);
						currentObject.getElement().getStyle()
								.setBackgroundColor(ColorHelper.randomColor());
						multicheckColumns.get(columnElement).setEmpty(false);

						
						final MulticheckNewDialogBox multicheckNewDialogBox = new MulticheckNewDialogBox();

						multicheckNewDialogBox.setModal(true);
						multicheckNewDialogBox.setAutoHideEnabled(true);

						multicheckNewDialogBox.setAnimationEnabled(true);

						multicheckNewDialogBox.setPopupPosition(
								(RootPanel.get().getOffsetWidth()) / 3,
								(RootPanel.get().getOffsetHeight()) / 4);
						multicheckNewDialogBox.show();

						multicheckNewDialogBox
								.addCloseHandler(new CloseHandler<PopupPanel>() {

									public void onClose(CloseEvent event) {

										rpcServiceUuid.getUuid(new AsyncCallback<String>() {

													@Override
													public void onFailure(Throwable caught) {
														// TODO Auto-generated method stub
														
													}

													@Override
													public void onSuccess(String result) {
														// TODO Auto-generated method stub
														
														currentObject.setCheckID(result);
														System.out.println("ID: "
																+ currentObject.checkId);
													}

												});

										

										currentObject
												.setText(multicheckNewDialogBox.multicheckTitle);
										currentObject
												.setCombination(multicheckNewDialogBox.combination);
										currentObject
												.addChildMulticheck(dragAccordion.checkID);
										
										System.out.println("Title: "
												+ currentObject.text);
										System.out.println("Combination: "
												+ currentObject.combination);
										System.out
												.println("Child Multichecks: "
														+ currentObject.childMultichecks);
									}

								});
						
						final SafeHtmlBuilder builder = new SafeHtmlBuilder();
						builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
								+ "\">");
						builder.appendHtmlConstant("Drag " + currentObject.text
								+ " into next higher level to build a tree");
						builder.appendHtmlConstant("</div>");

						DragSource source = new DragSource(currentObject) {
							@Override
							protected void onDragStart(DndDragStartEvent event) {
								super.onDragStart(event);

								DragobjectContainer dragAccordion = new DragobjectContainer();
								dragAccordion.checkID = currentObject.checkId;
								dragAccordion.checkName = currentObject.text;

								event.setData(dragAccordion);
								event.getStatusProxy().update(
										builder.toSafeHtml());
							}

						};

						source.setGroup("multicheck");

						multicheckColumns.get(columnElement).addFirst(
								currentObject);

						// add new empty field for multicheck

					}

					// action that is triggered if check is dragged into an
					// already used target field (overwriting)
					if (currentObject.checkId != null) {

						currentObject.setText(dragAccordion.checkName);
						currentObject.setCheckID(dragAccordion.checkID);
						currentObject.setEmpty(false);
						currentObject.getElement().getStyle()
								.setBackgroundColor(ColorHelper.randomColor());
						multicheckColumns.get(columnElement).setEmpty(false);

						final SafeHtmlBuilder builder = new SafeHtmlBuilder();
						builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
								+ "\">");
						builder.appendHtmlConstant("Drag " + currentObject.text
								+ " into next level to build a tree");
						builder.appendHtmlConstant("</div>");

						DragSource source = new DragSource(currentObject) {
							@Override
							protected void onDragStart(DndDragStartEvent event) {
								super.onDragStart(event);

								DragobjectContainer dragAccordion = new DragobjectContainer();
								dragAccordion.checkID = currentObject.checkId;
								dragAccordion.checkName = currentObject.text;

								event.setData(dragAccordion);
								event.getStatusProxy().update(
										builder.toSafeHtml());
							}

						};
						source.setGroup("multicheck");

						multicheckColumns.get(columnElement).set(
								multicheckColumns.get(columnElement).size()
										- currentObject.level, currentObject);

					}

					if (currentObject.level == multicheckColumns.get(
							columnElement).size()
							&& multicheckColumns.get(columnElement).size() < 4) {
						SameLevelCheckpathObject addNextLevelField = new SameLevelCheckpathObject(
								null, "drag check here", true,
								currentObject.level + 1);
						multicheckColumns.get(columnElement).addFirst(
								addNextLevelField);

					}

					rebuildCheckpathDiagram();

				}

			};
			target.setGroup("multicheck");
			target.setOverStyle("drag-ok");

			// if (mit.hasNext() == false) {

			// create new column if it is the last column and last column ist
			// empty

			// }

		}

		return checkColumn;

	}

}