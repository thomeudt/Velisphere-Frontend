package com.velisphere.tigerspice.client.rules;

import com.github.gwtbootstrap.client.ui.Paragraph;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.velisphere.tigerspice.client.event.CheckpathCalculatedEvent;
import com.velisphere.tigerspice.client.event.CheckpathCalculatedEventHandler;
import com.velisphere.tigerspice.client.event.EventUtils;


public class CheckpathCostIndicator extends Composite {


	private HandlerRegistration calcUpdateHandler;
	private Paragraph cost;
	private CheckpathEditorWidget wgtCheckpathEditor;
	
	public CheckpathCostIndicator(){
		this.cost = new Paragraph();
		this.cost.setText("###");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(cost);
		initWidget(hp);
	}
	
	public void setCheckpathEditor (CheckpathEditorWidget wgtCheckpathEditor){
		this.wgtCheckpathEditor = wgtCheckpathEditor;

		initCostUpdater();
		
		
	}
	
	void initCostUpdater(){
		
		
		calcUpdateHandler = EventUtils.EVENT_BUS.addHandler(CheckpathCalculatedEvent.TYPE, new CheckpathCalculatedEventHandler()     {
			@Override
		    public void onCheckpathCalculated(CheckpathCalculatedEvent checkpathCalculatedEvent) {
				System.out.println("FIRED");
				//calcUpdateHandler.removeHandler();
				cost.setText("USD " + wgtCheckpathEditor.getCost());
								
			}		
		});
	}
	
}
