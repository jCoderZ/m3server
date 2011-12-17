package org.jcoderz.m3dditiez.m3server.protocol.upnp;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceConfiguration;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;
import org.fourthline.cling.support.avtransport.impl.AVTransportService;
import org.fourthline.cling.support.connectionmanager.ConnectionManagerService;
import org.jcoderz.m3dditiez.m3server.logging.Logging;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptor;
import org.jcoderz.m3dditiez.m3server.protocol.ProtocolAdaptorException;
import org.slf4j.Logger;

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

	private static final DeviceType MEDIA_SERVER_DEVICE_TYPE = new UDADeviceType(
			"MediaServer", 3);

	@Override
	public void start() {
		try {
			int port = 8081;
			log.info("Starting Cling UPnP server at port " + port);
			UpnpServiceConfiguration devcfg = new ApacheUpnpServiceConfiguration(
					port);
			UpnpService upnpService = new UpnpServiceImpl(devcfg);
			upnpService.getRegistry().addDevice(device);
			log.info("Successfully started the Cling UPnP server");
		} catch (Exception e) {
			log.error("The Cling UPnP server could not be started", e);
			throw new ProtocolAdaptorException("", e);
		}
	}

	@Override
	public void stop() {
		try {
			log.info("Stopping Cling UPnP server");

			// ???

			log.info("Successfully stopped the Cling UPnP server");
		} catch (Exception e) {
			log.error("The Cling UPnP server could not be stopped", e);
			throw new ProtocolAdaptorException("", e);
		}
	}

	public String getName() {
		return UpnpAdaptor.class.getSimpleName();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Produces
	@Logging
	public LocalDevice createDevice() {

		DeviceIdentity identity = new DeviceIdentity(
				UDN.uniqueSystemIdentifier("m3server"));

		DeviceDetails details = new DeviceDetails(
				"m3server",
				new ManufacturerDetails("jCoderz.org"),
				new ModelDetails(
						"MediaServer",
						"An implementation of the UPnP media server specification",
						"v1"));

		AnnotationLocalServiceBinder alsb = new AnnotationLocalServiceBinder();
		LocalService<ConnectionManagerService> connectionManagerService = alsb
				.read(ConnectionManagerService.class);
		connectionManagerService
				.setManager(new DefaultServiceManager<ConnectionManagerService>(
						connectionManagerService,
						ConnectionManagerService.class));

		LocalService<AVTransportService> avTransportService = new AnnotationLocalServiceBinder()
				.read(AVTransportService.class);
		avTransportService
				.setManager(new DefaultServiceManager<AVTransportService>(
						avTransportService, AVTransportService.class));

		LocalService<ContentDirectory> contentDirectory = new AnnotationLocalServiceBinder()
				.read(ContentDirectory.class);
		contentDirectory
				.setManager(new DefaultServiceManager<ContentDirectory>(
						contentDirectory, ContentDirectory.class));

		LocalDevice device = null;
		try {
			device = new LocalDevice(identity, MEDIA_SERVER_DEVICE_TYPE,
					details, new LocalService[] { connectionManagerService,
							avTransportService, contentDirectory });
		} catch (ValidationException ex) {
			ex.printStackTrace();
			System.err.println(ex.getErrors());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return device;
	}
}
