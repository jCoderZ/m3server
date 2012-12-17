package org.jcoderz.m3server.library;

/**
 * This class represents an icon for an item.
 *
 * @author mrumpf
 */
public class Icon {

    private byte[] data;
    private String mimetype;

    public Icon(String mimetype, byte[] data) {
        this.mimetype = mimetype;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }
}
