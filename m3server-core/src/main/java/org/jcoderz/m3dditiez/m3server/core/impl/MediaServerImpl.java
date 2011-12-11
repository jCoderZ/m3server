package org.jcoderz.m3dditiez.m3server.core.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jboss.weld.environment.osgi.api.annotation.Publish;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.provider.ContentProvider;
import org.osgi.service.log.LogService;

/**
 * This class implements the protocol adaptor and content provider independent part of
 * the media server. It gets a list of content providers injected which deal with any backends for
 * which plugins have been created.
 * 
 * @author Michael Rumpf
 * 
 */
@Publish
//@Singleton
public class MediaServerImpl implements MediaServer {

	@Inject @OSGiService
	private LogService log;

	private /*Instance<*/ List<ContentProvider> providers;

	public void init() {
		System.err.println("yyy: " + getRoots());
	}

	public List<String> getRoots() {
		
		List<String> roots = new ArrayList<String>();
		for (ContentProvider p : providers) {
			log.log(LogService.LOG_DEBUG, "provider=" + p.getName());
			roots.add(p.getName());
		}
		return roots;
	}
}
