package org.jcoderz.m3server.renderer;

import java.util.Collections;
import java.util.HashMap;

import java.util.Map;
import java.util.logging.Logger;
import org.jcoderz.m3server.util.Logging;

/**
 * The registry that keeps track of all available renderer on the network.
 *
 * @author mrumpf
 */
public final class RendererRegistry {

    private static final Logger logger = Logging.getLogger(RendererRegistry.class);
    private static final Map<String, Renderer> renderers = Collections.synchronizedMap(new HashMap<String, Renderer>());

    private RendererRegistry() {
        // do not allow instances of this class
    }

    /**
     * Returns all currently known renderers on the network which can be used
     * for playing media.
     *
     * @return a list of renderers
     */
    public static Map<String, Renderer> getRenderers() {
        return renderers;
    }

    /**
     * Add a new renderer to the registry.
     *
     * @param renderer the renderer to add
     */
    public static void addRenderer(Renderer renderer) {
        renderers.put(renderer.getName(), renderer);
    }

    /**
     * Find a renderer by its name.
     *
     * @param rendererName the name of the renderer
     * @return the renderer in case it is found or null otherwise
     */
    public static Renderer findRenderer(String rendererName) {
        return renderers.get(rendererName);
    }

    /**
     * Removes a renderer from the internal map.
     *
     * @param renderer the renderer to remove
     */
    public static void removeRenderer(Renderer renderer) {
        Renderer r = renderers.get(renderer);
        renderers.remove(r);
    }

    /**
     * Dumps the renderer map.
     */
    public static void dumpRegistry() {
        logger.info("RendererRegistry: " + renderers);
    }
}
