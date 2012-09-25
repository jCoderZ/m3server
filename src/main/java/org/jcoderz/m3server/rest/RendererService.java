package org.jcoderz.m3server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jcoderz.m3server.renderer.Renderer;

@Path("/renderer")
public class RendererService {

	@GET
	@Path("/search")
	@Produces("application/json")
	public Response renderers() {
		List<Renderer> r = new ArrayList<Renderer>();
//		Playlist pl = ml.search(term);
//		System.out.println("term=" + term + " -> " + pl);
//		return pl;
		return Response.ok(r.toArray(new Renderer[r.size()])).build();
	}


}
