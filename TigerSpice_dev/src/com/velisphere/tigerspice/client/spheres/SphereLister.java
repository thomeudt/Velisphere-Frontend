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
/**
 * Sencha GXT 3.0.1 - Sencha for GWT
 * Copyright(c) 2007-2012, Sencha, Inc.
 * licensing@sencha.com
 *
 * http://www.sencha.com/products/gxt/license/
 */
package com.velisphere.tigerspice.client.spheres;
 
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.info.Info;
import com.velisphere.tigerspice.client.admin.propertyclass.EditPropertyClassInputWidget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DynamicAnchor;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.SphereData;
 
public class SphereLister extends Composite {

	private NavLink bread1;
	private NavLink bread0;
	private SphereServiceAsync rpcService;
	@UiField 
	Breadcrumbs brdMain;
	@UiField
	ListBox lstPrivateSpheres;
	@UiField
	Button btnOpenSphere;
	
  interface MyUiBinder extends UiBinder<Widget, SphereLister> {
  }
 
  private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
 
  public SphereLister() {
      
	  initWidget(uiBinder.createAndBindUi(this));
	  
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Endpoint Manager");
		brdMain.add(bread1);
		
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});
	
		
		final AnimationLoading animation = new AnimationLoading();
		animation.showLoadAnimation("Loading spheres");
		System.out.println("Loading");
		
		// TODO needs to be changed to show only spheres personal to user
		rpcService = GWT.create(SphereService.class);
		rpcService.getAllSpheresForUserID(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<SphereData>>() {
			
			
			public void onFailure(Throwable caught) {
				Window.alert("Error" + caught.getMessage());
				System.out.println("NoSUCC");
			}

			@Override
			public void onSuccess(LinkedList<SphereData> result) {
				
				System.out.println("SUCC");
								
				Iterator<SphereData> it = result.iterator();
					
				
				while (it.hasNext()){

					final SphereData currentItem = it.next();
					
					String suffix = new String();
					
					if(currentItem.sphereIsPublic==1){
						suffix = " * PUBLIC * ";
					}
					lstPrivateSpheres.addItem(currentItem.sphereName.concat(suffix), currentItem.sphereId);
					
						
				}
				
				if (lstPrivateSpheres.getItemCount() != 0){
					btnOpenSphere.setEnabled(true);
				}
				
				lstPrivateSpheres.setVisibleItemCount(7);
				
				
			

				animation.removeFromParent();
				
			}

			
		});

	  
  }

  
  @UiHandler("btnOpenSphere")
	void openSphere(ClickEvent event) {
	
		/**
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		
		CheckpathEditView checkpathView = new CheckpathEditView(lstCheckpath.getValue()); 		
		mainPanel.add(checkpathView);
		**/
		
		AppController.openSphere(lstPrivateSpheres.getValue());
		
	}
  

  @UiHandler("btnCreateNewSphere")
 	void createNewSphere(ClickEvent event) {
 	
	  final SphereAdder addSphere = new
			  SphereAdder();
	  
	  addSphere.setAutoHideEnabled(true);
	  
	  
	  
	  addSphere.show(); 
	  addSphere.center();
	 
	 		
 	}

  
  
   
}
