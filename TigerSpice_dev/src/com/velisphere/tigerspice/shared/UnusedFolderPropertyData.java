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
package com.velisphere.tigerspice.shared;

import java.util.List;

@SuppressWarnings("serial")
public class UnusedFolderPropertyData extends PropertyData {

  private List<PropertyData> children;

 
  public UnusedFolderPropertyData() {
    super();
  }

  public List<PropertyData> getChildren() {
    return children;
  }

  public void setChildren(List<PropertyData> children) {
    this.children = children;
  }

  public void addChild(PropertyData child) {
    getChildren().add(child);
  }
}
