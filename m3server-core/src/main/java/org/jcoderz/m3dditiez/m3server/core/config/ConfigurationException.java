package org.jcoderz.m3dditiez.m3server.core.config;

/**
 * This exception which gets thrown for any kind of configuration issues. 
 *
 * @author Michael Rumpf
 *
 */
@SuppressWarnings("serial")
public class ConfigurationException extends RuntimeException {

	/**
	 * Message constructor.
	 *
	 * @param msg the exception message
	 */
	public ConfigurationException(String msg) {
		super(msg);
	}

	/**
	 * Original cause constructor.
	 *
	 * @param msg the exception message
	 * @param cause the original cause exception
	 */
	public ConfigurationException(String msg, Exception cause) {
		super(msg, cause);
	}
}
