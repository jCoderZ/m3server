package org.jcoderz.m3server;

import org.apache.commons.lang.builder.ToStringBuilder;

public class Item {

	private String path;
	private String name;
	private String icon;


	public Item() {
	
	}

	public Item(String path, String name, String icon) {
		this.path = path;
		this.name = name;
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
