package org.jcoderz.m3server.rest;

import java.io.File;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jaudiotagger.tag.datatype.Artwork;
import org.jcoderz.m3server.Item;
import org.jcoderz.m3server.MediaLibrary;
import org.jcoderz.m3server.Playlist;
import org.jcoderz.mp3.intern.util.Environment;

/**
 * This class provides a RESTful service to the MediaLibrary functionality.
 */
@Path("/library")
@RequestScoped
public class LibraryRestService {

	@Inject
	MediaLibrary ml;

	@GET
	@Path("/search")
	@Produces("application/json")
	public Playlist search(@QueryParam("term") String term) {
		Playlist pl = ml.search(term);
		System.out.println("term=" + term + " -> " + pl);
		return pl;
	}

	@GET
	@Path("/browse{path:.*}")
	@Produces("application/json")
	public List<Item> browse(@PathParam("path") String path) {
		System.out.println("path=" + path);
		List<Item> result = ml.browse(path);
		System.out.println("result=" + result);
		return result;
	}

	@GET
	@Path("/browse{file:.*}/content")
	@Produces("audio/mpeg")
	public File file(@PathParam("file") String file) {
		System.out.println("file=" + file);
		File root = new File(Environment.getLibraryHome(), "audio");
		File result = new File(root, file);
		System.out.println("result=" + result);
		return result;
	}
	@GET
	@Path("/browse{file:.*}/cover")
	@Produces("image/jpg")
	public Response cover(@PathParam("file") String file) {
		System.out.println("file=" + file);
		Artwork cover = ml.coverImage(file);
		System.out.println("result=" + cover);
		return Response.ok(cover.getBinaryData(), cover.getMimeType()).build();
	}
}
