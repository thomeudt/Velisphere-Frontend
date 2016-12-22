package com.velisphere.tigerspice.client.dash;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEvent;
import com.velisphere.tigerspice.client.event.DraggedToCanvasEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;
import com.velisphere.tigerspice.client.event.RemoveGaugeFromDashEvent;
import com.velisphere.tigerspice.client.event.RemoveGaugeFromDashEventHandler;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.UuidService;
import com.velisphere.tigerspice.client.helper.UuidServiceAsync;
import com.velisphere.tigerspice.shared.DashData;
import com.velisphere.tigerspice.shared.GaugeData;

public class FlexBoard extends Composite{
	
	VerticalPanel verticalPanel;
	Button btnSave;
	Button btnDelete;
	LinkedList<Object> gaugeList;
	FlexColumn currentAddBoxColumn;
	static HTML SPACER = new HTML("&nbsp;<br>");
	Row row;
	String dashID;
	String dashName = "";
	HandlerRegistration removeGaugeHandler;
	
	// Default constructor for a new FlexBoard
	
	public FlexBoard()
	{
		
		gaugeList = new LinkedList<Object>();
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		displayHeader();
		displayInitialField();
		setRemoveGaugeEventHandler();
		
	}
	
	// Constructor to load an existing FlexBoard
	
