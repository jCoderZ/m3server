package org.jcoderz.m3server.protocol.http.rest;

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

import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
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

    /**
     * Search for the term in the library.
     *
     * @param term the term to search for
     * @return a list of items that matches the term
     */
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

    /**
     * Browse the specified path.
     *
     * @param path the path to browse
     * @return the item that is identified by the path
     */
    @GET
    @Path("/browse{path:.*}")
    @Consumes("application/json")
    public Response browse(@PathParam("path") String path) {
        Response resp = null;
        try {
            Item item = Library.browse(path);
            resp = Response.ok(item, MediaType.APPLICATION_JSON_TYPE).build();
        } catch (LibraryException ex) {
            throw new WebApplicationException(ex, Status.INTERNAL_SERVER_ERROR);
        }
        return resp;
    }
}
