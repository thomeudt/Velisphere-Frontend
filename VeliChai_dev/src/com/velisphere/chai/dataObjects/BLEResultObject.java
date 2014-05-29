package com.velisphere.chai.dataObjects;

import java.util.HashMap;
import java.util.LinkedList;

public class BLEResultObject {
	
	LinkedList<CheckObject> checks;
	LinkedList<MulticheckObject> multichecks;
	HashMap<String, String> triggerActions;
	LinkedList<String> checkpaths;

	
	public BLEResultObject(HashMap<String, String> triggerActions, LinkedList<CheckObject> checks,LinkedList<MulticheckObject> multichecks,LinkedList<String> checkpaths){
		this.triggerActions = triggerActions;
		this.checks = checks;
		this.multichecks = multichecks;
		this.checkpaths = checkpaths;
	}
	
	public HashMap<String, String> getTriggerActions(){
		return this.triggerActions;
	}
	
	public LinkedList<CheckObject> getChecks(){
		return this.checks;
	}
	
	public LinkedList<String> getCheckpaths(){
		return this.checkpaths;
	}
	
	
}
