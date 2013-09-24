package org.jcoderz.m3server.library.filesystem;

import java.io.File;

import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.LeafItem;

/**
 * Base class for filesystem items.
 */
public abstract class FilesystemItem extends LeafItem {
	private File file;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the item
	 * @param parent
	 *            the parent item
	 * @param file
	 *            the file
	 */
	public FilesystemItem(String name, Item parent, File file) {
		super(name, parent);
		this.file = file;
	}

	/**
	 * Set the file.
	 * 
	 * @param file
	 *            the file
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * Returns the file.
	 * 
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * Returns the file size.
	 * 
	 * @return the file size
	 */
	public long getSize() {
		return file.length();
	}

	/**
	 * Returns true when the instance is of type FolderItem.
	 * 
	 * @return true when the instance is of type FolderItem
	 */
	public boolean isFolder() {
		return FolderItem.class.equals(this.getClass());
	}

	/**
	 * Returns true when the instance is of type FileItem.
	 * 
	 * @return true when the instance is of type FileItem
	 */
	public boolean isFile() {
		return FileItem.class.equals(this.getClass());
	}
}
