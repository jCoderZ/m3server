package org.jcoderz.m3dditiez.m3server.protocol;

/**
 * 
 * @author Michael Rumpf
 * 
 */
public interface ProtocolAdaptor {

	/**
	 * Starts the adaptor.
	 */
	void start();

	/**
	 * Stops the adaptor.
	 */
	void stop();

	/**
	 * Returns the name of the adaptor.
	 * 
	 * @return the name of the adaptor
	 */
	String getName();
}
