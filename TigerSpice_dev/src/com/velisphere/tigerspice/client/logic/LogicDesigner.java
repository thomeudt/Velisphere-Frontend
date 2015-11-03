package com.velisphere.tigerspice.client.logic;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.PageHeader;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.LogicNameChangedEvent;
import com.velisphere.tigerspice.client.event.LogicNameChangedEventHandler;
import com.velisphere.tigerspice.client.favorites.FavoriteService;
import com.velisphere.tigerspice.client.favorites.FavoriteServiceAsync;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.client.logic.layoutWidgets.LogicCanvas;
import com.velisphere.tigerspice.client.logic.layoutWidgets.Explorer;
import com.velisphere.tigerspice.client.users.LoginSuccess;
import com.velisphere.tigerspice.shared.FavoriteData;

public class LogicDesigner extends Composite {


	
	@UiField
	Breadcrumbs brdMain;
	

	NavLink bread0;
	NavLink bread1;
	NavLink bread2;
	String userID;
	LogicCanvas canvas;
	String favoriteID;
	String checkpathID;

	@UiField
	Column colCanvas;
	@UiField
	Column colExplorer;
	@UiField
	PageHeader pghRuleName;

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

		this.checkpathID = checkpathID;
		EventUtils.RESETTABLE_EVENT_BUS.removeHandlers();

		
		addNameChangeHandler();
		this.userID = userID;
		initWidget(uiBinder.createAndBindUi(this));

		buildUI();
		addFavoritesFunctions();
		
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

	private void addFavoritesFunctions()
	{
		
		final VerticalPanel vp = new VerticalPanel();
					
		
		final FavoriteServiceAsync favoriteService = GWT
				.create(FavoriteService.class);
		
		favoriteService.getFavoriteForTargetID(checkpathID, new AsyncCallback<FavoriteData>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(FavoriteData result) {
				// TODO Auto-generated method stub
				
				if (result.getId() == null)
				{
					vp.add(addFavoriteWidget());			
				}
				else
				{
					favoriteID = result.getId();
					vp.add(removeFavoriteWidget());
					
				}
					
				
			}
			
		});
		
		
		
		pghRuleName.add(vp);
	}
	
	
	private void addNameChangeHandler()
	{
		logicNameChangedEventHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				LogicNameChangedEvent.TYPE, new LogicNameChangedEventHandler() {

					@Override
					public void onLogicNameChanged(
							LogicNameChangedEvent logicNameChangedEvent) {
			
						brdMain.remove(bread2);						
						bread2.setText(logicNameChangedEvent.getName());						
						brdMain.add(bread2);
						
										
					}
				});

	}
	
	private HorizontalPanel addFavoriteWidget()
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.STAR_EMPTY);
		hp.add(icnEditEndpointName);
		final Anchor ancEditEndpointName = new Anchor();
		ancEditEndpointName.setText(" Add Favorite");
		ancEditEndpointName.setHref("#");
		
		ancEditEndpointName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
					FavoriteData favoriteData = new FavoriteData();
					favoriteData.setName("Logic " + bread2.getText());
					favoriteData.setTargetId(checkpathID);
					favoriteData.setUserId(SessionHelper.getCurrentUserID());
					favoriteData.setType(VeliConstants.FAVORITE_LOGIC);
					
										
					final FavoriteServiceAsync favoriteService = GWT
							.create(FavoriteService.class);
					
					favoriteService.addFavorite(favoriteData, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							
							AppController.openLogicDesign(checkpathID);
							
						}
						
					}
							
							);
					
				
				
				}
				});
		hp.add(ancEditEndpointName);
		return hp;

	}
	
	
	private HorizontalPanel removeFavoriteWidget()
	{
		HorizontalPanel hp = new HorizontalPanel();
		
		Icon icnEditEndpointName = new Icon();
		icnEditEndpointName.setType(IconType.STAR);
		hp.add(icnEditEndpointName);
		final Anchor ancEditEndpointName = new Anchor();
		ancEditEndpointName.setText(" Remove Favorite");
		ancEditEndpointName.setHref("#");
		
		ancEditEndpointName.addClickHandler(
				new ClickHandler(){ 
				public void onClick(ClickEvent event)  {
										
										
					final FavoriteServiceAsync favoriteService = GWT
							.create(FavoriteService.class);
					
					favoriteService.deleteFavoriteForFavoriteID(favoriteID, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							
							AppController.openLogicDesign(checkpathID);
							
						}
						
					}
							
							);
					
				
				
				}
				});
		hp.add(ancEditEndpointName);
		return hp;

	}

	

}
