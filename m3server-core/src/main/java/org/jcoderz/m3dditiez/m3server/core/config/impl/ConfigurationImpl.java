package org.jcoderz.m3dditiez.m3server.core.config.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jcoderz.m3dditiez.m3server.core.config.Configuration;
import org.jcoderz.m3dditiez.m3server.core.config.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A singleton for accessing configuration properties.
 *
 * @author Michael Rumpf
 *
 */
//@Singleton
public class ConfigurationImpl implements Configuration {

	private static final Logger log = LoggerFactory.getLogger(ConfigurationImpl.class);

	private static final String CONFIG_FILE = "config.properties";

	private Properties p = new Properties();

	/**
	 * Public default constructor.
	 */
	public ConfigurationImpl() {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream is = cl.getResourceAsStream(CONFIG_FILE);
			if (is != null) {
				p.load(is);
			} else {
				log.warn("Could not read config file: " + CONFIG_FILE);
			}
		} catch (IOException ex) {
			log.error("Exception while reading config file + "
					+ CONFIG_FILE, ex);
			throw new ConfigurationException(
					"Exception while reading config file + " + CONFIG_FILE, ex);
		}
	}

	/**
	 * Returns a string from a configuration value.
	 *
	 * @param caller the caller class which is used as part of the key.
	 * @param key the key suffix
	 * @return the value as string
	 */
	public String getString(Class<?> caller, String key) {
		return p.getProperty(caller.getName() + "." + key);
	}

	/**
	 * Returns an integer from a configuration value.
	 *
	 * @param caller the caller class which is used as part of the key.
	 * @param key the key suffix
	 * @return the value as integer
	 */
	public Integer getInteger(Class<?> caller, String key) {
		return Integer.valueOf(p.getProperty(caller.getName() + "." + key));
	}

//	@Override
//	public String toString() {
//		return ToStringBuilder.reflectionToString(this);
//	}
}
