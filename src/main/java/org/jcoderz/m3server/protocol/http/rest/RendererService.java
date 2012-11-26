package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jcoderz.m3server.renderer.Renderer;
import org.jcoderz.m3server.renderer.RendererRegistry;
import org.jcoderz.m3server.util.Logging;

/**
 * This class provides a RESTful service to the renderer functionality.
 *
 * @author mrumpf
 */
@Path("/renderer")
public class RendererService {

    private static final Logger logger = Logging.getLogger(RendererService.class);

    /**
     * Lists all renderers.
     *
     * @return a list of renderers
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    public Response list() {
        Map<String, Renderer> r = RendererRegistry.getRenderers();
        return Response.ok(r).build();
    }

    /**
     * Plays the file specified by the path.
     *
     * @param renderer the renderer on which to play the file
     * @param path the path that identifies a file
     */
    @GET
    @Path("/{renderer}/play{path:.*}")
    @Consumes("application/json")
    public void play(@PathParam("renderer") String renderer, @PathParam("path") String path) {
        Renderer r = findRenderer(renderer);
        if (r != null) {
            r.play(path);
        }
    }

    /**
     * Plays any stored path.
     *
     * @param renderer the renderer on which to play the file
     * @param path the path that identifies a file
     */
    @GET
    @Path("/{renderer}/play")
    @Consumes("application/json")
    public void play(@PathParam("renderer") String renderer) {
        Renderer r = findRenderer(renderer);
        if (r != null) {
            r.play();
        }
    }

    /**
     * Stops playback on the renderer.
     *
     * @param renderer the renderer on which to stop playback
     */
    @GET
    @Path("/{renderer}/stop")
    @Consumes("application/json")
    public void stop(@PathParam("renderer") String renderer) {
        Renderer r = findRenderer(renderer);
        if (r != null) {
            r.stop();
        }
    }

    /**
     * Pauses playback on the renderer.
     *
     * @param renderer the renderer on which to pause playback
     */
    @GET
    @Path("/{renderer}/pause")
    @Consumes("application/json")
    public void pause(@PathParam("renderer") String renderer) {
        Renderer r = findRenderer(renderer);
        if (r != null) {
            r.pause();
        }
    }

    private Renderer findRenderer(String renderer) {
        Map<String, Renderer> renderers = RendererRegistry.getRenderers();
        for (String rendererName : renderers.keySet()) {
            if (rendererName.toLowerCase().equals(renderer.toLowerCase())) {
                return renderers.get(renderer);
            }
        }
        return null;
    }
}
