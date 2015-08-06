package com.velisphere.blender.dataObjects;

import java.util.HashMap;
import java.util.Map;

public class EndpointPropertyLogData {

		HashMap<String, String> propertyValuePairs = new HashMap<String, String>();	
		String timeStamp;
		String source;
		String processedByID;
		
		public void addPropertyValuePair(String propertyID, String value){
			this.propertyValuePairs.put(propertyID, value);
		}
			
		public void setTimeStamp(String timeStamp){
			this.timeStamp = timeStamp;
		}
		
		public void setSource(String source){
			this.source = source;
		}
		
		public void setProcessedByID(String processedByID){
			this.processedByID = processedByID;
		}
		
		public Map<String, String> getPropertyValuePairs(){
			return this.propertyValuePairs;
		}
		
		public String getTimeStamp(){
			return this.timeStamp;
		}
		
		public String getSource(){
			return this.source;
		}
		
		public String getProcessedByID(){
			return this.processedByID;
		}
		

	
}
