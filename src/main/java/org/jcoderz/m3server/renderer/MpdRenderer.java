package org.jcoderz.m3server.renderer;

/**
 * http://mpd.wikia.com/wiki/Protocol_Reference
 * http://mpd.wikia.com/wiki/Built-in_HTTP_streaming_part_2
 * 
 * @author mrumpf
 *
 */
public class MpdRenderer extends AbstractRenderer {

	public MpdRenderer(String name) {
		super(name, RendererType.MPD);
	}
}
