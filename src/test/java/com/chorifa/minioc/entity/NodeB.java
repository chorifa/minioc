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
		return ver;
	}

	@Override
	public String toString() {
		return "NodeB has field NodeA with ver = "+nodeA.getVersion();
	}

	static class NewNode{}

}
