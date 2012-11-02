package org.jcoderz.m3server.protocol;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.configuration.Configuration;
import org.jcoderz.m3server.util.Logging;

/**
 * The protocol adapter registry manages all available protocol adapters, like
 * <ul> <li>HttpProtocolAdapter</li> <li>AirplayProtocolAdapter</li>
 * <li>UpnpProtocolAdapter</li> </ul>
 *
 * @author mrumpf
 */
public final class ProtocolAdapterRegistry {

    private static final Logger logger = Logging.getLogger(ProtocolAdapterRegistry.class);
    private static Map<Class, ProtocolAdapter> adapters = new HashMap<>();
    private static boolean keepOn = true;

    /**
     * This shutdown hook is called when the process terminates and makes sure
     * that the sleep loop gets interrupted.
     */
    public static class RegistryShutdownHook extends Thread {

        @Override
        public void run() {
            logger.log(Level.INFO, "CTRL-C caught. Shutting down...");
            keepOn = false;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignore) {
            }
        }
    }

    /**
     * Waits until CTRL-C is pressed, the window is closed or the host is
     * shutdown.
     */
    public static void waitForTermination() {
        logger.log(Level.INFO, "Press CTRL-C for shutdown!");
        Runtime.getRuntime().addShutdownHook(new RegistryShutdownHook());
        while (keepOn) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
    }

    /**
     * Registers a protocol adapter by instantiating and initializing it.
     *
     * @param adapter the protocol adapter class
     * @param config the configuration instance
     */
    public static void register(Class adapter, Configuration config) {
        try {
            ProtocolAdapter pa = (ProtocolAdapter) adapter.newInstance();
            pa.init(config);
            adapters.put(adapter, pa);
        } catch (InstantiationException | IllegalAccessException ex) {
            logger.log(Level.SEVERE, "Could not instantiate protocol adapter: " + adapter, ex);
        }
    }

    /**
     * Starts all registered protocol adapters.
     */
    public static void startupAdapters() {
        logger.log(Level.INFO, "Starting {0} adapters", adapters.size());
        for (ProtocolAdapter pa : adapters.values()) {
            logger.log(Level.INFO, "Starting adapter {0}", pa.getName());
            pa.startup();
        }
    }

    /**
     * Stops all registered protocol adapters.
     */
    public static void shutdownAdapters() {
        logger.log(Level.INFO, "Shutting down {0} adapters", adapters.size());
        for (ProtocolAdapter pa : adapters.values()) {
            logger.log(Level.INFO, "Shutting down adapter {0}", pa.getName());
            pa.shutdown();
        }
    }
}
