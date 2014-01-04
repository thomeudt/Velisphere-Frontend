
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
	package com.velisphere.tigerspice.shared;

	import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

	public class CheckPathData implements IsSerializable, Comparable <CheckPathData>
	{
		

		public String checkpathId;
		public String checkpathName;
		public String uiObjectJSON;
		
		@Override
		public int compareTo(CheckPathData arg0) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		public String getCheckpathId(){
			return this.checkpathId;
		}
		
		public String getCheckpathName(){
			return this.checkpathName;
		}
		
		public String getUiObjectJSON(){
			return this.uiObjectJSON;
		}
		
		
	}
