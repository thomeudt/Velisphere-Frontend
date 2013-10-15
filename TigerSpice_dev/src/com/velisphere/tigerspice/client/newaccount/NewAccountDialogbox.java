package com.velisphere.tigerspice.client.newaccount;

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


public class NewAccountDialogbox extends Composite {

interface MyBinder extends UiBinder<Widget, NewAccountDialogbox>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	
	public NewAccountDialogbox() {
		initWidget(uiBinder.createAndBindUi(this));
	
	}
	
	@UiHandler("btnLogin")
	void submitLoginForm (ClickEvent event)  {
		 Window.alert("Logging In");
		
	
	}
	
}
