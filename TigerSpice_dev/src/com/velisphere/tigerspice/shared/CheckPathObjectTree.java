package com.velisphere.tigerspice.shared;

import java.util.LinkedList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CheckPathObjectTree implements IsSerializable{

	public LinkedList<CheckPathObjectColumn> tree = new LinkedList<CheckPathObjectColumn>();
}
