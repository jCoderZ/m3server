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

/**
 * Implementation of the adaptor for the UPnP protocol. 
 *
 * @author Michael Rumpf
 *
 */
@Singleton
public class UpnpAdaptor /*implements ProtocolAdaptor */ {

	@Inject
	private Logger log;

	@Inject
	private LocalDevice device;
	
	//@Logging
	public void start() {
		try {
			int port = 8088;
			log.fine("Starting Cling UPnP server at port " + port);
			UpnpServiceConfiguration devcfg = new DefaultUpnpServiceConfiguration(port);
			UpnpService upnpService = new UpnpServiceImpl(devcfg);
			upnpService.getRegistry().addDevice(device);
			log.info("Successfully started Cling UPnP server");
		} catch (Exception ex) {
			log.log(Level.SEVERE, "The Cling UPnP server could not be started", ex);
		}
	}

	public String getName() {
		return UpnpAdaptor.class.getSimpleName();
	}
}
