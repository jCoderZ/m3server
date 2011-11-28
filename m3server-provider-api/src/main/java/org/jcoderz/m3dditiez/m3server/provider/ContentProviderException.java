package org.jcoderz.m3dditiez.m3server.provider;

/**
 * This exception which gets thrown for any kind of content provider issues. 
 *
 * @author Michael Rumpf
 *
 */
@SuppressWarnings("serial")
public class ContentProviderException extends RuntimeException {

	/**
	 * Message constructor.
	 *
	 * @param msg the exception message
	 */
	public ContentProviderException(String msg) {
		super(msg);
	}

	/**
	 * Original cause constructor.
	 *
	 * @param msg the exception message
	 * @param cause the original cause exception
	 */
	public ContentProviderException(String msg, Exception ex) {
		super(msg, ex);
	}
}
