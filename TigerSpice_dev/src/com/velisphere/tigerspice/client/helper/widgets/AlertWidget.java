package com.velisphere.tigerspice.client.helper.widgets;

import com.github.gwtbootstrap.client.ui.Alert;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.AlertType;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class AlertWidget extends Composite {
	
	public AlertWidget (HTML message, AlertType alertType)
	{
		HorizontalPanel hP = new HorizontalPanel();
		if(alertType == AlertType.WARNING){
			hP.add(new Icon(IconType.WARNING_SIGN));	
		}
		
		hP.add(message);
		Alert alert = new Alert();
		alert.add(hP);
		alert.setType(alertType);
		initWidget(alert);
	}

}
