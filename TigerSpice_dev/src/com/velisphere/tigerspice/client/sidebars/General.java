package com.velisphere.tigerspice.client.sidebars;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.AppController;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpoints.EndpointService;
import com.velisphere.tigerspice.client.endpoints.EndpointServiceAsync;
import com.velisphere.tigerspice.client.favorites.FavoriteService;
import com.velisphere.tigerspice.client.favorites.FavoriteServiceAsync;
import com.velisphere.tigerspice.client.helper.VeliConstants;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.FavoriteData;

public class General extends Composite {

	@UiField Anchor ancProvisioning;
	@UiField Anchor ancMarket;
	@UiField Anchor ancGettingStarted;
	@UiField Anchor ancBestPractices;
	@UiField ListBox ddlShortcut;
	@UiField ListBox ddlFavs;
	
	private static GeneralUiBinder uiBinder = GWT.create(GeneralUiBinder.class);

	interface GeneralUiBinder extends UiBinder<Widget, General> {
	}

	public General() {
		initWidget(uiBinder.createAndBindUi(this));
		ancProvisioning.setHref("#");
		ancMarket.setHref("#");
		ancGettingStarted.setHref("#");
		ancBestPractices.setHref("#");
		//ddlFavs.setVisibleItemCount(3);
		populateShortcuts();
		populateFavorites();

	}

	
	void populateShortcuts(){
		EndpointServiceAsync rpcServiceEndpoint = GWT.create(EndpointService.class);
		//ddlShortcut.setWidth("150px");
		ddlShortcut.addItem("Select Endpoint");
		rpcServiceEndpoint.getEndpointsForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<EndpointData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(LinkedList<EndpointData> result) {
				Iterator<EndpointData> it = result.iterator();
				while(it.hasNext()){
					EndpointData endpointData = it.next();
					ddlShortcut.addItem(endpointData.endpointName, endpointData.endpointId);
				}
				
			}

		});
		
		ddlShortcut.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				AppController.openEndpoint(ddlShortcut.getValue());
			}
		});

	}
	
	
	
	void populateFavorites(){
		
		
	
		final FavoriteServiceAsync favoriteService = GWT
				.create(FavoriteService.class);
		
		ddlFavs.addItem("Select Favorite");
		
		favoriteService.getAllFavoritesForUser(SessionHelper.getCurrentUserID(), new AsyncCallback<LinkedList<FavoriteData>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}


			@Override
			public void onSuccess(LinkedList<FavoriteData> result) {
				
				Iterator<FavoriteData> it = result.iterator();
				while(it.hasNext()){
					FavoriteData favoriteData = it.next();
					
					
					ddlFavs.addItem(favoriteData.getName(), favoriteData.getId());
					
				}
			}

		});
		
		ddlFavs.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {

				favoriteService.getFavoriteForFavoriteID(ddlFavs.getValue(), new AsyncCallback<FavoriteData>(){

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onSuccess(FavoriteData result) {
						// TODO Auto-generated method stub
						
						if(result.getType() == VeliConstants.FAVORITE_ENDPOINT){
							AppController.openEndpoint(result.getTargetId());
						}
						
						if(result.getType() == VeliConstants.FAVORITE_LOGIC){
							AppController.openLogicDesign(result.getTargetId());
						}
						
					}

					
					
				});
				
				
			}
		});

	}
	
	
	@UiHandler("ancProvisioning")
	void openProvisioning (ClickEvent event) {
	
		AppController.openProvisioningWizard();
		
		
	}
	
	@UiHandler("ancMarket")
	void openMarket (ClickEvent event) {
	
		AppController.openMarket();
	}
	
	@UiHandler("ancGettingStarted")
	void openGettingStarted (ClickEvent event) {
	
		AppController.openGettingStarted();		
	}

	@UiHandler("ancBestPractices")
	void openBestPractices (ClickEvent event) {
	
		AppController.openBestPractices();		
	}

}
