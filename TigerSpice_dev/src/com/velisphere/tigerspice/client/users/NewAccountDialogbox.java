package com.velisphere.tigerspice.client.users;



import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class NewAccountDialogbox extends Composite {

interface MyBinder extends UiBinder<Widget, NewAccountDialogbox>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	@UiField TextBox txtEmail; 
	@UiField TextBox txtUsername;
	@UiField TextBox txtPassword;
	@UiField CheckBox cbxAgreeTandC;
	@UiField Alert aleError;
	
	
	
	private final UserServiceAsync userService = GWT
			.create(UserService.class);
	
	public NewAccountDialogbox() {
		txtEmail = new TextBox();
		txtUsername = new TextBox();
		txtPassword = new PasswordTextBox();
		cbxAgreeTandC = new CheckBox();
		aleError = new Alert();
		
		
		initWidget(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
	
	
	}
	
	@UiHandler("btnLogin")
	void submitLoginForm (ClickEvent event)  {
		 
			if (cbxAgreeTandC.getValue() == true)
			{
		 
			userService.addNewUser(txtUsername.getText(), txtEmail.getText(), txtPassword.getText(), new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					RootPanel.get("main").clear();
					NewAccountSuccessMessage newAccountSuccessMessage = new NewAccountSuccessMessage();
					RootPanel.get("main").add(newAccountSuccessMessage);
					
				}
				
			});
			} else
			{
				
				aleError.setText("You have to accept our terms and conditions before signing up.");
				aleError.setVisible(true);
			}
		}
	
}
