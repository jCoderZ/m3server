package org.jcoderz.m3server;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.protocol.HttpProtocolAdapter;
import org.jcoderz.m3server.protocol.ProtocolAdapterRegistry;
import org.jcoderz.m3server.protocol.UpnpProtocolAdapter;

/**
 * This is the main entry point for the m3server.
 *
 * @author mrumpf
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("java.util.logging.config.file=" + System.getenv("java.util.logging.config.file"));
        Properties httpProps = new Properties();
        httpProps.put(HttpProtocolAdapter.HTTP_PORT_KEY, 8080);
        // Bug in Grizzly? Only one folder 
        httpProps.put(HttpProtocolAdapter.HTTP_REST_SERVICES_ROOT_CONTEXT_KEY, "rest");
        httpProps.put(HttpProtocolAdapter.HTTP_PROTOCOL_KEY, "http");
        httpProps.put(HttpProtocolAdapter.HTTP_HOSTNAME_KEY, "localhost");
        httpProps.put(HttpProtocolAdapter.HTTP_PACKAGE_RESOURCE_KEY, "org.jcoderz.m3server.rest");
        //httpProps.put(HttpProtocolAdapter.HTTP_STATIC_CONTENT_ROOT_CONTEXT_KEY, "/ui");
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