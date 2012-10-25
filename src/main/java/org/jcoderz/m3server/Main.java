package org.jcoderz.m3server;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.jcoderz.m3server.protocol.http.HttpProtocolAdapter;
import org.jcoderz.m3server.protocol.ProtocolAdapterRegistry;
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

        Properties httpProps = new Properties();
        httpProps.put(HttpProtocolAdapter.HTTP_PORT_KEY, 8080);
        // Bug in Grizzly? Only one folder possible "m3server/rest" does not work!
        httpProps.put(HttpProtocolAdapter.HTTP_REST_SERVICES_ROOT_CONTEXT_KEY, "rest");
        httpProps.put(HttpProtocolAdapter.HTTP_PROTOCOL_KEY, "http");
        httpProps.put(HttpProtocolAdapter.HTTP_HOSTNAME_KEY, "localhost");
        httpProps.put(HttpProtocolAdapter.HTTP_PACKAGE_RESOURCE_KEY, "org.jcoderz.m3server.rest");
        ProtocolAdapterRegistry.register(HttpProtocolAdapter.class, httpProps);

//        not yet supported
//        Properties airplayProps = new Properties();
//        ProtocolAdapterRegistry.register(AirplayProtocolAdapter.class, airplayProps);

        Properties upnpProps = new Properties();
        ProtocolAdapterRegistry.register(UpnpProtocolAdapter.class, upnpProps);

        ProtocolAdapterRegistry.startupAdapters();
        ProtocolAdapterRegistry.waitForTermination();
        ProtocolAdapterRegistry.shutdownAdapters();
    }
}