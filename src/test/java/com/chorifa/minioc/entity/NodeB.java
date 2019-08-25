package com.chorifa.minioc.entity;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Named("nodeB")
@Singleton
public class NodeB implements Node {

	private int ver = 11;

	@Inject
	@Named("nodeA")
	private Node nodeA;


	public NodeB() {
	}

	@Override
	public int getVersion() {
		System.out.println("NodeB: getVersion has already run.");
		return ver;
	}

	@Override
	public String toString() {
		System.out.println("NodeB: toString has already run.");
		return "NodeB has field NodeA with ver = "+nodeA.getVersion();
	}

	static class NewNode{}

}
