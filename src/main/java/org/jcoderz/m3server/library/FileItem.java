package org.jcoderz.m3server.library;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author mrumpf
 */
public class FileItem extends AbstractItem {

    private long size;

    public FileItem(Item parent, String name) {
        super(parent, name);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
