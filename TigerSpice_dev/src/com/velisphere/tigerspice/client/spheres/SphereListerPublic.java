package com.velisphere.tigerspice.client.spheres;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import com.github.gwtbootstrap.client.ui.Column;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Row;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.velisphere.tigerspice.client.analytics.AnalyticsService;
import com.velisphere.tigerspice.client.analytics.AnalyticsServiceAsync;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.client.users.UserServiceAsync;
import com.velisphere.tigerspice.shared.SphereData;

public class SphereListerPublic extends Composite {
	
	ListBox lstSpheres;
	MultiWordSuggestOracle userNameOracle;
	HashMap<String, String> userDirectory;
	
	public SphereListerPublic() {

		initWidget(createBaseLayout());
		getUsers();
		
	}
	
	private VerticalPanel createBaseLayout(){
		
		Row row1 = new Row();
		
		Column col1 = new Column(2);
		col1.add(new HTML("<b>Step 1 <br></b>Search User by Name"));
		row1.add(col1);
		
		Column col2 = new Column(4);
		
		userNameOracle = new MultiWordSuggestOracle();
		userDirectory = new HashMap<String, String>();
		
		SuggestBox suggestBox = new SuggestBox(userNameOracle);
		suggestBox.setWidth("100%");
		
		col2.add(suggestBox);
		
		
		row1.add(col2);
		
		
		Row row2 = new Row();
		
		Column col3 = new Column(2);
		col3.add(new HTML("<b>Step 2</b><br>Selected User is Sharing:"));
		row2.add(col3);
		
		Column col4 = new Column(4);
		lstSpheres = new ListBox();
		lstSpheres.setVisibleItemCount(7);
		lstSpheres.setWidth("100%");
				
		col4.add(lstSpheres);
		
		row2.add(col4);
		
			
		suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>(){

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				// TODO Auto-generated method stub
				
				
				getSpheres(event.getSelectedItem().getReplacementString());
				
				
			}
		});
		
		lstSpheres.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if(lstSpheres != null){
					AppController.openPublicSphere(lstSpheres.getValue());	
				}
				
			}
			
		});
		
		VerticalPanel vP = new VerticalPanel();
		vP.add(row1);
		vP.add(row2);
		return vP;
		
	}
	
	private void getUsers(){
		
		UserServiceAsync userService = GWT
				.create(UserService.class);
		
		userService.getAllUsersWithPublicSpheres(new AsyncCallback<HashMap<String, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(HashMap<String, String> result) {
				// TODO Auto-generated method stub
				
				userNameOracle.addAll(result.keySet());
				userDirectory.putAll(result);
				
					
			}
			
		});

		
	}

	
	private void getSpheres(String userName)
	{
		
		lstSpheres.clear();
		String selectedUserID = userDirectory.get(userName);

		SphereServiceAsync sphereService = GWT
				.create(SphereService.class);
		
		sphereService.getPublicSpheresForUserID(selectedUserID, new AsyncCallback<LinkedList<SphereData>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			

			@Override
			public void onSuccess(LinkedList<SphereData> result) {
				// TODO Auto-generated method stub
				
				Iterator<SphereData> it = result.iterator();
				
				while (it.hasNext()){
				
					SphereData current = it.next();
					lstSpheres.addItem(current.sphereName, current.sphereId);
				}
				
				
			}
			
		});

		
		
	}
	
}
