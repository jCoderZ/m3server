package org.jcoderz.m3server.renderer;

import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.playlist.Playlist;

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
     * @return the item represented by the path
     */
    Item playpath(String url);

    /**
     * Plays any stored URL.
     */
    void play();

    /**
     * Returns information on the renderer.
     *
     * @return information on the renderer
     */
    Info info();

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

    /**
     * Sets a playlist on the renderer.
     *
     * @param playlist the playlist to set
     */
    void playlist(Playlist playlist);

    /**
     * Returns the playlist of the renderer.
     *
     * @return the playlist of the renderer
     */
    Playlist playlist();
}
