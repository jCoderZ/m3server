package org.jcoderz.m3dditiez.m3server.protocol.rest;

import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
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

	private Server server;

	@OSGiService
	LogService log;

//	@Inject
//	private Configuration config;

	@Override
	public void start() {
		System.out.println("Restlet starting>>>: log=" + log);
		try {
			int port = 8082; //config.getInteger(RestletAdaptor.class, "port");
			log.log(LogService.LOG_INFO, "Starting RESTlet server at port " + port);
			server = new Server(Protocol.HTTP, port, MediaServerProxy.class);
			server.start();
			log.log(LogService.LOG_INFO, "Successfully started RESTlet server");
		} catch (Exception e) {
			log.log(LogService.LOG_ERROR, "The RESTlet server could not be started", e);
			throw new ProtocolAdaptorException(
					"An unknown exception occured while starting the RESTlet server",
					e);
		}
		System.out.println("<<<Restlet starting");
	}

	@Override
	public void stop() {
		try {
			log.log(LogService.LOG_INFO, "Stopping RESTlet server at port...");
			server.stop();
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
