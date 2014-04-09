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
package com.velisphere.tigerspice.client.helper;

import com.github.gwtbootstrap.client.ui.AccordionGroup;
import com.velisphere.tigerspice.client.rules.SameLevelCheckpathObject;
import com.velisphere.tigerspice.shared.CheckPathObjectData;

public class DragobjectContainer {

	public AccordionGroup accordionGroup;
	public String endpointID;
	public String endpointName;
	public String propertyID;
	public String propertyClassID;
	public String propertyName;
	public String checkID;
	public String checkName;
	public Boolean isMulticheck;
	public SameLevelCheckpathObject checkpathObject;
	public String endpointClassID;
}
