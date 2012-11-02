package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jcoderz.m3server.renderer.Renderer;
import org.jcoderz.m3server.renderer.RendererRegistry;
import org.jcoderz.m3server.util.Logging;

/**
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
        System.err.println("renderers called!!!!!!!!!!");
        Map<String, Renderer> r = RendererRegistry.getRenderers();
        
        return Response.ok(r).build();
    }
}
