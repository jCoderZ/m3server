package org.jcoderz.m3server.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.util.Logging;

/**
 * This class represents a playlist. It can be exported in different formats:
 * M3U, M3UEXT, and PLS.
 *
 * @author mrumpf
 */
public class Playlist {

    private static final Logger logger = Logging.getLogger(Playlist.class);

    public enum PlaylistType {

        M3U, M3UEXT, PLS /* M3U8 ?? */

    };
    private String name;
    private List<Item> items = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }

    public void add(Item item) {
        logger.log(Level.FINE, "Adding item {0} to device playlist {1}", new Object[]{name, item});
        items.add(item);
    }

    public void remove(Item item) {
        logger.log(Level.FINE, "Removing item {0} from device playlist {1}", new Object[]{name, item});
        items.remove(item);
    }

    public String export(PlaylistType type, String baseUrl) {
        StringBuilder sb = new StringBuilder();
        playlistHeader(baseUrl, type, sb);
        int i = 1;
        for (Item item : items) {
            playlistBody(baseUrl, type, sb, item, i);
            i++;
        }
        return sb.toString();
    }

    private void playlistHeader(String baseUrl, PlaylistType type, StringBuilder sb) {
        switch (type) {
            case M3U: {
                break;
            }
            case M3UEXT: {
                sb.append("#EXTM3U\n");
                break;
            }
            case PLS: {
                sb.append("[playlist]\n");
                sb.append("Version=2");
                sb.append("NumberOfEntries=");
                sb.append(items.size());
                sb.append("\n\n");
                break;
            }
        }
    }

    private void playlistBody(String baseUrl, PlaylistType type, StringBuilder sb, Item item, int i) {
        switch (type) {
            case M3U: {
                sb.append(baseUrl).append(item.getPath());
                sb.append("\n");
                break;
            }
            case M3UEXT: {
                sb.append("#EXTINF:-1,");
                sb.append(item.getName());
                sb.append("\n");
                sb.append(baseUrl).append(item.getPath());
                sb.append("\n");
                break;
            }
            case PLS: {
                sb.append("File");
                sb.append(i);
                sb.append('=');
                sb.append(baseUrl).append(item.getPath());
                sb.append("\n");
                sb.append("Title");
                sb.append(i);
                sb.append('=');
                sb.append(item.getName());
                sb.append("\n");
                sb.append("Length");
                sb.append(i);
                sb.append("=-1");
                sb.append("\n\n");
                break;
            }
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
