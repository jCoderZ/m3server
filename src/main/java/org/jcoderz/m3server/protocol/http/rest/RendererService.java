package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;
import java.util.logging.Level;
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

    @GET
    @Path("/list")
    @Produces("application/json")
    public Response renderers() {
        Map<String, Renderer> r = RendererRegistry.getRenderers();

        return Response.ok(r).build();
    }

    @GET
    @Path("/play/{renderer}/{path:.*}")
    @Consumes("application/json")
    @Produces("application/json")
    public Response play(@PathParam("renderer") String renderer, @PathParam("path") String path) {
        logger.log(Level.INFO, "renderer={0}, path={0}", new Object[]{renderer, path});
        Map<String, Renderer> renderers = RendererRegistry.getRenderers();
        for (String rendererName : renderers.keySet()) {
            logger.log(Level.INFO, "r={0}", rendererName);
            if (rendererName.toLowerCase().equals(renderer.toLowerCase())) {
                Renderer ren = renderers.get(renderer);
                logger.log(Level.INFO, "Playing on {0}: {1}", new Object[]{ren, path});
                ren.play("/" + path);
            }
        }

        return Response.ok().build();
    }
}
