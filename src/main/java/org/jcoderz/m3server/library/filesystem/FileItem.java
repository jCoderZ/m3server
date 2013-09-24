package org.jcoderz.m3server.library.filesystem;

import java.io.File;

import org.jcoderz.m3server.library.Item;

/**
 * Represents a file item.
 */
public class FileItem extends FilesystemItem {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the file
	 * @param parent
	 *            the parent item
	 * @param file
	 *            the file
	 */
	public FileItem(String name, Item parent, File file) {
		super(name, parent, file);
	}
}
