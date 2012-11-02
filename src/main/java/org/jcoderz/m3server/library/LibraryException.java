package org.jcoderz.m3server.library;

/**
 * The base class for library exceptions.
 *
 * @author mrumpf
 */
public class LibraryException extends Exception {

    public LibraryException(String msg) {
        super(msg);
    }

    public LibraryException(String msg, Exception ex) {
        super(msg, ex);
    }
}
