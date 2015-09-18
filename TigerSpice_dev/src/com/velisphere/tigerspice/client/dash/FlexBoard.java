package com.velisphere.tigerspice.client.dash;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.visualization.client.VisualizationUtils;

public class FlexBoard extends Composite{
	
	VerticalPanel verticalPanel;
	
	public FlexBoard()
	{
		
	
		verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		displayInitialRow();
		
	}
	
	private void displayInitialRow()
	{
		verticalPanel.add(addRow());
		verticalPanel.add(addRow());
	}
	
	private Row addRow()
	{
		Row row = new Row();
		row.add(addColumn());
		row.add(addColumn());
		row.add(addColumn());
		row.add(addColumn());
		row.add(addColumn());
		return row;
	}

	private Column addColumn()
	{
		Column column = new Column(2);
		column.add(new GaugeBox());
		return column;
	}
	
}
