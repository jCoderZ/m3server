package org.jcoderz.m3server.library;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author mrumpf
 */
public class Item {

    private MimeType mimetype;
    private String path;
    private String name;
    private String icon;
    private String creator;

    public Item() {
    }

    public Item(String path, String name, String icon, String creator) {
        this.path = path;
        this.name = name;
        this.icon = icon;
        this.creator = creator;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public MimeType getMimetype() {
        return mimetype;
    }

    public void setMimetype(MimeType mimetype) {
        this.mimetype = mimetype;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}