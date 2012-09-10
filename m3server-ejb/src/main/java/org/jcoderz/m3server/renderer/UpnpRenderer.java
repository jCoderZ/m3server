package org.jcoderz.m3server.renderer;

public class UpnpRenderer  extends AbstractRenderer {

	public UpnpRenderer(String name, String host,
			int port) {
		super(name, RendererType.UPNP, host, port);
	}
}
