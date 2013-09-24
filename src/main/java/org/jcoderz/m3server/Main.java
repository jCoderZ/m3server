package org.jcoderz.m3server;

import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.library.Path;
import org.jcoderz.m3server.protocol.ProtocolAdapterRegistry;
import org.jcoderz.m3server.protocol.http.JettyHttpProtocolAdapter;
import org.jcoderz.m3server.protocol.upnp.UpnpProtocolAdapter;
import org.jcoderz.m3server.util.Logging;

/**
 * This is the main entry point for the m3server.
 * 
 * @author mrumpf
 */
public class Main {

	private static final Logger logger = Logging.getLogger(Main.class);

	public static void main(String[] args) throws IOException {
		CompositeConfiguration config = new CompositeConfiguration();
		try {
			config.addConfiguration(new SystemConfiguration());
			config.addConfiguration(new PropertiesConfiguration(
					"m3server.properties"));
		} catch (ConfigurationException ex) {
			throw new LibraryException(
					"Initialization of configuration failed", ex);
		}

		String home = config.getString("M3_LIBRARY_HOME");
		if (home == null || home.isEmpty()) {
			logger.severe("M3_LIBRARY_HOME has not been set");
			System.exit(1);
		}

		Collection<Item> items = Library.LIBRARY.browse(new Path(
				"/AUDIO/m3library/01-gold/A/a-ha/Analogue"));
		System.err.println("items=" + items);

		ProtocolAdapterRegistry
				.register(JettyHttpProtocolAdapter.class, config);

		// not yet supported
		// Properties airplayProps = new Properties();
		// ProtocolAdapterRegistry.register(AirplayProtocolAdapter.class,
		// config);

		Properties upnpProps = new Properties();
		ProtocolAdapterRegistry.register(UpnpProtocolAdapter.class, config);

		logger.info("Starting m3server...");
		ProtocolAdapterRegistry.startupAdapters();
		logger.info("Startup of m3server done!");
		ProtocolAdapterRegistry.waitForTermination();

		logger.info("Shutting down m3server...");
		ProtocolAdapterRegistry.shutdownAdapters();
		logger.info("Shutdown of m3server done! Exiting...");
	}
}