package org.jcoderz.m3server.protocol;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.jcoderz.m3server.Main;

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
            ResourceConfig rc = new PackagesResourceConfig(getString(HTTP_PACKAGE_RESOURCE_KEY));
            rc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
            URI uri = UriBuilder.fromUri(
                    getString(HTTP_PROTOCOL_KEY) + "://"
                    + getString(HTTP_HOSTNAME_KEY) + "/"
                    + getString(HTTP_REST_SERVICES_ROOT_CONTEXT_KEY)).port(getInteger(HTTP_PORT_KEY)).build();
            logger.log(Level.INFO, "HTTP server: {0}", uri);
            httpServer = GrizzlyServerFactory.createHttpServer(uri, rc);
            httpServer.getServerConfiguration().addHttpHandler(new ClasspathHttpHandler(Main.class), HTTP_STATIC_CONTENT_ROOT_CONTEXT_KEY);
        } catch (IOException ex) {
            throw new ProtocolAdapterException("Failed to start the HTTP server", ex);
        }
    }

    @Override
    public void shutdown() {
        httpServer.stop();
    }

    @Override
    public String getName() {
        return HttpProtocolAdapter.class.getSimpleName();
    }
}
