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
package com.velisphere.tigerspice.client.spheres;


import java.util.HashSet;






import java.util.LinkedList;
import java.util.Vector;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.velisphere.tigerspice.shared.EPCData;
import com.velisphere.tigerspice.shared.EndpointData;
import com.velisphere.tigerspice.shared.PropertyData;
import com.velisphere.tigerspice.shared.SphereData;
import com.velisphere.tigerspice.shared.UserData;

@RemoteServiceRelativePath("voltSphere")
public interface SphereService extends RemoteService {
	LinkedList<SphereData> getAllSpheres();
	LinkedList<SphereData> getAllSpheresForUserID(String userID);
	String updateSpherenameForSphereID(String sphereID, String sphereName);
	String updatePublicStateForSphereID(String sphereID, int publicState);
	String addSphere(String userID, String sphereName);
	SphereData getSphereForSphereID(String sphereID);
	LinkedList<SphereData> getPublicSpheresForUserID(String userID);
}


