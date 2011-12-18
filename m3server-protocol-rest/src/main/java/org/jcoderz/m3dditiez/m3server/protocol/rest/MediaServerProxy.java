package org.jcoderz.m3dditiez.m3server.protocol.rest;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.logging.Logging;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	private Logger log = LoggerFactory.getLogger(MediaServerProxy.class);

	//@Inject
//	private MediaServer ms;

	// TODO: add API methods
	@Get
	@Logging
	public String helloWorld() {
		log.debug("path=" + "helloWorld");
		java.util.logging.Logger l = java.util.logging.Logger.getLogger(MediaServerProxy.class.getName());
		l.info("###################################################################################");
//		if (ms == null)
//			return "MediaServer not set";
//		return ms.getRoots().toString();
		l.info("###################################################################################");
		return null;
	}

}
