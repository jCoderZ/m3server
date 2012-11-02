package org.jcoderz.m3server.util;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * This class wraps the Apache Commons Configuration implementation.
 *
 * @author mrumpf
 */
public final class Config {

    // HTTP
    public static final String HTTP_PORT_KEY = "http.port";
    public static final String HTTP_PROTOCOL_KEY = "http.protocol";
    public static final String HTTP_HOSTNAME_KEY = "http.hostname";
    public static final String HTTP_REST_SERVLET_ROOT_CONTEXT_KEY = "http.rest.servlet.root.context";
    public static final String HTTP_REST_ROOT_CONTEXT_KEY = "http.rest.root.context";
    public static final String HTTP_REST_PACKAGE_RESOURCES_KEY = "http.rest.package.resources";

    public static final String HTTP_WEBAPP_PACKAGE_DIR_KEY = "http.wepapp.package.dir";
    public static final String HTTP_WEBAPP_CONTEXT_ROOT_KEY = "http.wepapp.root.context";
    public static final String HTTP_STATIC_PACKAGE_DIR_KEY = "http.static.package.dir";
    public static final String HTTP_STATIC_CONTEXT_ROOT_KEY = "http.static.root.context";

    // UPnP
    public static final String UPNP_URL_ENCODING_KEY = "upnp.url.encoding";
    public static final String UPNP_RENDERER_TYPE_NAME_KEY = "upnp.renderer.type.name";
    public static final String UPNP_SERVER_TYPE_NAME_KEY = "upnp.server.type.name";
    public static final String UPNP_SERVER_NAME_KEY = "upnp.server.name";

    // Library
    public static final String LIBRARY_ROOTS_KEY = "library.roots";

    private static final String M3SERVER_PROPERTIES = "m3server.properties";
    private static CompositeConfiguration CONFIG;

    private Config() {
    }

    private static void init() {
        CONFIG = new CompositeConfiguration();
        try {
            CONFIG.addConfiguration(new SystemConfiguration());
            CONFIG.addConfiguration(new PropertiesConfiguration(M3SERVER_PROPERTIES));
        } catch (ConfigurationException ex) {
            ex.printStackTrace();
            // TODO: Throw runtime exception
        }
    }

    public static synchronized Configuration getConfig() {
        if (CONFIG == null) {
            init();
            if (CONFIG == null) {
                // TODO: Throw runtime exception
            }
        }
        return CONFIG;
    }
}