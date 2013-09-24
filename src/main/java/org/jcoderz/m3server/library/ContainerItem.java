package org.jcoderz.m3server.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerItem extends Item {
	private Map<String, Item> children = new HashMap<>();
	private ContainerItem parent;

	public ContainerItem(String name, ContainerItem parent) {
		super(name);
		this.parent = parent;
	}

	/**
	 * Add a child item to this folder item.
	 * 
	 * @param child
	 *            the child item
	 */
	public synchronized void addChild(Item child) {
		children.put(child.getName(), child);
	}

	/**
	 * Returns a child item defined by its name.
	 * 
	 * @param name
	 *            the name of the child item
	 * @return the child item
	 */
	public Item getChild(String name) {
		return children.get(name);
	}

	/**
	 * Returns a list of child items.
	 * 
	 * @return the unmodifiable list of child items
	 */
	public Map<String, Item> getChildren() {
		return Collections.unmodifiableMap(children);
	}

	public ContainerItem getParent() {
		return parent;
	}

	public void setParent(ContainerItem parent) {
		this.parent = parent;
	}

	public Path getPath() {
		List<String> segments = new ArrayList<>();
		segments.add(getName());
		ContainerItem item = parent;
		while (item != null) {
			segments.add(item.getName());
			item = item.getParent();
		}
		Collections.reverse(segments);
		return new Path(segments);
	}

	/**
	 * Returns true when a list of child items.
	 * 
	 * @return the unmodifiable list of child items
	 */
	public boolean hasChildren() {
		return children != null;
	}


	@Override
	public String toString() {
		return getPath().toString() + " (" + children.size() + ")";
	}
}
