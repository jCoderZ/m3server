package org.jcoderz.m3server.library.filesystem;

import java.io.File;
import java.util.logging.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.util.Logging;

/**
 * This class represents an audio file.
 *
 * @author mrumpf
 */
public class AudioFileItem extends FileItem {

    private static final Logger logger = Logging.getLogger(AudioFileItem.class);
    private String artist;
    private String title;
    private String album;
    private String genre;
    private String lengthString;
    private File file;
    private long length;
    private long bitrate;

    public AudioFileItem(Item parent, String name) {
        super(parent, name);
    }

    public long getLengthInMilliseconds() {
        return length;
    }

    public void setLengthInMilliseconds(long length) {
        this.length = length;
    }

    public String getLengthString() {
        return lengthString;
    }

    public void setLengthString(String lengthString) {
        this.lengthString = lengthString;
    }

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

    public long getBitrate() {
        return bitrate;
    }

    public void setBitrate(long bitrate) {
        this.bitrate = bitrate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @JsonIgnore
    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
