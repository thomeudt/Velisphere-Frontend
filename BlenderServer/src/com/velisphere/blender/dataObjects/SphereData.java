package com.velisphere.blender.dataObjects;

public class SphereData {
	
		public String sphereId;
		public String sphereName;
		public Integer sphereIsPublic;
		
		
		
		public String getId(){
			return sphereId;
		}
		
		public String getName(){
			return sphereName;
		}
		
		public Integer getIsPublic(){
			return sphereIsPublic;
		}
		
		public void setName(String sphereName){
			this.sphereName = sphereName;
		}
		
		public void setIsPublic(int sphereIsPublic){
			this.sphereIsPublic = sphereIsPublic;
		}

}
