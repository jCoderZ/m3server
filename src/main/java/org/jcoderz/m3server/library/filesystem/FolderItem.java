package org.jcoderz.m3server.library.filesystem;

import java.io.File;

import org.jcoderz.m3server.library.ContainerItem;

/**
 * Represents a folder item.
 */
public class FolderItem extends ContainerItem {
	private boolean isRead = false;
	private File folder;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the item
	 * @param parent
	 *            the parent item
	 * @param folder
	 *            the folder file
	 */
	public FolderItem(String name, FolderItem parent, File folder) {
		super(name, parent);
		this.folder = folder;
	}


	public File getFolder() {
		return folder;
	}

	public void setFolder(File folder) {
		this.folder = folder;
	}

	/**
	 * Indicate that the children have been read.
	 */
	public void setRead() {
		isRead = true;
	}

	/**
	 * Return the flag whether the children have been read.
	 */
	public boolean isRead() {
		return isRead;
	}
}
