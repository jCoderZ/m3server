package org.jcoderz.m3dditiez.m3server.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.core.config.Configuration;
import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;

/**
 * This class implements the protocol adaptor and content provider independent part of
 * the media server. It gets a list of content providers injected which deal with any backends for
 * which plugins have been created.
 * 
 * @author Michael Rumpf
 * 
 */
//@Singleton
public class MediaServerImpl implements MediaServer {

	//@Inject
	private Logger log;

	//@Inject
	private Configuration config;

	//@Inject
	//@Any
	private /*Instance<*/ List<ContentProvider> providers;

	public void init() {
		System.err.println("yyy: " + getRoots());
	}

	public List<String> getRoots() {
		List<String> roots = new ArrayList<String>();
		for (ContentProvider p : providers) {
			log.fine("provider=" + p.getName());
			roots.add(p.getName());
		}
		return roots;
	}
}
