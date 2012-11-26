package org.jcoderz.m3server.renderer;

/**
 * Defines the position info of a renderer when playing media.
 *
 * @author mrumpf
 */
public class Position {

    private String duration;
    private String uri;
    private String relTime;
    private String absTime;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRelTime() {
        return relTime;
    }

    public void setRelTime(String relTime) {
        this.relTime = relTime;
    }

    public String getAbsTime() {
        return absTime;
    }

    public void setAbsTime(String absTime) {
        this.absTime = absTime;
    }
}
