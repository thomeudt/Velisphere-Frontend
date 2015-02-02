package com.velisphere.tigerspice.client.logic.layoutWidgets;

public class LinkedPair<L, R> {

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
