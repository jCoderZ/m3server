/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcoderz.m3server.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * The path class provides convenience methods for dealing with library paths.
 */
public class Path {
	/** Denotes the root folder. */
	public static final Path ROOT = new Path(Path.SEPARATOR);

	private static final String SEPARATOR = "/";
	private List<String> pathSegments = new ArrayList<>();

	/**
	 * Constructor.
	 * 
	 * @param pathSegments
	 *            a list of path segments
	 */
	public Path(List<String> pathSegments) {
		this.pathSegments = new ArrayList<>();
		this.pathSegments.addAll(pathSegments);
	}

	/**
	 * Copy constructor.
	 * 
	 * @param path
	 *            the path to copy
	 */
	private Path(Path path) {
		pathSegments = new ArrayList<>();
		pathSegments.addAll(path.pathSegments);
	}

	public Path(String path) {
		if (!path.startsWith(SEPARATOR)) {
			throw new IllegalArgumentException(
					"Path must be absolute by starting with a " + SEPARATOR);
		}
		StringTokenizer strtok = new StringTokenizer(path, SEPARATOR);
		while (strtok.hasMoreTokens()) {
			String tok = strtok.nextToken();
			pathSegments.add(tok);
		}
	}

	/**
	 * Returns the segment at the given index.
	 * 
	 * @param index
	 *            the index
	 * @return the path segment denoted by the index
	 */
	public String index(int index) {
		if (index < 0) {
			throw new IllegalArgumentException("Segment index " + index
					+ " must not be smaller than 0");
		}
		if (index >= pathSegments.size()) {
			throw new IllegalArgumentException("Segment index " + index
					+ " larger than segments list (" + pathSegments.size()
					+ ")");
		}
		return pathSegments.get(index);
	}

	/**
	 * Returns the sub path from the given index to the end of the path.
	 * 
	 * @param startIndex
	 *            the amount of segments to skip.
	 * @return the sub path, starting from the specified index to the end of the
	 *         path
	 */
	public Path subpath(int startIndex) {
		if (startIndex < 0 || startIndex >= pathSegments.size()) {
			throw new IllegalArgumentException("The index '" + startIndex
					+ "' is out of range [0," + (pathSegments.size() - 1) + "]");
		}
		List<String> ps = new ArrayList<>();
		for (int i = startIndex; i < pathSegments.size(); i++) {
			ps.add(pathSegments.get(i));
		}
		return new Path(ps);
	}

	/**
	 * Returns the number of path segments.
	 * 
	 * @return the number of path segments.
	 */
	public int countSegments() {
		return pathSegments.size();
	}

	/**
	 * Returns the parent path, in case there is one, or null if not.
	 * 
	 * @return the parent path, in case there is one, or null if not.
	 */
	public Path parent() {
		Path parent = null;
		int index = pathSegments.size() - 1;
		if (index > 0) {
			parent = new Path(this);
			parent.pathSegments.remove(index);
		}
		return parent;
	}

	/**
	 * Returns a path segment iterator.
	 * 
	 * @return a path segment iterator.
	 */
	public Iterator<String> iterator() {
		return pathSegments.iterator();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Path) {
			Path other = (Path) o;
			return pathSegments.equals(other.pathSegments);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return pathSegments.hashCode();
	}

	/**
	 * @InheritDoc
	 */
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(SEPARATOR);
		for (int i = 0; i < pathSegments.size(); i++) {
			str.append(pathSegments.get(i));
			if (i < pathSegments.size() - 1) {
				str.append(SEPARATOR);
			}
		}
		return str.toString();
	}
}
