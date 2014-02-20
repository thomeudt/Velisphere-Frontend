package com.velisphere.tigerspice.client.checks;

import java.util.Iterator;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Legend;
import com.github.gwtbootstrap.client.ui.ListBox;
import com.github.gwtbootstrap.client.ui.Paragraph;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;
import com.velisphere.tigerspice.client.helper.AnimationLoading;
import com.velisphere.tigerspice.client.helper.DatatypeConfig;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassService;
import com.velisphere.tigerspice.client.propertyclasses.PropertyClassServiceAsync;
import com.velisphere.tigerspice.shared.PropertyClassData;

public class CheckNewDialogBox extends PopupPanel {

	@UiField
	ListBox lstPropertyID;
	@UiField
	ListBox lstOperator;
	@UiField
	Button btnSave;
	@UiField
	TextBox txtTriggerValue;
	@UiField
	TextBox txtCheckTitle;
	@UiField
	Paragraph txtEndpointName;
	
	
	private String endpointID;
	private String propertyID;
	private String propertyClassID;
	private String propertyName;
	private String checkTitle;
	private String operator;
	private String triggerValue;
	private String endpointName;

	private PropertyClassServiceAsync rpcServicePropertyClass;
	private CheckServiceAsync rpcServiceCheck;
	
	private AnimationLoading animationLoading = new AnimationLoading();

	private static CheckEditorDialogBoxUiBinder uiBinder = GWT
			.create(CheckEditorDialogBoxUiBinder.class);

	interface CheckEditorDialogBoxUiBinder extends
			UiBinder<Widget, CheckNewDialogBox> {
	}

	public CheckNewDialogBox(String endpointID, String propertyID,
			String propertyClassID, String propertyName, String endpointName) {
		
		this.endpointID = endpointID;
		this.propertyID = propertyID;
		this.propertyClassID = propertyClassID;
		this.propertyName = propertyName;
		this.endpointName = endpointName;

		setWidget(uiBinder.createAndBindUi(this));
		
		txtEndpointName.setText(this.endpointName);
				
		rpcServicePropertyClass = GWT.create(PropertyClassService.class);
		rpcServicePropertyClass.getPropertyClassForPropertyClassID(
				propertyClassID, new AsyncCallback<PropertyClassData>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);
					}

					@Override
					public void onSuccess(PropertyClassData result) {
						// TODO Auto-generated method stub

						DatatypeConfig dataTypeConfig = new DatatypeConfig();

						if (result.propertyClassDatatype
								.equalsIgnoreCase("string")) {
							Iterator<String> it = dataTypeConfig
									.getTextOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("byte")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("long")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("float")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("double")) {
							Iterator<String> it = dataTypeConfig
									.getNumberOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}

						else

						if (result.propertyClassDatatype
								.equalsIgnoreCase("boolean")) {
							Iterator<String> it = dataTypeConfig
									.getBooleanOperators().iterator();
							while (it.hasNext()) {
								String listItem = it.next();
								lstOperator.addItem(listItem);

							}
						}
						
						else lstOperator.addItem("Invalid Endpoint Configuration");

						lstOperator.setVisibleItemCount(1);

						// lstOperator.addItem(result.propertyClassName);
						removeLoadAnimation(animationLoading);

					}

				});

		lstOperator.setEnabled(true);
		lstOperator.setSelectedIndex(1);

		lstPropertyID.addItem(propertyName, propertyID);
		lstPropertyID.setVisibleItemCount(1);
		lstPropertyID.setEnabled(false);

	}

	
	@UiHandler("btnSave")
	void saveNewCheck (ClickEvent event) {

		this.checkTitle = txtCheckTitle.getText();
		this.operator = lstOperator.getValue();
		this.triggerValue = txtTriggerValue.getText();
		
		
		
		this.hide();
		/*
		 * 
		 * OLD IMPLEMENTATION / OBSOLETE
		showLoadAnimation(animationLoading);
		rpcServiceCheck = GWT.create(CheckService.class);
		rpcServiceCheck.addNewCheck(endpointID, propertyID, txtTriggerValue.getText(), lstOperator.getValue(), txtCheckTitle.getText(), 
				new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);
					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub
						removeLoadAnimation(animationLoading);
						currentWindow.hide();

					}

				});
		 */
		
		
		
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
	
	public String getCheckTitle(){
		return this.checkTitle;
	}
	
	public String getOperator(){
		return this.operator;
	}
	public String getTriggerValue(){
		return this.triggerValue;
	}

}
