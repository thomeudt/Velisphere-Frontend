package com.velisphere.tigerspice.shared;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.velisphere.tigerspice.client.rules.SameLevelCheckpathObject;

public class CheckPathObjectTree implements IsSerializable{

	public LinkedList<CheckPathObjectColumn> tree = new LinkedList<CheckPathObjectColumn>();
	public LinkedList<CheckPathObjectData> baseLayer = new LinkedList<CheckPathObjectData>();
}
