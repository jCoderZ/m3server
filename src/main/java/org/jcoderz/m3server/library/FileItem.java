package org.jcoderz.m3server.library;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author mrumpf
 */
public class FileItem extends AbstractItem {

    private long size;
    private String url;

    public FileItem(Item parent, String name, String displayName) {
        super(parent, name, displayName);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
