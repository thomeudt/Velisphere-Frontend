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

public class FilterAppliedEvent extends GwtEvent<FilterAppliedEventHandler> {

		
		public static Type<FilterAppliedEventHandler> TYPE = new Type<FilterAppliedEventHandler>();
		
		private String sphereID;
		private String endpointID;

		public FilterAppliedEvent(String sphereID, String endpointID)
		{
			this.sphereID = sphereID;
			this.endpointID = endpointID;
		}
		
		@Override
		public Type<FilterAppliedEventHandler> getAssociatedType() {
		    return TYPE;
		}

		@Override
		protected void dispatch(FilterAppliedEventHandler handler) {
		    handler.onFilterApplied(this);
		}
		
		public String getSphereID()
		{
			return this.sphereID;
		}
		
		public String getEndpointID()
		{
			return this.endpointID;
		}
}
