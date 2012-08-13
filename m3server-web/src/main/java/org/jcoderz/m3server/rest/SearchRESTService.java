package org.jcoderz.m3server.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jcoderz.m3server.MediaLibrary;
import org.jcoderz.m3server.Mp3Info;
import org.jcoderz.m3server.Playlist;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@Path("/search")
@RequestScoped
public class SearchRESTService {

	@Inject
	MediaLibrary ml;

	@GET
	@Path("/key/{key}")
	@Produces("application/json")
	public Playlist search(@PathParam("key") String key) {
		System.out.println("key=" + key);
		Playlist pl = ml.search(key);
		return pl;
	}
}
