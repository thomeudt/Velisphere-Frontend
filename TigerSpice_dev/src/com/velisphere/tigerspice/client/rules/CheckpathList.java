package com.velisphere.tigerspice.client.rules;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class CheckpathList extends Composite {

	@UiField
	Breadcrumbs brdMain;
	
	@UiField
	ListBox lstCheckpath;
	
	NavLink bread0;
	NavLink bread1;
	
	private CheckPathServiceAsync rpcServiceCheckPath;
	
	private static CheckpathListUiBinder uiBinder = GWT
			.create(CheckpathListUiBinder.class);

	interface CheckpathListUiBinder extends UiBinder<Widget, CheckpathList> {
	}

	public CheckpathList() {
		initWidget(uiBinder.createAndBindUi(this));
		bread0 = new NavLink();
		bread0.setText("Home");
		brdMain.add(bread0);
		bread1 = new NavLink();
		bread1.setText("Logic Designer");
		brdMain.add(bread1);
		rpcServiceCheckPath = GWT.create(CheckPathService.class);
		
		// load existing checkpaths
		
		

		rpcServiceCheckPath.getAllCheckpaths(
				new AsyncCallback<LinkedHashMap<String, String>>() {

					@Override
					public void onFailure(
							Throwable caught) {
						// TODO Auto-generated method
						// stub
						System.out
								.println("ERROR SAVING JSON: "
										+ caught);

					}

					@Override
					public void onSuccess(LinkedHashMap<String, String> result) {
						// TODO Auto-generated method
						// stub
						
						Iterator<Entry<String, String>> it = result.entrySet().iterator();
						while (it.hasNext()){
							Map.Entry<String, String> checkPair = (Map.Entry<String, String>)it.next();
							lstCheckpath.addItem(checkPair.getValue(), checkPair.getKey());
						}
						lstCheckpath.setVisibleItemCount(7);

					}
				});
		
		
		
	}
	
	
	@UiHandler("btnCreateCheckpath")
	void createCheckpath(ClickEvent event) {
	
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		
		CheckpathCreateView checkpathView = new CheckpathCreateView(); 		
		mainPanel.add(checkpathView);

		
	}

	@UiHandler("btnOpenCheckpath")
	void openCheckpath(ClickEvent event) {
	
		RootPanel mainPanel = RootPanel.get("main");
		mainPanel.clear();
		
		CheckpathEditView checkpathView = new CheckpathEditView(lstCheckpath.getValue()); 		
		mainPanel.add(checkpathView);

		
	}

}
