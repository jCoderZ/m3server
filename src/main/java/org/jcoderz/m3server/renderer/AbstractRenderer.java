package org.jcoderz.m3server.renderer;

public abstract class AbstractRenderer implements Renderer {

	private String name;
	private RendererType type;

	public AbstractRenderer() {

	}

	public AbstractRenderer(String name, RendererType type) {
		this.name = name;
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public RendererType getType() {
		return type;
	}

}
