package com.velisphere.tigerspice.client.logic.draggables;

import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Image;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.client.images.Images;
import com.velisphere.tigerspice.client.logic.connectors.ConnectorSensorLogicCheck;
import com.velisphere.tigerspice.client.logic.controllers.InCanvasLinkDropController;
import com.velisphere.tigerspice.shared.SerializableLogicLogicCheck;
import com.velisphere.tigerspice.shared.SerializableLogicPhysicalItem;

public class LogicCheck extends FocusPanel implements HasAllTouchHandlers {

	String content;
	LinkCreator dragPointWidget;
	int sourceCount;
	int xPos;
	int yPos;
	String uuid;
	boolean or;
	boolean and;
	LinkedList<ConnectorSensorLogicCheck> childConnectors;
	InCanvasLinkDropController linkDropController;
	

	
	

	public LogicCheck()
	{
		super();
		
		createCheckUUID();
		childConnectors = new LinkedList<ConnectorSensorLogicCheck>();
		sourceCount = 0;
		removeDefaultMouseDown();
		content = "Logic Check";
	    this.setStyleName("wellwhite");
        getElement().getStyle().setPadding(1, Unit.PX);
        getElement().getStyle().setBackgroundColor("#ffffff");
        this.getElement().getStyle().setCursor(Cursor.POINTER);
	}
	
	
	protected void createCheckUUID()
	{
		UuidServiceAsync uuidService = GWT
				.create(UuidService.class);
		
		uuidService.getUuid(new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				uuid = result;
			}
			
		});
		
	}
    
    @Override
    public HandlerRegistration addTouchStartHandler(TouchStartHandler handler){
            return addDomHandler(handler, TouchStartEvent.getType());
    }
    
    @Override
    public HandlerRegistration addTouchEndHandler(TouchEndHandler handler){
            return addDomHandler(handler, TouchEndEvent.getType());
    }
    
    @Override
    public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler){
            return addDomHandler(handler, TouchMoveEvent.getType());
    }
    
    @Override
    public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler){
            return addDomHandler(handler, TouchCancelEvent.getType());
    }
    
    private void removeDefaultMouseDown()
	{
    	
		this.addMouseDownHandler(new MouseDownHandler(){

			@Override
			public void onMouseDown(MouseDownEvent event) {
				event.preventDefault();
			}
			
		});
		
	}
    
    public Image getImage()
    {
    	Image tempImage = new Image();
    	tempImage.setUrl(Images.INSTANCE.and_icon().getSafeUri());
    	tempImage.setWidth("100%");
    	return tempImage;
    }

    public String getContentRepresentation() {
        return this.content;
    }
    
    public SerializableLogicLogicCheck getSerializableRepresentation()
    {
    	RootPanel.get().add(new HTML("Logic Check: getSerializable called."));
    	SerializableLogicLogicCheck serializable = new SerializableLogicLogicCheck();
    	RootPanel.get().add(new HTML("Logic Check: Init..."));
    	serializable.setAnd(this.and);
    	RootPanel.get().add(new HTML("Logic Check: And set."));
    	serializable.setContent(this.content);
    	RootPanel.get().add(new HTML("Logic Check: Content set."));
    	serializable.setId(this.uuid);
    	RootPanel.get().add(new HTML("Logic Check: UUID set."));
    	serializable.setOr(this.or);
    	RootPanel.get().add(new HTML("Logic Check: Or set."));
    	serializable.setSourceCount(this.sourceCount);
    	RootPanel.get().add(new HTML("Logic Check: Source Count set."));
    	serializable.setxPos(this.xPos);
    	RootPanel.get().add(new HTML("Logic Check: xPos set."));
    	serializable.setyPos(this.yPos);
    	RootPanel.get().add(new HTML("Logic Check: yPos set."));
    	return serializable;
    	
    }
    
    public void setDragPointWidget(LinkCreator dragPointWidget)
    {
    	this.dragPointWidget = dragPointWidget;
    }
    
    public LinkCreator getDragPointWidget()
    {
    	return this.dragPointWidget;
    }
    
    

    public int getSourceCount() {
		return sourceCount;
	}


	public void setSourceCount(int sourceCount) {
		this.sourceCount = sourceCount;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public int getxPos() {
		return xPos;
	}


	public void setxPos(int xPos) {
		this.xPos = xPos;
	}


	public int getyPos() {
		return yPos;
	}


	public void setyPos(int yPos) {
		this.yPos = yPos;
	}


	public String getId() {
		return uuid;
	}
	
	public void setId(String uuid) {
		this.uuid = uuid;
	}




	

	public boolean isOr() {
		return or;
	}


	public void setOr(boolean or) {
		this.or = or;
	}


	public boolean isAnd() {
		return and;
	}


	public void setAnd(boolean and) {
		this.and = and;
	}
	
	
	
	public LinkedList<ConnectorSensorLogicCheck> getChildConnectors() {
		return childConnectors;
	}


	public void addChildConnector(
			ConnectorSensorLogicCheck childConnector) {
		this.childConnectors.add(childConnector);
	}
	
	public InCanvasLinkDropController getLinkDropController() {
		return linkDropController;
	}


	public void setLinkDropController(InCanvasLinkDropController linkDropController) {
		this.linkDropController = linkDropController;
	}





	
}
