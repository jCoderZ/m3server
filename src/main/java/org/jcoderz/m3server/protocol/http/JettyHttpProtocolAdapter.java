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
    private static final String PROPERTY_PACKAGES = "com.sun.jersey.config.property.packages";
    private static final String PROPERTY_RESOURCE_CONFIG_CLASS_KEY = "com.sun.jersey.config.property.resourceConfigClass";
    private static final String PROPERTY_RESOURCE_CONFIG_CLASS = "com.sun.jersey.api.core.PackagesResourceConfig";
    private Server server;

    @Override
    public void startup() {
        server = new Server(Config.getConfig().getInt(Config.HTTP_PORT_KEY));
        HandlerList hl = new HandlerList();

        // library
        ContextHandler libraryContext = new ContextHandler();
        libraryContext.setContextPath(Config.getConfig().getString(Config.HTTP_STATIC_CONTEXT_ROOT_KEY));
        libraryContext.setResourceBase(Config.getConfig().getString(Config.HTTP_STATIC_PACKAGE_DIR_KEY));
        libraryContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        libraryContext.setHandler(new LibraryJettyHttpHandler());
        hl.addHandler(libraryContext);

        // webapp
        final URL warUrl = this.getClass().getResource(Config.getConfig().getString(Config.HTTP_WEBAPP_PACKAGE_DIR_KEY));
        final String warUrlString = warUrl.toExternalForm();
        WebAppContext webappContext = new WebAppContext(warUrlString, Config.getConfig().getString(Config.HTTP_WEBAPP_CONTEXT_ROOT_KEY));
        hl.addHandler(webappContext);

        // rest
        ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        servletHolder.setInitParameter(PROPERTY_PACKAGES, Config.getConfig().getString(Config.HTTP_REST_PACKAGE_RESOURCES_KEY));
        servletHolder.setInitParameter(PROPERTY_RESOURCE_CONFIG_CLASS_KEY, PROPERTY_RESOURCE_CONFIG_CLASS);
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, Config.getConfig().getString(Config.HTTP_REST_SERVLET_ROOT_CONTEXT_KEY));
        servletContextHandler.addServlet(servletHolder, Config.getConfig().getString(Config.HTTP_REST_ROOT_CONTEXT_KEY));
        hl.addHandler(servletContextHandler);

        server.setHandler(hl);
        try {
            server.start();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            // TODO: throw RuntimeException
        }
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
            server.join();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            // TODO: throw RuntimeException
        }
    }
}
