package com.velisphere.tigerspice.client.users;



import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.LoginDialogBox;


public class NewAccountSuccessMessage extends Composite {

interface MyBinder extends UiBinder<Widget, NewAccountSuccessMessage>{}
	
	private static MyBinder uiBinder = GWT.create(MyBinder.class);
	
	public NewAccountSuccessMessage() {
		
		initWidget(uiBinder.createAndBindUi(this));
			
		
	}
	

	@UiHandler("btnLogin")
	void openLoginForm (ClickEvent event) {
		// Window.alert("Logging In");
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		LoginDialogBox loginDialogBox = new LoginDialogBox();
		rootPanel.add(loginDialogBox, 250, 250);
		
		// loginDialogBox.setVisible(false);
		
		
	}
	
	
	
}
