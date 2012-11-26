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
     * Plays any stored URL.
     */
    void play();

    /**
     * Returns information on the renderer.
     *
     * @return information on the renderer
     */
    String info();

    /**
     * Gets position information.
     * 
     * @return position information
     */
    Position position();

    /**
     * Stops playback of the renderer.
     */
    void stop();

    /**
     * Pauses playback of the renderer.
     */
    void pause();

    /**
     * Returns the volume level of the renderer.
     *
     * @param level the volume level of the renderer
     */
    void volume(long level);

    /**
     * Returns the volume level of the renderer.
     * 
     * @return the volume level of the renderer
     */
    long volume();
}
