package org.jcoderz.m3server.library;

import java.util.logging.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jcoderz.m3server.util.Logging;

/**
 *
 * @author mrumpf
 */
public class FileItem extends AbstractItem {

    private static final Logger logger = Logging.getLogger(FileItem.class);
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
