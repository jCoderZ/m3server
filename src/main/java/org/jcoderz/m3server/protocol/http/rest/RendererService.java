package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
    @Path("/play")
    @Consumes("application/json")
    @Produces("application/json")
    public Response play(String path) {
        Map<String, Renderer> renderers = RendererRegistry.getRenderers();
        for (String renderer : renderers.keySet()) {
            logger.info("renderer=" + renderer);
            if (renderer.toLowerCase().indexOf("bravia") != -1) {
                Renderer r = renderers.get(renderer);
                r.play("/audio/filesystem/01-gold/A/a-ha/Lifelines/15 - Solace.mp3");
            }
        }

        return Response.ok().build();
    }
}
