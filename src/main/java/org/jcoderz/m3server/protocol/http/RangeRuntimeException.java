package org.jcoderz.m3server.protocol.http;

/**
 * This exception is thrown when range definitions are invalid.
 *
 * @author mrumpf
 */
public class RangeRuntimeException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message the exception message
     */
    public RangeRuntimeException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param cause the cause of the exception
     */
    public RangeRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     *
     * @param message the exception message
     * @param cause the cause of the exception
     */
    public RangeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
