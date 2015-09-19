package com.velisphere.tigerspice.client.dash;

import java.util.HashMap;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.github.gwtbootstrap.client.ui.constants.ButtonType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;

public class FlexBoard extends Composite{
	
	VerticalPanel verticalPanel;
	int fieldCount = 0;
	int rowCount = 0;
	HashMap<Integer, Row> rowMap;
	FlexColumn currentAddBoxColumn;
	
	
	public FlexBoard()
	{
		
	
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		rowMap = new HashMap<Integer, Row>();
		displayInitialField();
		
	}
	
	private void displayInitialField()
	{
		Row initialRow = addRow();
		verticalPanel.add(initialRow);
		currentAddBoxColumn = addAddBoxColumn();
		
		initialRow.add(currentAddBoxColumn);
		
	}
	
	private Row addRow()
	{
		Row row = new Row();
		rowCount ++;
		rowMap.put(rowCount, row);
		verticalPanel.add(row);
		return row;
	}


	private FlexColumn addAddBoxColumn()
	{
		FlexColumn column = new FlexColumn(2);
		column.add(addBox());
		fieldCount ++;
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
				
				Row lastRow = rowMap.get(rowCount);
				lastRow.remove(currentAddBoxColumn);
				
				
				if(fieldCount % 6 == 0)
				{
					RootPanel.get().add(new HTML("ROW ADDING REQUESTED"));
					addRow();
					lastRow = rowMap.get(rowCount);
				}
				
				lastRow.add(addGaugeColumn());		
				RootPanel.get().add(new HTML("GAUGE ADDING REQUESTED, ROWCOUNT " + rowCount));
				
				currentAddBoxColumn = addAddBoxColumn();
				lastRow.add(currentAddBoxColumn);

			}
			
		});
		
		return panel;
		
	}

	private Column addGaugeColumn()
	{
		
		FlexColumn column = new FlexColumn(2);
		column.add(new GaugeBox());
		fieldCount ++;
		return column;
	}
			
	
	
}



