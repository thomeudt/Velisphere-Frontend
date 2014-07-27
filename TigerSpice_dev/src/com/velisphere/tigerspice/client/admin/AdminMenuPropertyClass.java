package com.velisphere.tigerspice.client.admin;

import com.github.gwtbootstrap.client.ui.Divider;
import com.github.gwtbootstrap.client.ui.NavHeader;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Well;
import com.github.gwtbootstrap.client.ui.WellNavList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;

public class AdminMenuPropertyClass extends Composite {

	NavLink edit = new NavLink("Edit");
	NavLink add = new NavLink("Add");
	NavLink retire = new NavLink("Retire");
	
	public AdminMenuPropertyClass()
	{
		edit.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				AppController.openPropertyClassManager("");
			}
		});
		
		add.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				AppController.openPropertyClassCreator();
			}
		});
		
		retire.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event){
				AppController.openEPCDeleter();
			}
		});
		
		
		
		WellNavList well = new WellNavList();				
		well.add(new NavHeader("Endpoint Class Tasks"));
		well.add(new Divider());
		well.add(edit);
		well.add(add);
		well.add(retire);
		initWidget(well);
			
	}
	
	public void setEditActive(){
		edit.setActive(true);
		add.setActive(false);
		retire.setActive(false);
	}
	
	public void setAddActive(){
		edit.setActive(false);
		add.setActive(true);
		retire.setActive(false);
	}
	
	public void setRetireActive(){
		edit.setActive(false);
		add.setActive(false);
		retire.setActive(true);
	}	
	
}
