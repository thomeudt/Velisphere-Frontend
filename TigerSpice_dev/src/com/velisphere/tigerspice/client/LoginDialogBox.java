

package com.velisphere.tigerspice.client;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.*;

public class LoginDialogBox extends PopupPanel{
	
	interface MyBinder extends UiBinder<Widget, LoginDialogBox>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	public LoginDialogBox() {
		setStyleName("");
		add(uiBinder.createAndBindUi(this));
	}
	

}
