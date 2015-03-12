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
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LogicNameChangedEvent;
import com.velisphere.tigerspice.client.event.LogicNameChangedEventHandler;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.logic.layoutWidgets.Explorer;
import com.velisphere.tigerspice.client.users.LoginSuccess;

public class LogicDesigner extends Composite {


	
	@UiField
	Breadcrumbs brdMain;
	

	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	String userID;
	LogicCanvas canvas;

	@UiField
	Column colCanvas;
	@UiField
	Column colExplorer;

	HandlerRegistration logicNameChangedEventHandler;


	
	
	private static LogicDesignerUiBinder uiBinder = GWT
			.create(LogicDesignerUiBinder.class);

	interface LogicDesignerUiBinder extends UiBinder<Widget, LogicDesigner> {
	}

	public LogicDesigner(String userID) {

		EventUtils.RESETTABLE_EVENT_BUS.removeHandlers();

		addNameChangeHandler();
		this.userID = userID;
		initWidget(uiBinder.createAndBindUi(this));
		buildUI();
		

	}
	

	
	public LogicDesigner(String userID, String checkpathID) {

		EventUtils.RESETTABLE_EVENT_BUS.removeHandlers();

		
		addNameChangeHandler();
		this.userID = userID;
		initWidget(uiBinder.createAndBindUi(this));

		buildUI();
		
		canvas.openFromDatabase(checkpathID);
				
	}
	
	public LogicDesigner(String userID, String checkpathID, boolean deleteLogic) {

		EventUtils.RESETTABLE_EVENT_BUS.removeHandlers();

		
		addNameChangeHandler();
		this.userID = userID;
		initWidget(uiBinder.createAndBindUi(this));

		buildUI();
		
		canvas.openFromDatabase(checkpathID);
		
		if (deleteLogic == true)
		{
			canvas.deleteFromDatabase();	
		}
		
				
	}
	
	
	private void buildUI()
	{
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);
		bread2 = new NavLink();
		bread2.setText("Create New Logic Design");
		brdMain.add(bread2);
	
		
		
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
						LogicDesignList checkPathList = new LogicDesignList(SessionHelper.getCurrentUserID()); 		
						mainPanel.add(checkPathList);
						
					}
				});

	
		
		canvas = new LogicCanvas();
		colCanvas.add(canvas);
		
		Explorer explorer = new Explorer(this.userID, canvas);
		colExplorer.add(explorer);

	}
	
	private void addNameChangeHandler()
	{
		logicNameChangedEventHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				LogicNameChangedEvent.TYPE, new LogicNameChangedEventHandler() {

					@Override
					public void onLogicNameChanged(
							LogicNameChangedEvent logicNameChangedEvent) {
						RootPanel.get().add(new HTML("Name change completed to " + logicNameChangedEvent.getName()));

						brdMain.remove(bread2);						
						bread2.setText(logicNameChangedEvent.getName());						
						brdMain.add(bread2);
						
										
					}
				});

	}
	
	
	

}
