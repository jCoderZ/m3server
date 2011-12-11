package org.jcoderz.m3dditiez.m3server.protocol.rest;

import javax.inject.Inject;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptor;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptorException;
import org.osgi.service.log.LogService;
import org.restlet.Server;
import org.restlet.data.Protocol;

/**
 * Implementation of the adaptor for the REST protocol.
 * 
 * @author Michael Rumpf
 * 
 */
public class RestletAdaptor implements ProtocolAdaptor {

	private Server restServer;

	@Inject @OSGiService
	LogService log;
	
	@Inject @OSGiService
	private MediaServer server;
	
	@Override
	public void start() {
		try {
			int port = 8082; //config.getInteger(RestletAdaptor.class, "port");
			log.log(LogService.LOG_INFO, "Starting RESTlet server at port " + port);
			restServer = new Server(Protocol.HTTP, port, MediaServerProxy.class);
			restServer.start();
			log.log(LogService.LOG_INFO, "Successfully started RESTlet server");
		} catch (Exception e) {
			log.log(LogService.LOG_ERROR, "The RESTlet server could not be started", e);
			throw new ProtocolAdaptorException(
					"An unknown exception occured while starting the RESTlet server",
					e);
		}
	}

	@Override
	public void stop() {
		try {
			log.log(LogService.LOG_INFO, "Stopping RESTlet server at port...");
			restServer.stop();
			log.log(LogService.LOG_INFO, "RESTlet server has been stopped");
		} catch (Exception e) {
			log.log(LogService.LOG_ERROR, "The RESTlet server could not be stopped", e);
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
