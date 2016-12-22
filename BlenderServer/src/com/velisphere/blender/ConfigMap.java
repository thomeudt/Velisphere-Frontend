package com.velisphere.blender;

import java.util.HashMap;

public class ConfigMap {

	static HashMap<String, String> configMap = new HashMap<String, String>();

	public static String getUrl( String id) {
		return ConfigMap.configMap.get(id);
	}

	public static void addURL(String id, String url) {
		ConfigMap.configMap.put(id, url);
	}
	
	
	
}
