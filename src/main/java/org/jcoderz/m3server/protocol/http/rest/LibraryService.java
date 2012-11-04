package org.jcoderz.m3server.protocol.http.rest;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.FolderItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.util.Logging;

/**
 * This class provides a RESTful service to the Library functionality.
 *
 * @mrumpf
 */
@Path("/library")
@Consumes("application/json")
@Produces("application/json")
public class LibraryService {

    private static final Logger logger = Logging.getLogger(LibraryService.class);

    @GET
    @Path("/search")
    @Consumes("application/json")
    @Produces("application/json")
    public List<Item> search(@QueryParam("term") String term) {
        try {
            return Library.search(term);
        } catch (LibraryException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
    }

    @GET
    @Path("/browse{path:.*}")
    @Consumes("application/json")
    public Response browse(@PathParam("path") String path) {
        Response resp = null;
        try {
            Item item = Library.browse(path);
            if (item instanceof AudioFileItem) {/* && result.getName().toLowerCase().endsWith(".mp3")) {*/
                AudioFileItem fi = (AudioFileItem) item;
                File f = fi.getFile();
                resp = Response.ok(f, "audio/mpeg").header("Content-Length", "" + f.length()).build();
            } else if (item instanceof FolderItem) {
                resp = Response.ok(item, MediaType.APPLICATION_JSON_TYPE).build();
            } else {
            }
        } catch (LibraryException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
        return resp;
    }
    /*
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
     */

    @GET
    @Path("/browse{path:.*}/info")
    public Response info(@PathParam("path") String path, @QueryParam("tag") String tag) {
        // TODO: tag=all -> return MusicBrainzMetaData
        return null;
    }
}
