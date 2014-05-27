package com.velisphere.chai.dataObjects;

import java.util.HashMap;
import java.util.LinkedList;

public class BLEResultObject {
	
	LinkedList<CheckObject> checks;
	LinkedList<MulticheckObject> multichecks;
	HashMap<String, String> triggerActions;

	
	public BLEResultObject(HashMap<String, String> triggerActions, LinkedList<CheckObject> checks,LinkedList<MulticheckObject> multichecks){
		this.triggerActions = triggerActions;
		this.checks = checks;
		this.multichecks = multichecks;
	}
	
	public HashMap<String, String> getTriggerActions(){
		return this.triggerActions;
	}
	
	public LinkedList<CheckObject> getChecks(){
		return this.checks;
	}
	
	
}
