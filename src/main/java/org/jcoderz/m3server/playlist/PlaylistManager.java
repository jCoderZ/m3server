package org.jcoderz.m3server.playlist;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.jcoderz.m3server.util.Logging;

/**
 * This class manages all playlists in the system.
 *
 * @author mrumpf
 */
public final class PlaylistManager {

    private static final Logger logger = Logging.getLogger(PlaylistManager.class);
    private static final Map<String, Playlist> PLAYLIST_MAP = new HashMap<>();

    private PlaylistManager() {
        // do not allow instances of this class
    }

    /**
     * Returns a playlist for the specified device.
     *
     * @param device the name of the device
     * @return the playlist for the device
     */
    public static synchronized Playlist getPlaylist(String device) {
        Playlist result = PLAYLIST_MAP.get(device);
        if (result == null) {
            result = new Playlist(device);
            PLAYLIST_MAP.put(device, result);
        }
        return result;
    }

    /**
     * Returns a map of all playlists.
     *
     * @return the device playlist map
     */
    public static Map<String, Playlist> getPlaylists() {
        return PLAYLIST_MAP;
    }
}
