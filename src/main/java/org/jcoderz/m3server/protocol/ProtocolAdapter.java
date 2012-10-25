package org.jcoderz.m3server.protocol;

import org.apache.commons.configuration.Configuration;

/**
 * The base class for all protocol adapters.
 *
 * @author mrumpf
 */
public abstract class ProtocolAdapter {

    private Configuration config;

    /**
     * Initialize the protocol adapter instance.
     *
     * @param config the configuration instance
     */
    public void init(Configuration config) {
        this.config = config;
    }

    /**
     * Returns the configuration.
     *
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return config;
    }

    /**
     * Returns the name of the protocol adapter.
     *
     * @return the name of the protocol adapter
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * Start the protocol adapter.
     */
    public abstract void startup();

    /**
     * Shutdown the protocol adapter.
     */
    public abstract void shutdown();
}
