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
	 * Returns the name of the adaptor.
	 * 
	 * @return the name of the adaptor
	 */
	String getName();
}
