package org.jcoderz.m3server.playlist;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.jcoderz.m3server.library.Item;

/**
 * This class manages the playlists in the system.
 *
 * @author mrumpf
 */
public final class PlaylistManager {

    private static final Map<String, Playlist> PLAYLIST_MAP = new HashMap<>();

    private PlaylistManager() {
        // do not allow instances of this class
    }

    public static synchronized void addItem(String device, Item item) {
        Playlist pl = PLAYLIST_MAP.get(device);
        if (pl == null) {
            pl = new Playlist(device);
            PLAYLIST_MAP.put(device, pl);
        }
        pl.add(item);
    }

    public static Playlist getPlaylist(String device) {
        return PLAYLIST_MAP.get(device);
    }

    public static Map<String, Playlist> getPlaylists() {
        return Collections.unmodifiableMap(PLAYLIST_MAP);
    }
}
