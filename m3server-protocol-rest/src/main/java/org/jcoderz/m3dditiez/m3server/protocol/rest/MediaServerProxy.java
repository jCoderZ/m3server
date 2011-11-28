package org.jcoderz.m3dditiez.m3server.protocol.rest;

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

	//@Inject
	private MediaServer ms;

	// TODO: add API methods
	@Get
	public String helloWorld() {
		if (ms == null)
			return "MediaServer not set";
		return ms.getRoots().toString();
	}

}
