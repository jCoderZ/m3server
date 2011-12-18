package org.jcoderz.m3dditiez.m3server.protocol.rest;


import javax.enterprise.inject.Any;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptor;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptorException;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.slf4j.Logger;

/**
 * Implementation of the adaptor for the REST protocol.
 * 
 * @author Michael Rumpf
 * 
 */
public class RestletAdaptor implements ProtocolAdaptor {

	private @Inject @Any Logger log;
	
	@Inject @OSGiService
	private MediaServer server;

	@Inject
	private Server restServer;

	@Override
	public void start() {
		try {
			log.info("Starting RESTlet server at port " + restServer.getPort());
			restServer.start();
			log.info("Successfully started RESTlet server");
		} catch (Exception e) {
			log.error("The RESTlet server could not be started", e);
			throw new ProtocolAdaptorException(
					"An unknown exception occured while starting the RESTlet server",
					e);
		}
	}

	private @Produces Server createServer() {
		int port = 8082; //config.getInteger(RestletAdaptor.class, "port");
		return new Server(Protocol.HTTP, port, MediaServerProxy.class);
	}

	@Override
	public void stop() {
		try {
			log.info("Stopping RESTlet server at port...");
			restServer.stop();
			log.info("RESTlet server has been stopped");
		} catch (Exception e) {
			log.error("The RESTlet server could not be stopped", e);
			throw new ProtocolAdaptorException(
					"An unknown exception occured while stopping the RESTlet server",
					e);
		}
	}

	@Override
	public String getName() {
		return RestletAdaptor.class.getSimpleName();
	}
}
