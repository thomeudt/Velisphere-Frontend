package com.velisphere.toucan.xmlRootElements;
import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MessageElement {

	
		  private String summary;
		  private LinkedList<String> description;
		  public String getSummary() {
		    return summary;
		  }
		  public void setSummary(String summary) {
		    this.summary = summary;
		  }
		  public LinkedList<String> getDescription() {
		    return description;
		  }
		  public void setDescription(LinkedList<String> description) {
		    this.description = description;
		  }

}
