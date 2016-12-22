package com.velisphere.tigerspice.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class LinkedPair<L, R> implements IsSerializable {

	private final L left;
	private final R right;
	
	public LinkedPair(L left, R right)
	{
		this.left = left;
		this.right = right;
	}
	
	public L getLeft(){
		return this.left;
	}
	
	public R getRight(){
		return this.right;
	}

	
}
