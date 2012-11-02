package org.jcoderz.m3server.protocol.http.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jaudiotagger.tag.datatype.Artwork;
import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.library.MediaLibrary;
import org.jcoderz.m3server.library.Playlist;

/**
 * This class provides a RESTful service to the Library functionality.
 *
 * @mrumpf
 */
@Path("/library")
public class LibraryService {

    private static final Logger logger = Logger.getLogger(LibraryService.class.getName());

    @GET
    @Path("/search")
    @Produces("application/json")
    public Playlist search(@QueryParam("term") String term) {
        Playlist pl = MediaLibrary.getMediaLibrary().search(term);
        System.out.println("term=" + term + " -> " + pl);
        return pl;
    }

    @GET
    @Path("/browse{path:.*}")
    public Response browse(@PathParam("path") String path) {
        try {
            Item result = Library.getPath(path);
            String mt = "application/json";
            if (result instanceof FileItem && result.getName().toLowerCase().endsWith(".mp3")) {
                mt = "audio/mpeg";
            }
            System.err.println("path: " + path + ", mt: " + mt);
            Response resp = Response.ok(result, mt).build();
            System.err.println("r: " + resp);
            return resp;
        } catch (LibraryException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/browse{path:.*}/cover")
    public Response cover(@PathParam("path") String path) {
        Artwork c = MediaLibrary.getMediaLibrary().coverImage(path);
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
