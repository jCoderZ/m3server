package org.jcoderz.m3server.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.jcoderz.m3server.MediaLibrary;
import org.jcoderz.m3server.Playlist;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@RequestScoped
@Path("/browser")
public class BrowserRESTService {

	@Inject
	MediaLibrary ml;

	@GET
	@Path("/browse")
	@Produces("application/json")
	public Playlist search(@QueryParam("path") String path) {
		System.out.println("path=" + path + " -> " + "RESULT");
		return null;
	}
}
