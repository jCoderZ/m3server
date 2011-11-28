package org.jcoderz.m3dditiez.m3server.protocol;

/**
 * This exception which gets thrown for any kind of protocol adapter issues. 
 *
 * @author Michael Rumpf
 *
 */
@SuppressWarnings("serial")
public class ProtocolAdaptorException extends RuntimeException {

	/**
	 * Message constructor.
	 *
	 * @param msg the exception message
	 */
	public ProtocolAdaptorException(String msg) {
		super(msg);
	}

	/**
	 * Original cause constructor.
	 *
	 * @param msg the exception message
	 * @param cause the original cause exception
	 */
	public ProtocolAdaptorException(String msg, Exception ex) {
		super(msg, ex);
	}
}
