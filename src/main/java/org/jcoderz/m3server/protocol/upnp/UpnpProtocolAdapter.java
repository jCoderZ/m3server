package org.jcoderz.m3server.protocol.upnp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.protocol.ProtocolAdapter;
import org.jcoderz.m3server.renderer.Renderer;
import org.jcoderz.m3server.renderer.RendererRegistry;
import org.jcoderz.m3server.renderer.UpnpRenderer;
import org.jcoderz.m3server.util.Logging;
import org.teleal.cling.UpnpService;
import org.teleal.cling.UpnpServiceImpl;
import org.teleal.cling.model.meta.LocalDevice;
import org.teleal.cling.model.meta.RemoteDevice;
import org.teleal.cling.registry.Registry;
import org.teleal.cling.registry.RegistryListener;

/**
 * The UPnP protocol adapter hides all UPnP details behind a common interface.
 *
 * @author mrumpf
 */
public class UpnpProtocolAdapter extends ProtocolAdapter implements RegistryListener {

    private static final Logger logger = Logging.getLogger(UpnpProtocolAdapter.class);
    private static final String MEDIA_RENDERER = "MediaRenderer";
    private UpnpService upnpService;
    private LocalDevice mediaServerDevice;

    @Override
    public void startup() {

        // UPnP discovery is asynchronous, we need a callback
        upnpService = new UpnpServiceImpl(this);

        mediaServerDevice = UpnpMediaServer.createDevice(getConfiguration());
        upnpService.getRegistry().addDevice(mediaServerDevice);

        // broadcast an immediate search message for all devices
        upnpService.getControlPoint().search();
    }

    @Override
    public void shutdown() {
        upnpService.getRegistry().removeDevice(mediaServerDevice);
        upnpService.shutdown();
    }

    @Override
    public String getName() {
        return UpnpProtocolAdapter.class.getSimpleName();
    }

    @Override
    public void remoteDeviceAdded(Registry registry, RemoteDevice device) {
        logger.log(Level.INFO, "Remote device available: {0} ({1})", new Object[]{device.getDetails().getFriendlyName(), device.getType().getType()});
        // we deal with MediaRenderer devices only
        if (MEDIA_RENDERER.equals(device.getType().getType())) {
            Renderer r = RendererRegistry.findRenderer(device.getDetails().getFriendlyName());
            if (r == null) {
                r = new UpnpRenderer(upnpService, device);
                RendererRegistry.addRenderer(r);
            }
        }
    }

    @Override
    public void remoteDeviceRemoved(Registry registry, RemoteDevice device) {
        Renderer r = RendererRegistry.findRenderer(device.getDisplayString());
        if (r != null) {
            RendererRegistry.removeRenderer(r);
        }
        RendererRegistry.dumpRegistry();
    }

    @Override
    public void remoteDeviceDiscoveryStarted(Registry registry,
            RemoteDevice device) {
        // do nothing
    }

    @Override
    public void remoteDeviceDiscoveryFailed(Registry registry,
            RemoteDevice device, Exception ex) {
        // do nothing
    }

    @Override
    public void remoteDeviceUpdated(Registry registry, RemoteDevice device) {
        // do nothing
    }

    @Override
    public void localDeviceAdded(Registry registry, LocalDevice device) {
        // do nothing
    }

    @Override
    public void localDeviceRemoved(Registry registry, LocalDevice device) {
        // do nothing
    }

    @Override
    public void beforeShutdown(Registry registry) {
        // do nothing
    }

    @Override
    public void afterShutdown() {
        // do nothing
    }
}
