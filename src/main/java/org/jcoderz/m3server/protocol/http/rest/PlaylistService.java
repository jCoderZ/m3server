package org.jcoderz.m3server.protocol.http.rest;

import java.util.Map;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
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
     * Lists all palylists.
     *
     * @return a list of playlists
     */
    @GET
    @Path("/list")
    @Produces("application/json")
    public Response list() {
        Map<String, Playlist> p = PlaylistManager.getPlaylists();
        return Response.ok(p).build();
    }
}
