package org.jcoderz.m3server.renderer;

import java.net.URL;

/**
 * http://mpd.wikia.com/wiki/Protocol_Reference
 * http://mpd.wikia.com/wiki/Built-in_HTTP_streaming_part_2
 *
 * @author mrumpf
 *
 */
public class MpdRenderer extends AbstractRenderer {

    /**
     * Constructor.
     *
     * @param name the name of the renderer
     */
    public MpdRenderer(String name) {
        super(name, RendererType.MPD);
    }

    @Override
    public void play(URL url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
