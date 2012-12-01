package org.jcoderz.m3server.renderer;

/**
 * Defines the position info of a renderer when playing media.
 *
 * @author mrumpf
 */
public class Info {

    public enum State {

        PLAYING, STOPPED, PAUSED, TRANSITIONING
    };
    private State state = State.STOPPED;
    private String duration;
    private String uri;
    private String relTime;
    private String absTime;
    private String relCount;
    private String absCount;
    private String track;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
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

    public String getRelCount() {
        return relCount;
    }

    public void setRelCount(String relCount) {
        this.relCount = relCount;
    }

    public String getAbsCount() {
        return absCount;
    }

    public void setAbsCount(String absCount) {
        this.absCount = absCount;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
