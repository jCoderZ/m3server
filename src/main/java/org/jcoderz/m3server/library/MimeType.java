package org.jcoderz.m3server.library;

/**
 *
 * @author mrumpf
 */
public enum MimeType {

    FOLDER("inode/folder"), MP3("audio/mpeg");
    private String type;
    private String subtype;

    MimeType(String mimeType) {
        int idx = mimeType.indexOf('/');
        type = mimeType.substring(0, idx - 1);
        subtype = mimeType.substring(idx);
    }
    
    public String getSubtype() {
        return subtype;
    }

    public String getType() {
        return type;
    }
}
