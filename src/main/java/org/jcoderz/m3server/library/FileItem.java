package org.jcoderz.m3server.library;

import java.util.logging.Logger;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jcoderz.m3server.util.Logging;

/**
 * This class represents a common file.
 *
 * @author mrumpf
 */
public class FileItem extends AbstractItem {

    private static final Logger logger = Logging.getLogger(FileItem.class);
    private long size;

    /**
     * Constructor.
     *
     * @param parent the parent item
     * @param name the name of the item
     */
    public FileItem(Item parent, String name) {
        super(parent, name);
    }

    /**
     * Returns the size of the file item.
     *
     * @return the size of the file item
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets the file size.
     *
     * @param size the file size
     */
    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
