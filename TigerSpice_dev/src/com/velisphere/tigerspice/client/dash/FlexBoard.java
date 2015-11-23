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
import com.velisphere.tigerspice.shared.DashData;
import com.velisphere.tigerspice.shared.GaugeData;

public class FlexBoard extends Composite{
	
	VerticalPanel verticalPanel;
	Button btnSave;
	LinkedList<Object> gaugeList;
	FlexColumn currentAddBoxColumn;
	static HTML SPACER = new HTML("&nbsp;<br>");
	Row row;
	String dashID;
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
		//rowMap = new HashMap<Integer, Row>();
		displayHeader();
		loadExistingFields();
		setRemoveGaugeEventHandler();
		
	}
	
	
	private void displayHeader()
	{
		row = new Row();
		verticalPanel.add(row);
		Column saveDashboardColumn = new Column(2);
		btnSave = new Button("Save");
		btnSave.setSize(ButtonSize.SMALL);
		btnSave.setType(ButtonType.PRIMARY);
		saveDashboardColumn.add(btnSave);
		row.add(saveDashboardColumn);
	
		row.add(new HTML("<br>&nbsp;<br>"));
		
		btnSave.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				saveDashboardDialog().center();
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

	/*
	private Row addRow()
	{
		Row row = new Row();
		rowCount ++;
		rowMap.put(rowCount, row);
		verticalPanel.add(row);
		return row;
	}
	*/

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
							GaugeBox gaugeBox = new GaugeBox(gaugeData.getEndpointID(), gaugeData.getPropertyID(), 
									gaugeData.getGaugeType(), gaugeData.getGreenRange(), gaugeData.getYellowRange(),
									gaugeData.getRedRange(), gaugeData.getMinMax());
	
							
							row.add(addGaugeColumnWithData(gaugeBox));		
										
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
			panel.add(txtName);
			txtName.setText("");
			Button btnSave = new Button("Save");
			panel.add(btnSave); 
			saveDialog.add(panel);
			saveDialog.setAutoHideEnabled(true);
			
			btnSave.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					
					final AnimationLoading animationLoading = new AnimationLoading("Saving gauges...");
					animationLoading.showLoadAnimation();

					
					LinkedList<GaugeData> gaugeDataList = new LinkedList<GaugeData>();
					
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
							
						}
						
					}
					
					GaugeServiceAsync gaugeService = GWT
							.create(GaugeService.class);
					
					gaugeService.saveDashboard(SessionHelper.getCurrentUserID(), txtName.getText(), gaugeDataList, new AsyncCallback<String>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(String result) {
							// TODO Auto-generated method stub
							AppController.openDashBoardWithSelected(result);
						}
						
					});
					saveDialog.removeFromParent();
					animationLoading.removeLoadAnimation();
				}
				
			});
			return saveDialog;

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
		
		Button gaugeButton = new Button("Add Gauge...");
		gaugeButton.setSize(ButtonSize.DEFAULT);
		gaugeButton.setType(ButtonType.SUCCESS);
		
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

		
		Button switchButton = new Button("Add Switch...");
		switchButton.setSize(ButtonSize.DEFAULT);
		switchButton.setType(ButtonType.PRIMARY);
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



