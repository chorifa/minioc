package com.chorifa.minioc.entity;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named("nodeA")
@Singleton
public class NodeA implements Node {

	private int ver = 10;


	private Node nodeB;

	public NodeA() {
	}

	@Inject
	public NodeA(@Named("nodeB") Node nodeB){
		this.nodeB = nodeB;
	}

	@Override
	public int getVersion() {
		System.out.println("NodeA: getVersion has already run");
		return ver;
	}

	@Override
	public String toString() {
		System.out.println("NodeA: toString has already run.");
		return "NodeA has field NodeB with ver = "+nodeB.getVersion();
	}

	static class NewNode{}

}
