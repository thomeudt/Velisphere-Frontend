/*******************************************************************************
 * CONFIDENTIAL INFORMATION
 *  __________________
 *  
 *   Copyright (C) 2013 Thorsten Meudt 
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
package com.velisphere.tigerspice.client.helper;

import com.github.gwtbootstrap.client.ui.Strong;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class AnimationLoading extends Composite {

	@UiField Strong stgLoading;
	
	private static AnimationLoadingUiBinder uiBinder = GWT
			.create(AnimationLoadingUiBinder.class);

	interface AnimationLoadingUiBinder extends
			UiBinder<Widget, AnimationLoading> {
	}

	public AnimationLoading() {
		initWidget(uiBinder.createAndBindUi(this));
		stgLoading.addStyleName("vcenter");
	}
	
	public void showLoadAnimation() {
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(this, 25, 40);
	}

	public void showLoadAnimation(String text) {
		stgLoading.setText(text);
		RootPanel rootPanel = RootPanel.get("main");
		rootPanel.getElement().getStyle().setPosition(Position.RELATIVE);
		rootPanel.add(this, 25, 40);
	}


}
