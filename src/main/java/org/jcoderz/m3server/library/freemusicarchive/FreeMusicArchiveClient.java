package org.jcoderz.m3server.library.freemusicarchive;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.jcoderz.m3server.util.Logging;

/**
 * This implements the client API for the freemusicarchive.org access.
 *
 * @author mrumpf
 */
public class FreeMusicArchiveClient {

    private static final Logger logger = Logging.getLogger(FreeMusicArchiveClient.class);
    private static String apiUrl;
    private static String apiKey;

    private static void convertJsonArrayToSet(JSONObject object, List<String> list, String field) throws JSONException {
        JSONArray a = object.getJSONArray("dataset");
        for (int i = 0; i < a.length(); i++) {
            JSONObject genre = a.getJSONObject(i);
            list.add(genre.getString(field));
        }
    }

    private FreeMusicArchiveClient() {
        // do not allow instances of this class
    }

    public static void init(String url, String key) {
        apiUrl = url;
        apiKey = key;

        final String proxyHost = "www-le.dienste.telekom.de";
        final String proxyPort = "8080";
        System.setProperty("http.proxyHost", proxyHost); 
        System.setProperty("http.proxyPort", proxyPort); 
        System.setProperty("https.proxyHost", proxyHost); 
        System.setProperty("https.proxyPort", proxyPort); 
    }

    public static List<String> getCurators() {
        return getData("curators", "curator_title");
    }
    public static List<String> getGenres() {
        return getData("genres", "genre_title");
    }
    public static List<String> getArtists() {
        return getData("artists", "artist_name");
    }

    public static List<String> getData(String data,String field) {
        List<String> result = new ArrayList<>();

        MultivaluedMap<String, String> params = new MultivaluedMapImpl();
        params.add("api_key", "F4NCN1GJWU90E8GR");

        try {
            JSONObject object = getData(data, params);
            convertJsonArrayToSet(object, result, field);
            int pages = object.getInt("total_pages");
            int page = 2;
            do {
                object = getData(data, params, page++);
                convertJsonArrayToSet(object, result, field);
            } while(object != null && page <= pages);
        } catch (JSONException ex) {
            logger.log(Level.SEVERE, "An exception occured while converting the response to JSON", ex);
            // TODO: throw new exception
        }

        return result;
    }

    private static JSONObject getData(String data, MultivaluedMap<String, String> params, int page) {
        params.add("page", "" + page);
        return getData(data, params);
    }
    private static JSONObject getData(String data, MultivaluedMap<String, String> params) {
        ClientConfig cc = new DefaultClientConfig();
        Client c = Client.create(cc);
        WebResource r = c.resource("http://freemusicarchive.org/api/get");
        JSONObject object = null;
        ClientResponse response = r.path(data + ".json").
                queryParams(params).
                accept(MediaType.APPLICATION_JSON_TYPE).
                get(ClientResponse.class);
        String str = response.getEntity(String.class);
        try {
            object = new JSONObject(str);
        } catch (JSONException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        return object;
    }
}
