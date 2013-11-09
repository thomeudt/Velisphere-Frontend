package com.velisphere.tigerspice.client.checks;

import java.util.Iterator;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.shared.CheckData;

public class CheckEditorWidget extends Composite {

	private String endpointID;
	private CheckServiceAsync rpcService;

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
	

	Button btnAddProvisioned = new Button();
	btnAddProvisioned.setText("+ New Check");
	btnAddProvisioned.setType(ButtonType.SUCCESS);
	btnAddProvisioned.setSize(ButtonSize.SMALL);

	
	hpMain.add(container);
	
	con.add(hpMain);

	con.add(btnAddProvisioned);
	rpcService = GWT.create(CheckService.class);
	updateCheckList(endpointID, container);
		
	}
	
	private void updateCheckList(String endpointID, final VerticalLayoutContainer container){
		final AnimationLoading animationLoading = new AnimationLoading();
		showLoadAnimation(animationLoading);
		
		rpcService.getChecksForEndpointID(
		
				endpointID,
				new AsyncCallback<Vector<CheckData>>() {

					@Override
					public void onFailure(
							Throwable caught) {
						// TODO Auto-generated method
						// stub
						removeLoadAnimation(animationLoading);
					}

					@Override
					public void onSuccess(Vector<CheckData> result) {
						

						final Accordion accordion = new Accordion();

						Iterator<CheckData> it = result.iterator();
						while (it.hasNext()){
	
							CheckData currentItem = it.next();
							final AccordionGroup accordionGroup = new AccordionGroup();
							accordionGroup.setHeading(currentItem.getName());
							Row row = accordionRowBuilder(Images.INSTANCE.tag(), currentItem.getPropertyId(), "Checking on Property: ");	
							accordionGroup.add(row);
							
							Row row2 = accordionRowBuilder(Images.INSTANCE.tag(), currentItem.getOperator(), "Operator: ");	
							accordionGroup.add(row2);
							
							Row row3 = accordionRowBuilder(Images.INSTANCE.tag(), currentItem.getCheckValue(), "Trigger Value: ");	
							accordionGroup.add(row3);
							
							String stateText = new String();
							if (currentItem.getState()==1){
								stateText = "True";
							} else
							{
								stateText = "Not True";
							}
							Row row4 = accordionRowBuilder(Images.INSTANCE.tag(), stateText, "Current State: ");	
							accordionGroup.add(row4);
							
							
							
							accordion.add(accordionGroup);
							container.add(accordion);
							
							
							
						}
						
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
	
	
	private Row accordionRowBuilder(ImageResource imRes, String checkItem, String itemDescriptor){
		Column iconColumn = new Column(1);
		iconColumn.addStyleName("text-center");
		Image img = new Image();
		img.setResource(imRes);
		iconColumn.add(img);
		
		
		Column textColumn = new Column(2);
		DynamicAnchor propertyLine = new DynamicAnchor(itemDescriptor + " " + checkItem, true, null);
		
										
		textColumn.add(propertyLine);
		Row row = new Row();
		row.add(iconColumn);
		row.add(textColumn);
		return row;
	
	}
	
}
