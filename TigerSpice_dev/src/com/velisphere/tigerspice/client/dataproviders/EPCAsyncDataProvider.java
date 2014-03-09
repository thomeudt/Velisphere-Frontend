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
package com.velisphere.tigerspice.client.dataproviders;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.velisphere.tigerspice.client.endpointclasses.EPCService;
import com.velisphere.tigerspice.client.endpointclasses.EPCServiceAsync;
import com.velisphere.tigerspice.client.users.UserService;
import com.velisphere.tigerspice.client.users.UserServiceAsync;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.UserData;

/**
 * An AsynchDataProvider using RPC call to get all Photo Information from the
 * server. Used in the GWT in Action 2nd Edition CellList example
 * 
 */
public class EPCAsyncDataProvider extends AsyncDataProvider<EPCData> {

	/**
	 * Reference to the RPC service this provider will use to get data.
	 */
	private EPCServiceAsync rpcService;

	/**
	 * Create a new AllDataAsyncDataProvider instance and set up the RPC
	 * framework that it will use.
	 */
	public EPCAsyncDataProvider() {
		rpcService = GWT.create(EPCService.class);
	}

	/**
	 * {@link #onRangeChanged(HasData)} is called when the table requests a new
	 * range of data. You can push data back to the displays using
	 * {@link #updateRowData(int, List)}.
	 */
	@Override
	protected void onRangeChanged(HasData<EPCData> display) {

		// Get the new range required.
		final Range range = display.getVisibleRange();

		ColumnSortList sortList = ((AbstractCellTable<EPCData>) display)
				.getColumnSortList();
		String sortOnName = "epcName";
		boolean isAscending = true;
		if ((sortList != null) && (sortList.size() > 0)) {
			sortOnName = sortList.get(0).getColumn().getDataStoreName();
			isAscending = sortList.get(0).isAscending();
		}

		// Call getPhotoList RPC call
		rpcService.getAllEndpointClassDetails(new AsyncCallback<Vector<EPCData>>() {

			// There's been a failure in the RPC call
			// Normally you would handle that in a good way,
			// here we just throw up an alert.
			public void onFailure(Throwable caught) {
				Window.alert("Error" + caught.getMessage());
			}

			@Override
			public void onSuccess(Vector<EPCData> result) {
				updateRowData(range.getStart(), result);

			}

			// We've successfully for the data from the RPC call,
			// Now we update the row data with that result starting
			// at a particular row in the cell widget (usually the range start)

		});
	}
}
