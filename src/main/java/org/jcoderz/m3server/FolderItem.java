package org.jcoderz.m3server;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author mrumpf
 */
public class FolderItem extends Item {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
