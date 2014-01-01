package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.spheres.SphereEditorWidget;

public class RuleView extends Composite {

	
	@UiField CheckpathEditorWidget wgtCheckpathEditor;
	private CheckPathServiceAsync rpcServiceCheckPath;

	
	private static RuleViewUiBinder uiBinder = GWT
			.create(RuleViewUiBinder.class);

	interface RuleViewUiBinder extends UiBinder<Widget, RuleView> {
	}
	
	private String userID;

	public RuleView() {

		  
			rpcServiceCheckPath = GWT.create(CheckPathService.class);
		     initWidget(uiBinder.createAndBindUi(this));
				
	}

	
	@UiHandler("btnSaveCheckpath")
	void saveCheckpath (ClickEvent event) {
		System.out.println("Save checkpath data: " + wgtCheckpathEditor);
	
		Iterator<MulticheckColumn<SameLevelCheckpathObject>> it =  wgtCheckpathEditor.multicheckColumns.iterator();
		while(it.hasNext()){
			MulticheckColumn<SameLevelCheckpathObject> multicheckColumn = it.next();
			Iterator<SameLevelCheckpathObject> checkIt = multicheckColumn.iterator();
			while(checkIt.hasNext()){
				
				System.out.println("Name:" +checkIt.next().text);
			}
		}
		
		
	}

	
	

}
