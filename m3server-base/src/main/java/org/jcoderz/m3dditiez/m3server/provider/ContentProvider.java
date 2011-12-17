package org.jcoderz.m3dditiez.m3server.provider;

import java.util.List;

/**
 * The generic interface of all content providers.
 *
 * @author Michael Rumpf
 *
 */
public interface ContentProvider {

	/**
	 * Returns the name which is displayed on the top level of the media server hierarchy (TODO: I18n).
	 *  
	 * @return the name of the provider
	 */
	String getName();
	
	/**
	 * Returns a list of children.
	 *
	 * @param path the path to get the children for
	 * @return a list of children of the specified path
	 */
	List<String> getChildren(String path);
}
