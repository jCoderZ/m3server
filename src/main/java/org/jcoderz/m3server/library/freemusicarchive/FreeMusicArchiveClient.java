package org.jcoderz.m3server.library.freemusicarchive;

import java.util.ArrayList;
import java.util.List;

/**
 * This implements the client API for the freemusicarchive.org access.
 *
 * @author mrumpf
 */
public class FreeMusicArchiveClient {

    private String apiUrl;
    private String apiKey;

    public FreeMusicArchiveClient(String apiUrl, String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }

    public List<String> getGenres() {
        List<String> result = new ArrayList<>();
        //http://freemusicarchive.org/api/get/genres.json?api_key=F4NCN1GJWU90E8GR
        return result;
    }
}
