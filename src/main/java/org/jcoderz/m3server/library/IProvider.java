package org.jcoderz.m3server.library;

import java.util.Collection;

/**
 * The provider interface.
 */
public interface IProvider {
	String getName();
	void setName(String name);
	/**
	 * Browse a path and return a collection of items.
	 * 
	 * @param path
	 *            the path to browse
	 * @return the collection of items found for this path
	 */
	Collection<Item> browse(Path path);

	/**
	 * Returns the item, denoted by the path.
	 * @param path the path of the item
	 * @return the item
	 */
	Item item(Path path);
}
