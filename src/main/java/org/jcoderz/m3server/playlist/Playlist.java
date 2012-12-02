package org.jcoderz.m3server.playlist;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.jcoderz.m3server.library.Item;

/**
 * This class represents a playlist. It can be exported in different formats:
 * M3U, M3UEXT, and PLS.
 *
 * @author mrumpf
 */
public class Playlist {

    public enum PlaylistType {

        M3U, M3UEXT, PLS
    };
    private String name;
    private String baseUrl;
    List<Item> items = new ArrayList<>();

    public Playlist(String name) {
        this.name = name;
        this.baseUrl = "";
    }

    public Playlist(String name, String baseUrl) {
        this.name = name;
        this.baseUrl = baseUrl;
    }

    public void add(Item i) {
        items.add(i);
    }

    public String export(PlaylistType type) {
        StringBuilder sb = new StringBuilder();
        playlistHeader(type, sb);
        int i = 1;
        for (Item item : items) {
            playlistBody(type, sb, item, i);
            i++;
        }
        return sb.toString();
    }

    private void playlistHeader(PlaylistType type, StringBuilder sb) {
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

    private void playlistBody(PlaylistType type, StringBuilder sb, Item item, int i) {
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
