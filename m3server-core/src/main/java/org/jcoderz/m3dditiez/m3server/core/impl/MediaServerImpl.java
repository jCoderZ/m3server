package org.jcoderz.m3dditiez.m3server.core.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.environment.osgi.api.Service;
import org.jboss.weld.environment.osgi.api.annotation.Publish;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the protocol adaptor and content provider independent part of
 * the media server. It gets a list of content providers injected which deal with any backends for
 * which plugins have been created.
 * 
 * @author Michael Rumpf
 * 
 */
@Publish
@Singleton
public class MediaServerImpl implements MediaServer {

	private static final Logger log = LoggerFactory.getLogger(MediaServerImpl.class);

	@Inject
	private Service<ContentProvider> providers;

	public void init() {
		System.err.println("yyy: " + getRoots());
	}

	public List<String> getRoots() {
		
		List<String> roots = new ArrayList<String>();
		for (ContentProvider p : providers) {
			log.debug("provider=" + p.getName());
			roots.add(p.getName());
		}
		return roots;
	}
}
