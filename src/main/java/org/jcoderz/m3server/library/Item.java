package org.jcoderz.m3server.library;


public abstract class Item {
	private String name;
	// TODO: Use AOP
	private long lastaccess;

	public Item(String name) {
		this.name = name;
		lastaccess = System.currentTimeMillis();
	}

	public String getName() {
		lastaccess = System.currentTimeMillis();
		return name;
	}

	public void setName(String name) {
		lastaccess = System.currentTimeMillis();
		this.name = name;
	}

	public abstract Path getPath();

	public abstract Item getParent();
}
