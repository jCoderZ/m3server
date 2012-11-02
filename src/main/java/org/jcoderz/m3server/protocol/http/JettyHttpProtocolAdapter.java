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

/**
 * This is the HTTP adapter that provides the REST services and the jQuery UI.
 *
 * @author mrumpf
 */
public class JettyHttpProtocolAdapter extends ProtocolAdapter {

    private static final Logger logger = Logger.getLogger(JettyHttpProtocolAdapter.class.getName());
    public static final String HTTP_PORT_KEY = "http.port";
    public static final String HTTP_PROTOCOL_KEY = "http.protocol";
    public static final String HTTP_HOSTNAME_KEY = "http.hostname";
    public static final String HTTP_REST_SERVICES_ROOT_CONTEXT_KEY = "http.rest.services.root.context";
    public static final String HTTP_STATIC_CONTENT_ROOT_CONTEXT_KEY = "http.static.content.root.context";
    public static final String HTTP_PACKAGE_RESOURCE_KEY = "http.package.resources";
    private Server server;

    @Override
    public void startup() {
        server = new Server(8080);
        HandlerList hl = new HandlerList();

        // library
        ContextHandler libraryContext = new ContextHandler();
        libraryContext.setContextPath("/m3server/library");
        libraryContext.setResourceBase(".");
        libraryContext.setClassLoader(Thread.currentThread().getContextClassLoader());
        libraryContext.setHandler(new LibraryJettyHttpHandler());
        hl.addHandler(libraryContext);

        // webapp
        final String WEBAPPDIR = "/org/jcoderz/m3server/ui";
        final String CONTEXTPATH = "/m3server/ui";
        final URL warUrl = this.getClass().getResource(WEBAPPDIR);
        final String warUrlString = warUrl.toExternalForm();
        WebAppContext webappContext = new WebAppContext(warUrlString, CONTEXTPATH);
        hl.addHandler(webappContext);

        // rest
        ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        servletHolder.setInitParameter("com.sun.jersey.config.property.packages", "org.jcoderz.m3server");
        servletHolder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        ServletContextHandler servletContextHandler = new ServletContextHandler(server, "/m3server");
        servletContextHandler.addServlet(servletHolder, "/rest/*");
        hl.addHandler(servletContextHandler);

        server.setHandler(hl);
        try {
            server.start();
        } catch (Exception ex) {
            Logger.getLogger(JettyHttpProtocolAdapter.class.getName()).log(Level.SEVERE, null, ex);
            // TODO: throw RuntimeException
        }
    }

    @Override
    public void shutdown() {
        try {
            server.stop();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(JettyHttpProtocolAdapter.class.getName()).log(Level.SEVERE, null, ex);
            // TODO: throw RuntimeException
        }
    }
}
