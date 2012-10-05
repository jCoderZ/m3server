package org.jcoderz.m3server;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author mrumpf
 */
public class AudioFileItem extends FileItem {

    private String artist;
    private String title;
    private String album;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
