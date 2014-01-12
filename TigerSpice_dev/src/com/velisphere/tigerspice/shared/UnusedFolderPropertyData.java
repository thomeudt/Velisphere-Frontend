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
