package com.velisphere.tigerspice.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class HeroUnitLogin extends Composite implements HasText {

	private static HeroUnitLoginUiBinder uiBinder = GWT
			.create(HeroUnitLoginUiBinder.class);

	interface HeroUnitLoginUiBinder extends UiBinder<Widget, HeroUnitLogin> {
	}

	public HeroUnitLogin() {
		initWidget(uiBinder.createAndBindUi(this));
		
			
	}
	
	@UiHandler("btnLogin")
	void openLoginForm (ClickEvent event) {
		// Window.alert("Logging In");
		RootPanel rootPanel = RootPanel.get("stockList");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		LoginDialogBox loginDialogBox = new LoginDialogBox();
		rootPanel.add(loginDialogBox, 250, 250);
		
		// loginDialogBox.setVisible(false);
		
		
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}
	
}