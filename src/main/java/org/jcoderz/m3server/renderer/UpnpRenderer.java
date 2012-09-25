package org.jcoderz.m3server.renderer;

import org.teleal.cling.model.meta.Device;

public class UpnpRenderer extends AbstractRenderer {
	private Device device;

	public UpnpRenderer(Device device) {
		super(device.getDetails().getFriendlyName(), RendererType.UPNP);
		this.device = device;
	}
	
	
}
