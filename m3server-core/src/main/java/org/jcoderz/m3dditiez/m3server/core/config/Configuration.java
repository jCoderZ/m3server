package org.jcoderz.m3dditiez.m3server.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A singleton for accessing configuration properties.
 *
 * @author Michael Rumpf
 *
 */
//@Singleton
public class Configuration {

	//@Inject
	private Logger log;

	private static final String CONFIG_FILE = "config.properties";

	private Properties p = new Properties();

	/**
	 * Public default constructor.
	 */
	public Configuration() {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			InputStream is = cl.getResourceAsStream(CONFIG_FILE);
			if (is != null) {
				p.load(is);
			} else {
				log.warning("Could not read config file: " + CONFIG_FILE);
			}
		} catch (IOException ex) {
			log.log(Level.SEVERE, "Exception while reading config file + "
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
