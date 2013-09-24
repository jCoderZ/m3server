package org.jcoderz.m3server.protocol.http.rest;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.playlist.Playlist;
import org.jcoderz.m3server.playlist.PlaylistManager;
import org.jcoderz.m3server.util.Logging;

/**
 * This class provides a RESTful service interface to the Playlist
 * functionality.
 *
 * @author mrumpf
 */
@Path("/playlist")
@Consumes("application/json")
@Produces("application/json")
public class PlaylistService {

    private static final Logger logger = Logging.getLogger(LibraryService.class);

    /**
     * Lists all playlists.
     *
     * @return a list of playlists
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    public Response list() {
        Map<String, Playlist> p = PlaylistManager.getPlaylists();
        logger.log(Level.INFO, "Playlists: " + p);
        return Response.ok(p).build();
    }

    /**
     * Lists a playlist.
     *
     * @return a playlist
     */
    @GET
    @Path("/show/{name}")
    @Produces("application/json")
    public Response show(@PathParam("name") String name) {
        Playlist pl = PlaylistManager.getPlaylist(name);
        return Response.ok(pl).build();
    }

    /**
     * Add an item to the playlist.
     *
     * @param name the name of the playlist
     * @param path of the item to add to the playlist
     */
    @POST
    @Path("/list/{name}/add{path:.*}")
    @Produces("application/json")
    public void add(@PathParam("name") String name, @PathParam("path") String path) {
        try {
            Collection<Item> items = Library.LIBRARY.browse(new org.jcoderz.m3server.library.Path(path));
            // FIXME: add all items
            PlaylistManager.getPlaylist(name).add(null);
        } catch (LibraryException ex) {
            logger.log(Level.SEVERE, "TODO", ex);
            // TODO: Wrap into WebApplicatuionException
        }
    }

    /**
     * Remove an item from the playlist.
     *
     * @param name the name of the playlist
     * @param path of the item to remove from the playlist
     */
    @POST
    @Path("/list/{name}/remove{path:.*}")
    @Produces("application/json")
    public void remove(@PathParam("name") String name, @PathParam("path") String path) {
        try {
            Collection<Item> items = Library.LIBRARY.browse(new org.jcoderz.m3server.library.Path(path));
            // FIXME: Remove all items
            PlaylistManager.getPlaylist(name).remove(null);
        } catch (LibraryException ex) {
            logger.log(Level.SEVERE, "TODO", ex);
            // TODO: Wrap into WebApplicatuionException
        }
    }
}
