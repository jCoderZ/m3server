package org.jcoderz.m3dditiez.m3server.protocol.upnp;

import javax.inject.Inject;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.LocalDevice;
import org.jboss.weld.environment.osgi.api.annotation.OSGiService;
import org.jcoderz.m3dditiez.m3server.core.MediaServer;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptor;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptorException;
import org.osgi.service.log.LogService;

/**
 * Implementation of the adaptor for the UPnP protocol. 
 *
 * @author Michael Rumpf
 *
 */
public class UpnpAdaptor implements ProtocolAdaptor {

	@Inject @OSGiService
	private LogService log;

	private LocalDevice device;

	@Inject @OSGiService
	private MediaServer server;
	
	@Override
	public void start() {
		try {
			int port = 8081;
			log.log(LogService.LOG_INFO, "Starting Cling UPnP server at port " + port);
			UpnpServiceConfiguration devcfg = new ApacheUpnpServiceConfiguration(port);
			UpnpService upnpService = new UpnpServiceImpl(devcfg);
			upnpService.getRegistry().addDevice(device);
			log.log(LogService.LOG_INFO, "Successfully started the Cling UPnP server");
		} catch (Exception e) {
			log.log(LogService.LOG_ERROR, "The Cling UPnP server could not be started", e);
			throw new ProtocolAdaptorException("", e);
		}
	}

	@Override
	public void stop() {
		try {
			log.log(LogService.LOG_INFO, "Stopping Cling UPnP server");
			
			// ???
			
			log.log(LogService.LOG_INFO, "Successfully stopped the Cling UPnP server");
		} catch (Exception e) {
			log.log(LogService.LOG_ERROR, "The Cling UPnP server could not be stopped", e);
			throw new ProtocolAdaptorException("", e);
		}
	}

	public String getName() {
		return UpnpAdaptor.class.getSimpleName();
	}
}
