package org.jcoderz.m3server.protocol;

import java.io.IOException;

/**
 * The protocol adapter exception wraps all internal exceptions which cannot be
 * handled.
 *
 * @author mrumpf
 */
public class ProtocolAdapterException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param msg the exception message
     * @param ex the parent exception
     */
    public ProtocolAdapterException(String msg, IOException ex) {
        super(msg, ex);
    }
}
