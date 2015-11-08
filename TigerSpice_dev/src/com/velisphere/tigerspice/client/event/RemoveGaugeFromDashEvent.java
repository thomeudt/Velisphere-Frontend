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
package com.velisphere.tigerspice.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Widget;
import com.velisphere.tigerspice.client.dash.GaugeBox;
import com.velisphere.tigerspice.client.logic.draggables.PhysicalItem;

public class RemoveGaugeFromDashEvent extends GwtEvent<RemoveGaugeFromDashEventHandler> {


		public static Type<RemoveGaugeFromDashEventHandler> TYPE = new Type<RemoveGaugeFromDashEventHandler>();
		GaugeBox gaugeBox;
		
		public RemoveGaugeFromDashEvent(GaugeBox gaugeBox)
		{
			this.gaugeBox = gaugeBox;
		}
		
		  @Override
		public Type<RemoveGaugeFromDashEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(RemoveGaugeFromDashEventHandler handler) {
		    handler.onGaugeBoxRemoved(this);
		}
		
	
		
		public GaugeBox getGaugeBox()
		{
			return this.gaugeBox;
		}
		
}
