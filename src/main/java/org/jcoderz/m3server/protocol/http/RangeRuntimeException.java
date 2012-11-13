package org.jcoderz.m3server.protocol.http;

/**
 *
 * @author mrumpf
 */
public class RangeRuntimeException extends RuntimeException {

    public RangeRuntimeException(String message) {
        super(message);
    }

    public RangeRuntimeException(Throwable cause) {
        super(cause);
    }

    public RangeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
