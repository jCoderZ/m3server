package org.jcoderz.m3server.protocol.http;

import com.sun.jersey.spi.container.servlet.ServletContainer;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jcoderz.m3server.protocol.ProtocolAdapter;
import org.jcoderz.m3server.protocol.ProtocolAdapterRuntimeException;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.Logging;

/**
 * This is the HTTP adapter that provides multiple endpoints. <p>Currently the
 * following endpoints are available:</p> <ul> <li>REST services via the
 * ServletContextHandler</li> <li>jQuery UI via the WebAppContext</li>
 * <li>Static resources via the ContextHandler</li> </ul>
 *
 * @author mrumpf
 */
public class JettyHttpProtocolAdapter extends ProtocolAdapter {

    private static final Logger logger = Logging.getLogger(JettyHttpProtocolAdapter.class);
    /** The init parameter name for the Java packages list. */
    private static final String PROPERTY_PACKAGES = "com.sun.jersey.config.property.packages";
    /** The init parameter name for Jersey package resource configs. */
    private static final String PROPERTY_RESOURCE_CONFIG_CLASS_KEY = "com.sun.jersey.config.property.resourceConfigClass";
    /** The Jersey package resource config class. */
    private static final String PROPERTY_RESOURCE_CONFIG_CLASS = "com.sun.jersey.api.core.PackagesResourceConfig";
    /** The init parameter name for response filters. */
    private static final String PROPERTY_RESPONSE_FILTER_CLASS_KEY = "com.sun.jersey.spi.container.ContainerResponseFilters";
    /** The standard Jersey logging filter. */
    private static final String PROPERTY_RESPONSE_FILTER_CLASS = "com.sun.jersey.api.container.filter.LoggingFilter";
    /** Enable JSON POJO mapping. */
    private static final String PROPERTY_JSON_POJO_MAPPING_FEATURE = "com.sun.jersey.api.json.POJOMappingFeature";
    private Server server;

    @Override
    public void startup() {
        int port = Config.getConfig().getInt(Config.HTTP_PORT_KEY);
        server = new Server(port);
        logger.log(Level.CONFIG, "Jetty HTTP server port: {0}", port);
        HandlerList hl = new HandlerList();

        // library
        ContextHandler libraryContext = new ContextHandler();
        final String libraryContextPath = Config.getConfig().getString(Config.HTTP_STATIC_CONTEXT_ROOT_KEY);
        logger.log(Level.CONFIG, "library context path: {0}", libraryContextPath);
        libraryContext.setContextPath(libraryContextPath);
        final String libraryResourcePackage = Config.getConfig().getString(Config.HTTP_STATIC_PACKAGE_DIR_KEY);
        logger.log(Level.CONFIG, "library resource package: {0}", libraryResourcePackage);
        libraryContext.setResourceBase(libraryResourcePackage);
        libraryContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        libraryContext.setHandler(new LibraryJettyHttpHandler());
        hl.addHandler(libraryContext);

        // webapp
        final String webappResourcePackage = Config.getConfig().getString(Config.HTTP_WEBAPP_PACKAGE_DIR_KEY);
        logger.log(Level.CONFIG, "webapp resource package: {0}", webappResourcePackage);
        final URL warUrl = this.getClass().getResource(webappResourcePackage);
        final String warUrlString = warUrl.toExternalForm();
        final String webappContextPath = Config.getConfig().getString(Config.HTTP_WEBAPP_CONTEXT_ROOT_KEY);
        logger.log(Level.CONFIG, "webapp context path: {0}", webappContextPath);
        WebAppContext webappContext = new WebAppContext(warUrlString, webappContextPath);
        hl.addHandler(webappContext);

        // rest
        ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        final String restResourcePackage = Config.getConfig().getString(Config.HTTP_REST_PACKAGE_RESOURCES_KEY);
        logger.log(Level.CONFIG, "rest resource package: {0}", restResourcePackage);
        servletHolder.setInitParameter(PROPERTY_PACKAGES, restResourcePackage);
        servletHolder.setInitParameter(PROPERTY_RESOURCE_CONFIG_CLASS_KEY, PROPERTY_RESOURCE_CONFIG_CLASS);
        servletHolder.setInitParameter(PROPERTY_RESPONSE_FILTER_CLASS_KEY, PROPERTY_RESPONSE_FILTER_CLASS);
        servletHolder.setInitParameter(PROPERTY_JSON_POJO_MAPPING_FEATURE, "" + true);
        logger.log(Level.CONFIG, "rest servlet init parameters: {0}", servletHolder.getInitParameters());
        final String restRootContextPath = Config.getConfig().getString(Config.HTTP_REST_SERVLET_ROOT_CONTEXT_KEY);
        final String restContextPath = Config.getConfig().getString(Config.HTTP_REST_ROOT_CONTEXT_KEY);
        logger.log(Level.CONFIG, "rest context path: {0}{1}", new Object[] {restRootContextPath, restContextPath});
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, restRootContextPath);
        servletContextHandler.addServlet(servletHolder, restContextPath);
        hl.addHandler(servletContextHandler);

        server.setHandler(hl);
        try {
            server.start();
        } catch (Exception ex) {
            final String msg = "Could not start Jetty HTTP server";
            logger.log(Level.SEVERE, msg, ex);
            throw new ProtocolAdapterRuntimeException(msg, ex);
        }
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
            server.join();
        } catch (Exception ex) {
            final String msg = "Could not stop Jetty HTTP server";
            logger.log(Level.SEVERE, msg, ex);
            throw new ProtocolAdapterRuntimeException(msg, ex);
        }
    }
}
