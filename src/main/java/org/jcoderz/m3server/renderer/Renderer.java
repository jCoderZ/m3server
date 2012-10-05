package org.jcoderz.m3server.renderer;

import java.net.URL;

/**
 * The Renderer interface. <ul> <li>MPD</li> <li>UPnP</li> <li>AirPlay</li>
 * <li>...</li> </ul>
 *
 * @author mrumpf
 *
 */
public interface Renderer {

    /**
     * Returns the name of the renderer.
     *
     * @return the name of the renderer
     */
    String getName();

    /**
     * Returns the type of the renderer.
     *
     * @return the type of the renderer
     */
    RendererType getType();

    /**
     * Sends a URL for playback to the renderer.
     *
     * @param url the URL to play
     */
    void play(URL url);
}
