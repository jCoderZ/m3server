package org.jcoderz.m3server;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;
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
        logger.info("Starting m3server...");

        CompositeConfiguration config = new CompositeConfiguration();
        try {
            config.addConfiguration(new SystemConfiguration());
            config.addConfiguration(new PropertiesConfiguration("m3server.properties"));
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
            // TODO: Throw runtime exception
        }

        ProtocolAdapterRegistry.register(JettyHttpProtocolAdapter.class, config);

//        not yet supported
//        Properties airplayProps = new Properties();
//        ProtocolAdapterRegistry.register(AirplayProtocolAdapter.class, config);

        Properties upnpProps = new Properties();
        ProtocolAdapterRegistry.register(UpnpProtocolAdapter.class, config);

        ProtocolAdapterRegistry.startupAdapters();
        ProtocolAdapterRegistry.waitForTermination();
        ProtocolAdapterRegistry.shutdownAdapters();
    }
}