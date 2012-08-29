package org.jcoderz.m3server.rest;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.jaudiotagger.tag.datatype.Artwork;
import org.jcoderz.m3server.MediaLibrary;
import org.jcoderz.m3server.Playlist;

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
	public Response browse(@PathParam("path") String path) {
		Object result = ml.browse(path);
		String mt = "application/json";
		if (result instanceof File) {
			mt = "audio/mpeg3";
		}
		System.err.println("path: " + path + ", mt: " + mt);
		return 	Response.ok(result, mt).build();
	}

	@GET
	@Path("/browse{path:.*}/cover")
	public Response cover(@PathParam("path") String path) {
		Artwork c = ml.coverImage(path);
		// Workaround for wrong mime-type: Chrome is reporting: "Resource interpreted as Image but transferred with MIME type image/jpg"
		String mt = c.getMimeType();
		if ("image/jpg".equals(mt)) {
			mt = "image/jpeg";
		}
		return Response.ok(c.getBinaryData(), mt).build();
	}

	@GET
	@Path("/browse{path:.*}/info")
	public Response info(@PathParam("path") String path, @QueryParam("tag") String tag) {
		System.err.println("info: " + path + ", tag: " + tag);
		// TODO: tag=all -> return MusicBrainzMetaData
		return null;
	}
}
