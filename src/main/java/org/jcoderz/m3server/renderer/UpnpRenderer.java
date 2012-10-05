package org.jcoderz.m3server.renderer;

import java.net.URL;
import org.teleal.cling.model.meta.Device;

public class UpnpRenderer extends AbstractRenderer {

    private Device device;

    /**
     * Constructor.
     *
     * @param name the name of the renderer
     */
    public UpnpRenderer(Device device) {
        super(device.getDetails().getFriendlyName(), RendererType.UPNP);
        this.device = device;
    }

    @Override
    public void play(URL url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
