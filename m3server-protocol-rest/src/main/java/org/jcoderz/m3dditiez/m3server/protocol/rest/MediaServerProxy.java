package org.jcoderz.m3dditiez.m3server.protocol.rest;

import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

/**
 * Implements the server resources for the REST API protocol adaptor.
 *
 * @author Michael Rumpf
 *
 */
//@Singleton
public class MediaServerProxy extends ServerResource {

	@Inject @OSGiService
	private MediaServer server;
	

	//@Inject
//	private MediaServer ms;

	// TODO: add API methods
	@Get
	public String helloWorld() {
//		if (ms == null)
//			return "MediaServer not set";
//		return ms.getRoots().toString();
		return null;
	}

}
