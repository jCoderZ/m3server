package org.jcoderz.m3dditiez.m3server.core.config;


/**
 * A singleton for accessing configuration properties.
 *
 * @author Michael Rumpf
 *
 */
public interface Configuration {

	/**
	 * Returns a string from a configuration value.
	 *
	 * @param caller the caller class which is used as part of the key.
	 * @param key the key suffix
	 * @return the value as string
	 */
	String getString(Class<?> caller, String key);

	/**
	 * Returns an integer from a configuration value.
	 *
	 * @param caller the caller class which is used as part of the key.
	 * @param key the key suffix
	 * @return the value as integer
	 */
	Integer getInteger(Class<?> caller, String key);
}
