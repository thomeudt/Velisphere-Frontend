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
import com.velisphere.tigerspice.shared.DashData;
import com.velisphere.tigerspice.shared.GaugeData;

public class FlexBoard extends Composite{
	
	VerticalPanel verticalPanel;
	//int rowCount = 0;
	//HashMap<Integer, Row> rowMap;
	LinkedList<GaugeBox> gaugeList;
	FlexColumn currentAddBoxColumn;
	static HTML SPACER = new HTML("&nbsp;<br>");
	Row row;
	String dashID;
	
	// Default constructor for a new FlexBoard
	
	public FlexBoard()
	{
		
		gaugeList = new LinkedList<GaugeBox>();
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		//rowMap = new HashMap<Integer, Row>();
		displayHeader();
		displayInitialField();
		
	}
	
	// Constructor to load an existing FlexBoard
	
	public FlexBoard(String dashID)
	{
		this.dashID = dashID;
		gaugeList = new LinkedList<GaugeBox>();
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		//rowMap = new HashMap<Integer, Row>();
		displayHeader();
		loadExistingFields();
		
	}
	
	
	private void displayHeader()
	{
		row = new Row();
		verticalPanel.add(row);
		Column saveDashboardColumn = new Column(2);
		Button btnSave = new Button("Save");
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
					
					LinkedList<GaugeData> gaugeDataList = new LinkedList<GaugeData>();
					
					Iterator<GaugeBox> it = gaugeList.iterator();
					while (it.hasNext())
					{
						GaugeBox gaugeBox = it.next();
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
					
					GaugeServiceAsync gaugeService = GWT
							.create(GaugeService.class);
					
					gaugeService.saveDashboard(txtName.getText(), gaugeDataList, new AsyncCallback<String>(){

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
		
		Button button = new Button("Add +");
		button.setSize(ButtonSize.LARGE);
		button.setType(ButtonType.SUCCESS);
		panel.add(button);
		
		button.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
		//		Row lastRow = rowMap.get(rowCount);
				row.remove(currentAddBoxColumn);
				
		/*		
				if(fieldCount % 6 == 0)
				{
					RootPanel.get().add(new HTML("ROW ADDING REQUESTED"));
					verticalPanel.add(SPACER);
					addRow();
					lastRow = rowMap.get(rowCount);
				}
			*/	
				row.add(addGaugeColumn());		
				
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
	
	private Column addGaugeColumnWithData(GaugeBox gaugeBox)
	{
		
		FlexColumn column = new FlexColumn(2);
		column.add(gaugeBox);
		gaugeList.add(gaugeBox);
		return column;
	}
	
	
	
	
	
}



