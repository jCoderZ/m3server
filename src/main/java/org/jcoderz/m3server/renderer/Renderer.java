package org.jcoderz.m3server.renderer;

/**
 * The Renderer interface.
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
    void play(String url);

    /**
     * Plays any stored item.
     */
    void play();

    /**
     * Get info.
     */
    String info();

    /**
     * Stops playback of the renderer.
     */
    void stop();

    /**
     * Pauses playback of the renderer.
     */
    void pause();
}
