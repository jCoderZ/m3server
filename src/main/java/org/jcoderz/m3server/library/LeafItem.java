package org.jcoderz.m3server.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class LeafItem extends Item {
	private ContainerItem parent;

	public LeafItem(String name, Item parent) {
		super(name);
		if (ContainerItem.class.isAssignableFrom(parent.getClass())) {
			this.parent = (ContainerItem) parent;
		} else {
			throw new IllegalArgumentException(
					"Parameter parent is not of type ContainerItem");
		}
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

	@Override
	public String toString() {
		return getPath().toString();
	}
}
