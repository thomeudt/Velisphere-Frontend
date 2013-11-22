package com.velisphere.tigerspice.client.rules;

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
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.helper.DragobjectAccordion;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.client.properties.PropertyService;
import com.velisphere.tigerspice.client.properties.PropertyServiceAsync;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.client.rules.RuleView;
import com.velisphere.tigerspice.client.spheres.SphereView;
import com.velisphere.tigerspice.shared.CheckData;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class CheckpathEditorWidget extends Composite {

	private String endpointID;
	private CheckServiceAsync rpcService;
	Accordion accordion = new Accordion();

	public CheckpathEditorWidget() {

		// widget constructor requires a parameter, thus we need to invoke the
		// no-args constructor of the parent class and then set the value for
		// sphereID
		// also note that we need to use the @UiFactory notation to instantiate
		// a widget that requires arguments like this, see how it is done in
		// SphereOverview class
		super();
		//this.endpointID = endpointID;

		FlowLayoutContainer con = new FlowLayoutContainer();
		initWidget(con);

		
		final VerticalLayoutContainer container = new VerticalLayoutContainer();
		container.setBorders(true);
		container.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
		// container.setHeight((int)((RootPanel.get().getOffsetHeight())/2.5));
		container.setWidth((int) ((RootPanel.get().getOffsetWidth()) / 4));

		VerticalLayoutContainer firstCheckColumn = new VerticalLayoutContainer();
		firstCheckColumn.setBorders(true);
		firstCheckColumn.setWidth((int) (400));
		firstCheckColumn.setHeight((int) (400));
		
		HorizontalLayoutContainer firstCheckRow = new HorizontalLayoutContainer();
		firstCheckRow.setBorders(true);
		
		VerticalLayoutContainer firstCheckField = new VerticalLayoutContainer();
		firstCheckField.setBorders(true);
		firstCheckField.setWidth((int) (100));
		
		Paragraph pghFirstCheck = new Paragraph();
		pghFirstCheck.setText("1st Check here");
		firstCheckField.add(pghFirstCheck);
		
		VerticalLayoutContainer addCheckField = new VerticalLayoutContainer();
		addCheckField.setBorders(true);
		addCheckField.setWidth((int) (100));
		
		Paragraph pghAddSameLevel = new Paragraph();
		pghAddSameLevel.setText("add same level");
		addCheckField.add(pghAddSameLevel);
		
		firstCheckRow.add(firstCheckField);
		firstCheckRow.add(new Icon(IconType.CHEVRON_RIGHT));
		firstCheckRow.add(addCheckField);
		
		
		VerticalLayoutContainer addNextLevelField = new VerticalLayoutContainer();
		addCheckField.setBorders(true);
		addCheckField.setWidth((int) (100));

		Paragraph pghAddNextLevel = new Paragraph();
		pghAddNextLevel.setText("add next level");
		addNextLevelField.add(pghAddNextLevel);
				
		firstCheckColumn.add(addNextLevelField);
		firstCheckColumn.add(new Icon(IconType.CHEVRON_UP));
		firstCheckColumn.add(firstCheckRow);
		
		
				
		container.add(firstCheckColumn);
		
		con.add(container);
		
		rpcService = GWT.create(CheckService.class);

		DropTarget target = new DropTarget(accordion) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);

				// do the drag and drop visual action

				

			}

		};
		target.setGroup("check");
		target.setOverStyle("drag-ok");

	}


}
