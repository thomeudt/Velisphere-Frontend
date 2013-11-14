package com.velisphere.tigerspice.client.checks;


import java.util.Iterator;




import com.github.gwtbootstrap.client.ui.ListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class CheckNewDialogBox extends PopupPanel {

	@UiField ListBox lstPropertyID;
	@UiField ListBox lstOperator; 
	
	private PropertyClassServiceAsync rpcServicePropertyClass;
	private AnimationLoading animationLoading = new AnimationLoading();
	
	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, CheckNewDialogBox> {
	}

	public CheckNewDialogBox(String endpointID, String propertyID, String propertyClassID) {
		
		setWidget(uiBinder.createAndBindUi(this));
		rpcServicePropertyClass = GWT.create(PropertyClassService.class);
		rpcServicePropertyClass.getPropertyClassForPropertyClassID(propertyClassID,
				new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);
					}

					@Override
					public void onSuccess(PropertyClassData result) {
						// TODO Auto-generated method stub
						
						DatatypeConfig dataTypeConfig = new DatatypeConfig();
						
						
						
						if(result.propertyClassName.equalsIgnoreCase("text"))
						{
							Iterator<String> it = dataTypeConfig.getTextOperators().iterator();
							while(it.hasNext()){
								String listItem = it.next();
								lstOperator.addItem(listItem);
								
							}
						}
						
						lstOperator.setVisibleItemCount(1);
						
					
						//lstOperator.addItem(result.propertyClassName);						
						removeLoadAnimation(animationLoading);


					}

				});

		lstOperator.setEnabled(true);
		lstOperator.setSelectedIndex(1);
		
		lstPropertyID.addItem(propertyID, propertyID);
		lstPropertyID.setVisibleItemCount(1);
		
		
		
		
	}
	
	private void showLoadAnimation(AnimationLoading animationLoading) {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(animationLoading, 25, 40);
	}

	private void removeLoadAnimation(AnimationLoading animationLoading) {
		if (animationLoading != null)
			animationLoading.removeFromParent();
	}

}
