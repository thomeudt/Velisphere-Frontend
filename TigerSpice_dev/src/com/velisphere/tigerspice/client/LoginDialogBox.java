

package com.velisphere.tigerspice.client;

import java.io.IOException;

import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.ProcCallException;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class LoginDialogBox extends PopupPanel{
	
	interface MyBinder extends UiBinder<Widget, LoginDialogBox>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	public LoginDialogBox() {
		setStyleName("");
		add(uiBinder.createAndBindUi(this));
		
	}
	
	@UiHandler("btnLogin")
	void submitLoginForm (ClickEvent event)  {
		// Window.alert("Logging In");
		EndpointClassEditor endpointClassEditor = new EndpointClassEditor();
		endpointClassEditor.open();
	
	}
	

}
