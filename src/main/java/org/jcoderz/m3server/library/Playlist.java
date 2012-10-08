package org.jcoderz.m3server.library;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class Playlist implements Serializable {

    private List<Mp3Info> mp3 = new ArrayList<Mp3Info>();

    public List<Mp3Info> getMp3() {
        return mp3;
    }

    public void setMp3(List<Mp3Info> mp3) {
        this.mp3 = mp3;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
