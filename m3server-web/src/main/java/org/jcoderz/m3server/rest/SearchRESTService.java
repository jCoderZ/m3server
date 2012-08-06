package org.jcoderz.m3server.rest;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.jcoderz.m3server.model.Mp3Info;

/**
 * JAX-RS Example
 * 
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@Path("/search")
@RequestScoped
public class SearchRESTService {

	@GET
	@Path("/{key}")
	@Produces("application/json")
	public Mp3Info lookupMemberById(@PathParam("key") String key) {
		System.out.println("key=" + key);
		Mp3Info m3i = new Mp3Info();
		m3i.setAlbum("Under the blood red sky");
		m3i.setArtist("U2");
		m3i.setReleased("1999");
		m3i.setSize(1000000L);
		m3i.setTitle("Sundy bloody sunday");
		return m3i;
	}
}
