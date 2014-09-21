/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2014 Thorsten Meudt / Connected Things Lab
 *   All Rights Reserved.
 *  
 *  NOTICE:  All information contained herein is, and remains
 *  the property of Thorsten Meudt and its suppliers,
 *  if any.  The intellectual and technical concepts contained
 *  herein are proprietary to Thorsten Meudt
 *  and its suppliers and may be covered by Patents,
 *  patents in process, and are protected by trade secret or copyright law.
 *  Dissemination of this information or reproduction of this material
 *  is strictly forbidden unless prior written permission is obtained
 *  from Thorsten Meudt.
 ******************************************************************************/
package com.velisphere.tigerspice.client.users;



import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.CheckBox;
import com.github.gwtbootstrap.client.ui.Image;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;


public class NewAccountWidget extends Composite {

interface MyBinder extends UiBinder<Widget, NewAccountWidget>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	@UiField TextBox txtEmail; 
	@UiField TextBox txtUsername;
	@UiField TextBox txtPassword;
	@UiField CheckBox cbxAgreeTandC;
	@UiField Alert aleError;
	@UiField Image imgCaptchaImage;
	@UiField TextBox txtCaptchaWord;
	
	
	
	private final UserServiceAsync userService = GWT
			.create(UserService.class);
	
	public NewAccountWidget() {
		txtEmail = new TextBox();
		txtUsername = new TextBox();
		txtPassword = new PasswordTextBox();
		cbxAgreeTandC = new CheckBox();
		aleError = new Alert();
		
		
		initWidget(uiBinder.createAndBindUi(this));
		aleError.setVisible(false);
		imgCaptchaImage.setUrl("/SimpleCaptcha.jpg");
	
	
	}
	
	@UiHandler("btnLogin")
	void submitLoginForm (ClickEvent event)  {
		 
			if (cbxAgreeTandC.getValue() == true)
			{
		 
			userService.addNewUser(txtUsername.getText(), txtEmail.getText(), txtPassword.getText(), txtCaptchaWord.getText(), new AsyncCallback<String>(){

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(String result) {
					// TODO Auto-generated method stub
					
					if (result.equals("OK")){
						RootPanel.get("main").clear();
						NewAccountSuccessMessage newAccountSuccessMessage = new NewAccountSuccessMessage();
						RootPanel.get("main").add(newAccountSuccessMessage);	
					} else
						aleError.setText("Signup failed - make sure you entered the right captcha word.");
					aleError.setVisible(true);
					
					
				}
				
			});
			} else
			{
				
				aleError.setText("You have to accept our terms and conditions before signing up.");
				aleError.setVisible(true);
			}
		}
	
	@UiHandler("ancReloadCaptcha")
	void reloadCaptcha(ClickEvent event){
		imgCaptchaImage.setUrl("/SimpleCaptcha.jpg?load_"+Random.nextInt());
	}
	
}
