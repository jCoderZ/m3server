package org.jcoderz.m3dditiez.m3server.protocol.rest;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.logging.Logging;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;

/**
 * Implements the server resources for the REST API protocol adaptor.
 *
 * @author Michael Rumpf
 *
 */
@Singleton
public class MediaServerProxy extends ServerResource {

	@Inject @OSGiService
	private MediaServer server;

	private @Inject Logger log;

	//@Inject
//	private MediaServer ms;

	// TODO: add API methods
	@Get
	@Logging
	public String helloWorld() {
		log.debug("path=" + "helloWorld");
//		if (ms == null)
//			return "MediaServer not set";
//		return ms.getRoots().toString();
		return null;
	}

}
