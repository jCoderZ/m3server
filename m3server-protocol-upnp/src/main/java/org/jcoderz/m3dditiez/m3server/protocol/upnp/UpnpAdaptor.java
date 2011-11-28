package org.jcoderz.m3dditiez.m3server.protocol.upnp;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.fourthline.cling.DefaultUpnpServiceConfiguration;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.model.meta.LocalDevice;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptor;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptorException;

/**
 * Implementation of the adaptor for the UPnP protocol. 
 *
 * @author Michael Rumpf
 *
 */
@Singleton
public class UpnpAdaptor implements ProtocolAdaptor {

	@Inject
	private Logger log;

	@Inject
	private LocalDevice device;

	@Override
	public void start() {
		try {
			int port = 8088;
			log.fine("Starting Cling UPnP server at port " + port);
			UpnpServiceConfiguration devcfg = new DefaultUpnpServiceConfiguration(port);
			UpnpService upnpService = new UpnpServiceImpl(devcfg);
			upnpService.getRegistry().addDevice(device);
			log.info("Successfully started the Cling UPnP server");
		} catch (Exception e) {
			log.log(Level.SEVERE, "The Cling UPnP server could not be started", e);
			throw new ProtocolAdaptorException("", e);
		}
	}

	@Override
	public void stop() {
		try {
			log.fine("Stopping Cling UPnP server");
			
			// ???
			
			log.info("Successfully stopped the Cling UPnP server");
		} catch (Exception e) {
			log.log(Level.SEVERE, "The Cling UPnP server could not be stopped", e);
			throw new ProtocolAdaptorException("", e);
		}
	}

	public String getName() {
		return UpnpAdaptor.class.getSimpleName();
	}
}
