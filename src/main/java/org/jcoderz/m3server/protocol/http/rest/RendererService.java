package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.jcoderz.m3server.renderer.Info;

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
    @POST
    @Path("/{renderer}/playpath{path:.*}")
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
    @POST
    @Path("/{renderer}/play")
    public void play(@PathParam("renderer") String renderer) {
        Renderer r = findRenderer(renderer);
        if (r != null) {
            r.play();
        }
    }

    /**
     * Gets the position info of the server.
     *
     * @param renderer the renderer on which to play the file
     * @return the position info
     */
    @GET
    @Path("/{renderer}/info")
    @Produces("application/json")
    public Info info(@PathParam("renderer") String renderer) {
        Info position = null;
        Renderer r = findRenderer(renderer);
        if (r != null) {
            position = r.info();
        }
        return position;
    }

    /**
     * Gets the volume of the renderer.
     *
     * @param renderer the renderer from which to get the volume
     * @return the volume level
     */
    @GET
    @Path("/{renderer}/volume")
    @Produces("application/json")
    public long volume(@PathParam("renderer") String renderer) {
        long level = 0L;
        Renderer r = findRenderer(renderer);
        if (r != null) {
            level = r.volume();
        }
        return level;
    }

    /**
     * Sets the volume of the renderer.
     *
     * @param renderer the renderer on which to change the volume
     */
    @POST
    @Path("/{renderer}/volume/{level}")
    public void volume(@PathParam("renderer") String renderer, @PathParam("level") long level) {
        Renderer r = findRenderer(renderer);
        if (r != null) {
            r.volume(level);
        }
    }

    /**
     * Stops playback on the renderer.
     *
     * @param renderer the renderer on which to stop playback
     */
    @POST
    @Path("/{renderer}/stop")
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
    @POST
    @Path("/{renderer}/pause")
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
