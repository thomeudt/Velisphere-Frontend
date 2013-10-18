package com.velisphere.tigerspice.client.helper;

import com.github.gwtbootstrap.client.ui.Container;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.endpointclasses.EPCList;
import com.velisphere.tigerspice.client.endpoints.EndpointList;
import com.velisphere.tigerspice.client.properties.PropertyList;
import com.velisphere.tigerspice.client.users.UserList;

public class Banderole extends Composite {

interface MyBinder extends UiBinder<Widget, Banderole>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	@UiField ListBox lbxSelectTable;
	
	public Banderole() {
		lbxSelectTable = new ListBox();
		initWidget(uiBinder.createAndBindUi(this));
		
		lbxSelectTable.addItem("Users", "1");
		lbxSelectTable.addItem("Endpoints", "2");
		lbxSelectTable.addItem("Endpoint Classes", "3");
		lbxSelectTable.addItem("Endpoint Properties", "4");
	}
	
	public void setTitle(String title)
	{
		
	}
	
	@UiHandler("lbxSelectTable")
	void changeView (ChangeEvent event) {
		// Window.alert("Logging In");
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.clear();
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
	
		System.out.println(lbxSelectTable.getValue());
		
		if(lbxSelectTable.getValue().equals("1")){
			UserList userClassList = new UserList();
			rootPanel.add(userClassList);	
		}
		
		if(lbxSelectTable.getValue().equals("2")){
			EndpointList endpointList = new EndpointList();
			rootPanel.add(endpointList);	
		}
		
		if(lbxSelectTable.getValue().equals("3")){
			EPCList epcList = new EPCList();
			rootPanel.add(epcList);	
		}
		
		if(lbxSelectTable.getValue().equals("4")){
			PropertyList propertyList = new PropertyList();
			rootPanel.add(propertyList);	
		}
		
		
		
		// loginDialogBox.setVisible(false);
		
		
	}
}