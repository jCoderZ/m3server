package org.jcoderz.m3dditiez.m3server.protocol.rest;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jcoderz.m3dditiez.m3server.core.config.Configuration;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptor;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptorException;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Implementation of the adaptor for the REST protocol.
 * 
 * @author Michael Rumpf
 * 
 */
@Singleton
public class RestletAdaptor implements ProtocolAdaptor {

	private Server server;
	@Inject
	private Logger log;

	@Inject
	private Configuration config;

	@Override
	public void start() {
		try {
			int port = config.getInteger(RestletAdaptor.class, "port");
			log.fine("Starting RESTlet server at port " + port);
			server = new Server(Protocol.HTTP, port, MediaServerProxy.class);
			server.start();
			log.info("Successfully started RESTlet server");
		} catch (Exception e) {
			log.log(Level.SEVERE, "The RESTlet server could not be started", e);
			throw new ProtocolAdaptorException(
					"An unknown exception occured while starting the RESTlet server",
					e);
		}
	}

	@Override
	public void stop() {
		try {
			log.fine("Stopping RESTlet server at port...");
			server.stop();
			log.info("RESTlet server has been stopped");
		} catch (Exception e) {
			log.log(Level.SEVERE, "The RESTlet server could not be stopped", e);
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
