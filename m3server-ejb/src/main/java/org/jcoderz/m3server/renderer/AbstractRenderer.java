package org.jcoderz.m3server.renderer;

public abstract class AbstractRenderer implements Renderer {

	private String name;
	private RendererType type;
	private String host;
	private int port;

	public AbstractRenderer() {

	}

	public AbstractRenderer(String name, RendererType type, String host,
			int port) {
		this.name = name;
		this.type = type;
		this.host = host;
		this.port = port;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public RendererType getType() {
		return type;
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}

}
