package org.jcoderz.m3server;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
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
        logger.info("Starting m3server...");

        Configuration config = Config.getConfig();

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