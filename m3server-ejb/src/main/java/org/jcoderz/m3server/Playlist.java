package org.jcoderz.m3server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class Playlist implements Serializable {
	private List<Mp3Info> mp3 = new ArrayList<Mp3Info>();

	public List<Mp3Info> getMp3() {
		return mp3;
	}

	public void setMp3(List<Mp3Info> mp3) {
		this.mp3 = mp3;
	}
}
