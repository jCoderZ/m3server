package org.jcoderz.m3server.protocol.http;

import com.sun.jersey.api.container.ContainerFactory;
import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.jcoderz.m3server.Main;
import org.jcoderz.m3server.protocol.ProtocolAdapter;
import org.jcoderz.m3server.protocol.ProtocolAdapterException;

/**
 * This is the HTTP adapter that provides the REST services and the jQuery UI.
 *
 * @author mrumpf
 */
public class HttpProtocolAdapter extends ProtocolAdapter {

    private static final Logger logger = Logger.getLogger(HttpProtocolAdapter.class.getName());
    public static final String HTTP_PORT_KEY = "http.port";
    public static final String HTTP_PROTOCOL_KEY = "http.protocol";
    public static final String HTTP_HOSTNAME_KEY = "http.hostname";
    public static final String HTTP_REST_SERVICES_ROOT_CONTEXT_KEY = "http.rest.services.root.context";
    public static final String HTTP_STATIC_CONTENT_ROOT_CONTEXT_KEY = "http.static.content.root.context";
    public static final String HTTP_PACKAGE_RESOURCE_KEY = "http.package.resources";
    private HttpServer httpServer;

    @Override
    public void startup() {
        try {
            httpServer = new HttpServer();
            NetworkListener networkListener = new NetworkListener("sample-listener", getConfiguration().getString(HTTP_HOSTNAME_KEY), getConfiguration().getInt(HTTP_PORT_KEY));
            networkListener.setMaxPendingBytes(5000000);
            httpServer.addListener(networkListener);
            ResourceConfig rc = new PackagesResourceConfig(getConfiguration().getString(HTTP_PACKAGE_RESOURCE_KEY));
            rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);

            httpServer.getServerConfiguration().addHttpHandler(ContainerFactory.createContainer(
                    HttpHandler.class, rc), "/rest");
            httpServer.getServerConfiguration().addHttpHandler(new ClasspathHttpHandler(Main.class), "/");
            httpServer.getServerConfiguration().addHttpHandler(new LibraryHttpHandler(),
                    "/" + getConfiguration().getString(HTTP_STATIC_CONTENT_ROOT_CONTEXT_KEY));
            httpServer.start();
        } catch (Exception ex) {
            throw new ProtocolAdapterException("Failed to start the HTTP server", ex);
        }
    }

    @Override
    public void shutdown() {
        httpServer.stop();
    }
}
