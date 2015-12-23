package com.velisphere.tigerspice.client.users;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.Login;
import com.velisphere.tigerspice.client.users.ChangeAccount.ChangeAccountUiBinder;

public class CloseAccountConfirm extends Composite {

	private static CloseAccountConfirmUiBinder uiBinder = GWT
			.create(CloseAccountConfirmUiBinder.class);

	interface CloseAccountConfirmUiBinder extends
			UiBinder<Widget, CloseAccountConfirm> {
	}

	public CloseAccountConfirm() {
		initWidget(uiBinder.createAndBindUi(this));
		buildPage();
	}

	

	@UiField
	Button btnLogout;
	@UiField
	Alert aleSuccess;
	@UiField
	Alert aleError;
	@UiField
	HTML htmChangeHeader;
	

	
	
	
	private void buildPage()
	{
		aleSuccess.setVisible(false);
		aleError.setVisible(false);
		htmChangeHeader.setHTML("<b>Your account has been deleted. Please click logout to end your session. Thanks for having tried Velisphere!</b>");
		
		btnLogout.addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event) {
				Cookies.removeCookie("sid", "/");
				
				Login loginScreen = new Login();
				loginScreen.onModuleLoad();
				
			}
			
		});
		
	}
}
