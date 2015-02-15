package com.velisphere.tigerspice.client.logic;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.logic.layoutWidgets.Explorer;
import com.velisphere.tigerspice.client.rules.CheckpathList;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class LogicDesigner extends Composite {


	
	@UiField
	Paragraph txtSaveStatus;
	@UiField
	Breadcrumbs brdMain;
	@UiField
	TextBox txtCheckpathTitle;
	@UiField
	Button btnSaveCheckpath;
	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	String userID;
	LogicCanvas canvas;

	@UiField
	Column colCanvas;
	@UiField
	Column colExplorer;
	


	
	
	private static LogicDesignerUiBinder uiBinder = GWT
			.create(LogicDesignerUiBinder.class);

	interface LogicDesignerUiBinder extends UiBinder<Widget, LogicDesigner> {
	}

	public LogicDesigner(String userID) {

		this.userID = userID;
		initWidget(uiBinder.createAndBindUi(this));
		
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText("Create New Logic Design");
		brdMain.add(bread2);
		btnSaveCheckpath.setWidth("160px");
		//wgtCostIndicator.setCheckpathEditor(wgtCheckpathEditor);
		
		
		bread0.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {

				RootPanel.get("main").clear();
				LoginSuccess loginSuccess = new LoginSuccess();
				RootPanel.get("main").add(loginSuccess);

			}
		});
		
		bread1.addClickHandler(
				new ClickHandler(){
					@Override
					public void onClick(ClickEvent event){
			
						RootPanel mainPanel = RootPanel.get("main");
						mainPanel.clear();
						CheckpathList checkPathList = new CheckpathList("dummy"); 		
						mainPanel.add(checkPathList);
						
					}
				});

	
		
		canvas = new LogicCanvas();
		colCanvas.add(canvas);
		
		Explorer explorer = new Explorer(this.userID, canvas);
		colExplorer.add(explorer);
		
		
		
		

	}
	
	@UiHandler("btnSaveCheckpath")
	void saveCheckpath(ClickEvent event) 
	{
		canvas.getJson();
		
	}
	
	
	
		
	

}
