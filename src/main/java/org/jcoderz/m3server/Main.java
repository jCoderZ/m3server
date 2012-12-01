package org.jcoderz.m3server;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.protocol.ProtocolAdapterRegistry;
import org.jcoderz.m3server.protocol.http.JettyHttpProtocolAdapter;
import org.jcoderz.m3server.protocol.upnp.UpnpProtocolAdapter;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.Logging;

/**
 * This is the main entry point for the m3server.
 *
 * @author mrumpf
 */
public class Main {

    private static final Logger logger = Logging.getLogger(Main.class);

    public static void main(String[] args) throws IOException {

        Configuration config = Config.getConfig();
        String home = config.getString("M3_LIBRARY_HOME");
        if (home == null || home.isEmpty()) {
            logger.severe("M3_LIBRARY_HOME has not been set");
            System.exit(1);
        }

        Library.init(config);

        ProtocolAdapterRegistry.register(JettyHttpProtocolAdapter.class, config);

//        not yet supported
//        Properties airplayProps = new Properties();
//        ProtocolAdapterRegistry.register(AirplayProtocolAdapter.class, config);

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