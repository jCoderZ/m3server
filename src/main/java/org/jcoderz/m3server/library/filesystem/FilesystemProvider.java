package org.jcoderz.m3server.library.filesystem;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.jcoderz.m3server.library.IProvider;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Path;

/**
 * Hello world!
 * 
 */
public class FilesystemProvider implements IProvider {
	private String name;
	private FolderItem rootFolderItem;
	private List<String> filters = new ArrayList<>();

	public List<String> getFilters() {
		return filters;
	}

	public void setFilters(String filters) {
		StringTokenizer strtok = new StringTokenizer(filters, "|");
		while (strtok.hasMoreTokens()) {
			String filter = strtok.nextToken();
			this.filters.add(filter);
		}
	}

	public void setRoot(String fsRoot) {
		if (fsRoot == null) {
			throw new IllegalArgumentException("root folder must not be null");
		}
		File rootFolder = new File(fsRoot);
		if (!rootFolder.exists()) {
			throw new IllegalArgumentException("root folder " + fsRoot
					+ "does not exist");
		}
		if (!rootFolder.isDirectory()) {
			throw new IllegalArgumentException("root " + fsRoot
					+ " is not a folder");
		}
		rootFolderItem = new FolderItem(null, null, rootFolder);

		// populate the top-level content
		readFolder(rootFolderItem);
	}

	@Override
	public Collection<Item> browse(Path path) {
		FolderItem folderItem = rootFolderItem;
		Iterator<String> iter = path.iterator();
		while (iter.hasNext()) {
			String segment = iter.next();
			Item childItem = folderItem.getChild(segment);
			if (FolderItem.class.isAssignableFrom(childItem.getClass())) {
				folderItem = (FolderItem) childItem;
				readFolder(folderItem);
			}
		}
		return folderItem.getChildren().values();
	}

	@Override
	public Item item(Path path) {
		FolderItem folderItem = rootFolderItem;
		Iterator<String> iter = path.iterator();
		Item result = null;
		while (iter.hasNext()) {
			String segment = iter.next();
			Item childItem = folderItem.getChild(segment);
			if (FolderItem.class.isAssignableFrom(childItem.getClass())) {
				folderItem = (FolderItem) childItem;
				readFolder(folderItem);
			} else {
				result = childItem;
			}
		}
		return result;
	}

	private void readFolder(FolderItem folderItem) {
		if (!folderItem.isRead()) {
			File[] files = folderItem.getFolder().listFiles();
			folderItem.setRead();
			for (int i = 0; i < files.length; i++) {
				addItem(files[i], folderItem);
			}
		}
	}

	private void addItem(File file, FolderItem parentItem) {
		if (file.isDirectory()) {
			FolderItem fi = new FolderItem(file.getName(), parentItem, file);
			parentItem.addChild(fi);
		} else if (file.isFile()) {
			FileItem fi = new FileItem(file.getName(), parentItem, file);
			parentItem.addChild(fi);
		} else {
			throw new IllegalArgumentException("Unknown file type '"
					+ file.toString() + "'");
		}
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
