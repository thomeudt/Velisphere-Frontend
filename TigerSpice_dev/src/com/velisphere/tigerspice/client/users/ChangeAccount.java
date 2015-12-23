package com.velisphere.tigerspice.client.users;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.appcontroller.SessionHelper;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;

public class ChangeAccount extends Composite {

	@UiField
	PasswordTextBox txtOldPW;
	@UiField
	PasswordTextBox txtNewPW;
	@UiField
	PasswordTextBox txtConfirmPW;
	@UiField
	Button btnChangePassword;
	@UiField
	Button btnCloseAccount;
	@UiField
	Alert aleSuccess;
	@UiField
	Alert aleError;
	@UiField
	HTML htmChangeHeader;
	@UiField
	HTML htmCloseHeader;


	
	
	private static ChangeAccountUiBinder uiBinder = GWT
			.create(ChangeAccountUiBinder.class);

	interface ChangeAccountUiBinder extends UiBinder<Widget, ChangeAccount> {
	}

	public ChangeAccount() {
		initWidget(uiBinder.createAndBindUi(this));
		buildPage();
	}

	private void buildPage()
	{
		aleSuccess.setVisible(false);
		aleError.setVisible(false);
		htmChangeHeader.setHTML("<h3>Change Password</h3>");
		htmCloseHeader.setHTML("<h3>Close Account</h3>");
		btnChangePassword.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event) {
				executePwChange();
				
			}
			
		});
		
	}
	
	private void executePwChange()
	{
		
		
	
		if(txtConfirmPW.getText().equals(txtNewPW.getText()))
		{
			final LoginServiceAsync loginService = GWT
					.create(LoginService.class);

			
			
			loginService.changePassword(SessionHelper.getCurrentUserID(), txtOldPW.getText(), txtNewPW.getText(), new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					aleError.setText("Password change request could not be processed. Did you enter the correct old password?");
					aleError.setVisible(true);
					aleSuccess.setVisible(false);
				}

				@Override
				public void onSuccess(Boolean result) {
					// TODO Auto-generated method stub
					
					if (result == true)
					{
						aleSuccess.setText("Password change successful!");
						aleSuccess.setVisible(true);
						aleError.setVisible(false);
					}
					else
					{
						aleError.setText("Password change request could not be processed. Did you enter the correct old password?");
						aleError.setVisible(true);
						aleSuccess.setVisible(false);
					}
					
				}

			});
			
		}
		
		else
			
		{
			aleError.setText("Your passwords in 'New Password' and 'Confirm New Password' do not match!");
			aleError.setVisible(true);
			aleSuccess.setVisible(false);
		}
		
			
	}	

}
