package org.jcoderz.m3server.library;

/**
 * The base class for library runtime exceptions.
 *
 * @author mrumpf
 */
public class LibraryRuntimeException extends RuntimeException {

    public LibraryRuntimeException(String msg) {
        super(msg);
    }

    public LibraryRuntimeException(String msg, Exception ex) {
        super(msg, ex);
    }
}
