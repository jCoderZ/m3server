package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jcoderz.m3server.renderer.Renderer;
import org.jcoderz.m3server.renderer.RendererRegistry;

@Path("/renderer")
public class RendererService {

    @GET
    @Path("/list")
    @Produces("application/json")
    public Response renderers() {
        System.err.println("renderers called!!!!!!!!!!");
        Map<String, Renderer> r = RendererRegistry.getRenderers();
        
        return Response.ok(r).build();
    }
}
