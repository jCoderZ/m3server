package org.jcoderz.m3server.protocol.http.rest;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
    /*
     @HEAD
     @Path("/browse{path:.*}")
     @Consumes("application/json")
     public Response browseHead(@PathParam("path") String path) {
     Response resp = null;
     try {
     Item item = Library.browse(path);
     if (AudioFileItem.class.isAssignableFrom(item.getClass())) {// && result.getName().toLowerCase().endsWith(".mp3")) {
     AudioFileItem fi = (AudioFileItem) item;
     File f = fi.getFile();
     // header("transferMode.dlna.org", "Streaming").
     // Bit 24 - Streaming Mode Flag
     // Bit 22 - Background Mode Flag
     // Bit 21 - HTTP Connection Stalling Flag
     // Bit 20 - DLNA v1.5 versioning flag
     // 01700000 00000000 00000000 00000000
     resp = Response.ok().header("Server", "Linux, UPnP/1.0 DLNADOC/1.50, Serviio/1.0.1").header("Content-Type", "audio/mpeg").header("transferMode.dlna.org", "Streaming").header("contentFeatures.dlna.org", "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=01500000000000000000000000000000").header("Cache-control", "no-cache").header("Date", "Sun, 11 Nov 2012 22:39:59 GMT").build();
     //resp = Response.ok(f, "audio/mpeg").header("Content-Length", "" + f.length()).build();
     } else if (FolderItem.class.isAssignableFrom(item.getClass())) {
     resp = Response.ok().header("Content-Type", MediaType.APPLICATION_JSON).build();
     } else {
     }
     } catch (LibraryException ex) {
     throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
     }
     return resp;
     }
     */

    @GET
    @Path("/browse{path:.*}")
    @Consumes("application/json")
    public Response browseGet(@PathParam("path") String path) {
        Response resp = null;
        try {
            Item item = Library.browse(path);
            if (AudioFileItem.class.isAssignableFrom(item.getClass())) {/* && result.getName().toLowerCase().endsWith(".mp3")) {*/
                AudioFileItem fi = (AudioFileItem) item;
                File f = fi.getFile();
                // header("transferMode.dlna.org", "Streaming").
                // Bit 24 - Streaming Mode Flag
                // Bit 22 - Background Mode Flag
                // Bit 21 - HTTP Connection Stalling Flag
                // Bit 20 - DLNA v1.5 versioning flag
                // 01700000 00000000 00000000 00000000
                long len = f.length();
                // header("Content-Length", "" + f.length())
                resp = Response.ok(f, "audio/mpeg").status(206).header("Server", "Linux, UPnP/1.0 DLNADOC/1.50, Serviio/1.0.1").header("transferMode.dlna.org", "Streaming").header("contentFeatures.dlna.org", "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=01500000000000000000000000000000").header("Content-Range", "bytes 0-" + (len - 1) + "/" + len).header("Cache-control", "no-cache").header("Date", "Sun, 11 Nov 2012 22:39:59 GMT").build();
                //resp = Response.ok(f, "audio/mpeg").header("Content-Length", "" + f.length()).build();
            } else if (FolderItem.class.isAssignableFrom(item.getClass())) {
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