	public FlexBoard(String dashID)
	{
		this.dashID = dashID;
		gaugeList = new LinkedList<Object>();
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		getDashName();
		displayHeader();
		loadExistingFields();
		setRemoveGaugeEventHandler();
		
	}
	
	
	private void displayHeader()
	{
		row = new Row();
		verticalPanel.add(row);

		Column titleColumn = new Column(2);
		titleColumn.add(new HTML("<b>Dashboard Controls:</b>"));
		row.add(titleColumn);

		
		
		Column saveDashboardColumn = new Column(2);
		btnSave = new Button("Save");
		btnSave.setSize(ButtonSize.SMALL);
		btnSave.setType(ButtonType.SUCCESS);
		btnSave.setBaseIcon(IconType.SAVE);
		btnSave.setWidth("80%");
		saveDashboardColumn.add(btnSave);
		row.add(saveDashboardColumn);
	
		
		btnSave.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				saveDashboardDialog().center();
			}
			
		});

		Column deleteDashboardColumn = new Column(2);
		btnDelete = new Button("Delete");
		btnDelete.setSize(ButtonSize.SMALL);
		btnDelete.setType(ButtonType.DANGER);
		btnDelete.setBaseIcon(IconType.TRASH);
		btnDelete.setWidth("80%");
		deleteDashboardColumn.add(btnDelete);
		row.add(deleteDashboardColumn);
	
		row.add(new HTML("<br>&nbsp;<br>"));
		
		btnDelete.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				deleteDashboard();
				
			}
			
		});

		
	}
	
	
	private void displayInitialField()
	{
		row = new Row();
		verticalPanel.add(row);
		currentAddBoxColumn = addAddBoxColumn();
		
		row.add(currentAddBoxColumn);
		
	}

	
	private void getDashName()
	{
		final AnimationLoading animationLoading = new AnimationLoading("Loading gauges...");
		animationLoading.showLoadAnimation();
		
		GaugeServiceAsync gaugeService = GWT
				.create(GaugeService.class);

		gaugeService.getDashboardName(dashID, new AsyncCallback<String>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						animationLoading.removeLoadAnimation();
						dashName = result;
						
					}
			
				});
		
	}
	
	private void loadExistingFields()
	{
		

		final AnimationLoading animationLoading = new AnimationLoading("Loading gauges...");
		animationLoading.showLoadAnimation();
		
		GaugeServiceAsync gaugeService = GWT
				.create(GaugeService.class);

		gaugeService.getGaugesForDashID(dashID, new AsyncCallback<LinkedList<GaugeData>>()
				{

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(LinkedList<GaugeData> result) {
						// TODO Auto-generated method stub
						
						Iterator<GaugeData> it = result.iterator();
						while (it.hasNext())
						{

							GaugeData gaugeData = it.next();
						
							if(gaugeData.isSwitch() == true)
							{
		
								SwitchBox switchBox = new SwitchBox(gaugeData.getEndpointID(), gaugeData.getPropertyID());
		
								
								row.add(addSwitchColumnWithData(switchBox));		
		
							}
							else
							{
								GaugeBox gaugeBox = new GaugeBox(gaugeData.getEndpointID(), gaugeData.getPropertyID(), 
										gaugeData.getGaugeType(), gaugeData.getGreenRange(), gaugeData.getYellowRange(),
										gaugeData.getRedRange(), gaugeData.getMinMax());
		
								
								row.add(addGaugeColumnWithData(gaugeBox));		
							}
							
																
						}
						currentAddBoxColumn = addAddBoxColumn();
						row.add(currentAddBoxColumn);

						animationLoading.removeLoadAnimation();
					
					}

								
				});

		
	}
	
	
	private DialogBox saveDashboardDialog()
	{
			final DialogBox saveDialog = new DialogBox();
			saveDialog.setText("Name of this Dashboard:");
			saveDialog.setStyleName("wellappleblue");
			VerticalPanel panel = new VerticalPanel();
			final TextBox txtName = new TextBox();
			txtName.setText(dashName);
			panel.add(txtName);
		
			Button btnSave = new Button("Save");
			panel.add(btnSave); 
			saveDialog.add(panel);
			saveDialog.setAutoHideEnabled(true);
			
			btnSave.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					
					final AnimationLoading animationLoading = new AnimationLoading("Saving gauges...");
					animationLoading.showLoadAnimation();

					
					final LinkedList<GaugeData> gaugeDataList = new LinkedList<GaugeData>();
					
					Iterator<Object> it = gaugeList.iterator();
					while (it.hasNext())
					{
						Object object = it.next();
						if(object.getClass()==GaugeBox.class)
						{
							GaugeBox gaugeBox = (GaugeBox) object;
							GaugeData gaugeData = new GaugeData();
							gaugeData.setEndpointID(gaugeBox.getEndpointID());
							gaugeData.setPropertyID(gaugeBox.getPropertyID());
							gaugeData.setGaugeType(gaugeBox.getGaugeType());
							gaugeData.setMinMax(gaugeBox.getMinMax());
							gaugeData.setGreenRange(gaugeBox.getGreenRange());
							gaugeData.setYellowRange(gaugeBox.getYellowRange());
							gaugeData.setRedRange(gaugeBox.getRedRange());
							gaugeDataList.add(gaugeData);	
						}
						
						if(object.getClass()==SwitchBox.class)
						{
							SwitchBox switchBox = (SwitchBox) object;
							GaugeData switchData = new GaugeData();
							switchData.setSwitch(true);
							switchData.setEndpointID(switchBox.getEndpointID());
							switchData.setPropertyID(switchBox.getPropertyID());
							gaugeDataList.add(switchData);	
							
						}
						
					}
					
					if(dashID==null)
					{
						UuidServiceAsync uuidService = GWT
								.create(UuidService.class);
						uuidService.getUuid(new AsyncCallback<String>()
								{

									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
										
									}

									@Override
									public void onSuccess(String result) {
										dashID = result;
										saveDashboard(txtName.getText(), gaugeDataList);
									}
							
								});
							
					}

					else
					{
						saveDashboard(txtName.getText(), gaugeDataList);
							
					}
					
					saveDialog.removeFromParent();
					animationLoading.removeLoadAnimation();
				}
				
			});
			return saveDialog;

		}


	private void saveDashboard(String dashName, LinkedList<GaugeData> gaugeDataList)
	{
		
		final AnimationLoading animationLoading = new AnimationLoading("Saving dashboard...");
		animationLoading.showLoadAnimation();
		
		
		GaugeServiceAsync gaugeService = GWT
				.create(GaugeService.class);
		
		gaugeService.saveDashboard(SessionHelper.getCurrentUserID(), dashName, dashID,  gaugeDataList, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				animationLoading.removeLoadAnimation();
				AppController.openDashBoardWithSelected(result);
			}
			
		});

	}

	private void deleteDashboard()
	{
		
		final AnimationLoading animationLoading = new AnimationLoading("Deleting dashboard...");
		animationLoading.showLoadAnimation();
		
		GaugeServiceAsync gaugeService = GWT
				.create(GaugeService.class);
		gaugeService.deleteDashboard(dashID, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				// TODO Auto-generated method stub
				
				animationLoading.removeLoadAnimation();
				AppController.openDashBoard();
				
			}
			
		});
			
		
	}
	
	
	private FlexColumn addAddBoxColumn()
	{
		FlexColumn column = new FlexColumn(2);
		column.add(addBox());
		return column;
	}

	private VerticalPanel addBox()
	{
		VerticalPanel panel = new VerticalPanel();
		panel.setHeight("180px");
		SimplePanel upperSpacer = new SimplePanel();
		upperSpacer.setHeight("20px");
		panel.add(upperSpacer);
		Button gaugeButton = new Button("Add Gauge...");
		gaugeButton.setSize(ButtonSize.DEFAULT);
		gaugeButton.setType(ButtonType.PRIMARY);
		gaugeButton.setBaseIcon(IconType.DASHBOARD);
		gaugeButton.setWidth("120px");
		panel.add(gaugeButton);
		
		gaugeButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				row.remove(currentAddBoxColumn);
				
				row.add(addGaugeColumn());		
				
				currentAddBoxColumn = addAddBoxColumn();
				row.add(currentAddBoxColumn);

			}
			
		});

		panel.add(new HTML("or"));
		
		
		Button switchButton = new Button("Add Switch...");
		switchButton.setBaseIcon(IconType.POWER_OFF);
		switchButton.setSize(ButtonSize.DEFAULT);
		switchButton.setType(ButtonType.PRIMARY);
		switchButton.setWidth("120px");
		panel.add(switchButton);
		
		switchButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				row.remove(currentAddBoxColumn);
				
				row.add(addSwitchColumn());		
				
				currentAddBoxColumn = addAddBoxColumn();
				row.add(currentAddBoxColumn);

			}
			
		});

		
		return panel;
		
	}

	private Column addGaugeColumn()
	{
		
		FlexColumn column = new FlexColumn(2);
		GaugeBox gaugeBox = new GaugeBox();
		column.add(gaugeBox);
		gaugeList.add(gaugeBox);
		return column;
	}

	
	private Column addSwitchColumn()
	{
		
		FlexColumn column = new FlexColumn(2);
		SwitchBox switchBox = new SwitchBox();
		column.add(switchBox);
		gaugeList.add(switchBox);
		return column;
	}

	
	private Column addGaugeColumnWithData(GaugeBox gaugeBox)
	{
		
		FlexColumn column = new FlexColumn(2);
		column.add(gaugeBox);
		gaugeList.add(gaugeBox);
		return column;
	}
	
	private Column addSwitchColumnWithData(SwitchBox switchBox)
	{
		
		FlexColumn column = new FlexColumn(2);
		column.add(switchBox);
		gaugeList.add(switchBox);
		return column;
	}
	
	
	private void setRemoveGaugeEventHandler()
	{

		removeGaugeHandler = EventUtils.RESETTABLE_EVENT_BUS.addHandler(
				RemoveGaugeFromDashEvent.TYPE, new RemoveGaugeFromDashEventHandler() {

					@Override
					public void onGaugeBoxRemoved(
							RemoveGaugeFromDashEvent removeGaugeFromDashEvent) {
						
						gaugeList.remove(removeGaugeFromDashEvent.getGaugeBox());
						
					}
				});

	}
	
	
	
}



