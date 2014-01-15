package com.velisphere.tigerspice.client.properties;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Accordion;
import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.velisphere.tigerspice.client.checks.CheckService;
import com.velisphere.tigerspice.client.checks.CheckServiceAsync;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DragobjectContainer;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.helper.EventUtils;
import com.velisphere.tigerspice.client.helper.SessionHelper;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEvent;
import com.velisphere.tigerspice.client.helper.SessionVerifiedEventHandler;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.EndpointData;

public class ActorPropertiesByEndpointWidget extends Composite {

	private PropertyServiceAsync rpcServiceProperty;
	private EndpointServiceAsync rpcServiceEndpoint;
	private HandlerRegistration sessionHandler;
	private String userID;
	private Accordion accordion;
	private AnimationLoading loadAnimation;
	
	public ActorPropertiesByEndpointWidget() {
	
		
		
	super();
		
	rpcServiceEndpoint = GWT.create(EndpointService.class);
	rpcServiceProperty = GWT.create(PropertyService.class);
	loadAnimation = new AnimationLoading();
	
	
	FlowLayoutContainer con = new FlowLayoutContainer();
	initWidget(con);

	VerticalLayoutContainer container = new VerticalLayoutContainer();

	container.setBorders(false);
	container.setScrollMode(ScrollSupport.ScrollMode.AUTOY);
	// container.setHeight((int)((RootPanel.get().getOffsetHeight())/2.5));
	container.setWidth(180);
	con.add(container);
	accordion = new Accordion();
	container.add(accordion);

	
	SessionHelper.validateCurrentSession();

	
	sessionHandler = EventUtils.EVENT_BUS.addHandler(SessionVerifiedEvent.TYPE, new SessionVerifiedEventHandler()     {
	    	
	@Override
    public void onSessionVerified(SessionVerifiedEvent sessionVerifiedEvent) {
		
		sessionHandler.removeHandler();
		userID = SessionHelper.getCurrentUserID();

	loadAnimation.showLoadAnimation("Loading endpoints");
	rpcServiceEndpoint.getEndpointsForUser(userID, new AsyncCallback<Vector<EndpointData>>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(Vector<EndpointData> result) {
			
			loadAnimation.removeLoadAnimation();
			Iterator<EndpointData> it = result.iterator();
			while(it.hasNext()){
				AccordionGroup endpoint = new AccordionGroup();
				
				EndpointData current = it.next();
				String shortName;
				
				if (current.getName().length() < 25){
					shortName = current.getName();	
				} else
				{
					shortName = current.getName().substring(0, 20)+"(...)";
				}
				endpoint.setHeading(shortName);
				endpoint.setTitle(current.getName());
				addActPropertiesToEndpoint(endpoint, current.getId());
				
				accordion.add(endpoint);
			}
			
		}
		
	});	
}
	});
	}
	
	private void addActPropertiesToEndpoint(final AccordionGroup endpoint, String endpointID){

		
		loadAnimation.showLoadAnimation("Loading actors");
		rpcServiceProperty.getActorPropertiesForEndpointID(endpointID, new AsyncCallback<LinkedList<PropertyData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<PropertyData> result) {
				// TODO Auto-generated method stub
				loadAnimation.removeLoadAnimation();
				Iterator<PropertyData> it = result.iterator();
				if (it.hasNext() == false){
					Row row = accordionRowBuilder("this thing can't do anything");
					endpoint.add(row);
				}
				while(it.hasNext()){
					
					PropertyData current = it.next();
					String shortName;
					
					if (current.getName().length() < 25){
						shortName = current.getName();	
					} else
					{
						shortName = current.getName().substring(0, 20)+"(...)";
					}
					Row row = accordionRowBuilder(shortName);
					setDragSource(row, current.getId(), current.getName());
					endpoint.add(row);

				}
								
			}
			
		});	

	}
	
	private Row accordionRowBuilder(String checkItem) {
		Column textColumn = new Column(2);
		DynamicAnchor propertyLine = new DynamicAnchor(checkItem, true, null);
		textColumn.add(propertyLine);
		Row row = new Row();
		row.add(textColumn);
		return row;
	}
	
	private void setDragSource(Row accordionRow, final String checkID, final String checkName){
		final SafeHtmlBuilder builder = new SafeHtmlBuilder();
		builder.appendHtmlConstant("<div style=\"border:1px solid #ddd;cursor:default\" class=\""
				+ "\">");
		builder.appendHtmlConstant("Drag "
				+ checkName
				+ " into Checkpath to create an action");
		builder.appendHtmlConstant("</div>");
		
		DragSource source = new DragSource(
				accordionRow) {
			@Override
			protected void onDragStart(
					DndDragStartEvent event) {
				super.onDragStart(event);
				// by
				// default
				// drag
				// is
				// allowed
				
				DragobjectContainer dragAccordion = new DragobjectContainer();
				dragAccordion.checkID = checkID;
				dragAccordion.checkName = checkName;
				
				event.setData(dragAccordion);
				event.getStatusProxy()
						.update(builder
								.toSafeHtml());
			}
			

		};
		source.setGroup("actors");

	}

	
}