package app.odesanmi.fma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * <a
 * href="http://code.google.com/p/free-music-archive-java-api/">free-music-archive-java-api</a>
 *
 * @author roomtek
 */
public class FMAClient {

    private String apikey;
    private final String urlprefix = "http://freemusicarchive.org/api/get/";
    private final String DATASET = "dataset", VALUE = "value";
    // KONSTANTS
    public static final String curator_id = "curator_id";
    public static final String curator_title = "curator_title";
    public static final String curator_tagline = "curator_tagline";
    public static final String curator_bio = "curator_bio";
    public static final String curator_site_url = "curator_site_url";
    public static final String curator_playlists = "curator_playlists";
    public static final String curator_type = "curator_type";
    public static final String curator_handle = "curator_handle";
    public static final String curator_image_file = "curator_image_file";
    // ALBUMS
    public static final String album_id = "album_id";
    public static final String album_handle = "album_handle";
    public static final String album_url = "album_url";
    public static final String album_title = "album_title";
    public static final String album_type = "album_type";
    public static final String album_information = "album_information";
    public static final String album_date_released = "album_date_released";
    public static final String album_tracks = "album_tracks";
    // ARTISTS
    public static final String artist_id = "artist_id";
    public static final String artist_bio = "artist_bio";
    public static final String artist_website = "artist_website";
    public static final String artist_name = "artist_name";
    public static final String artist_url = "artist_url";
    // TRACKS
    public static final String track_id = "track_id";
    public static final String track_title = "track_title";
    public static final String track_url = "track_url";
    public static final String track_date_created = "track_date_created";
    public static final String license_title = "license_title";
    public static final String track_composer = "track_composer";
    public static final String track_publisher = "track_publisher";
    public static final String track_favorites = "track_favorites";
    public static final String track_duration = "track_duration";
    // GENRES
    public static final String genre_id = "genre_id";
    public static final String genre_parent_id = "genre_parent_id";
    public static final String genre_title = "genre_title";
    public static final String genre_handle = "genre_handle";

    public FMAClient(String api) {
        apikey = api;
    }

    public String getAllArtists() {

        return urlprefix.concat("artists").concat(".xml?api_key=")
                .concat(apikey);
    }

    public NodeList getCurators(int limit) throws Exception {

        String urla = urlprefix.concat("curators").concat(".xml?api_key=")
                .concat(apikey);

        if (limit != 0) {
            urla = urla.concat("&limit=" + limit);
        }

        return getReturnData(urla);
    }

    public NodeList getAlbumsForCuratorID(String ID, int limit) throws Exception {

        String urla = urlprefix.concat("albums").concat(".xml?api_key=")
                .concat(apikey).concat("&curator_id=" + ID);

        if (limit != 0) {
            urla = urla.concat("&limit=" + limit);
        }

        return getReturnData(urla);
    }

    public NodeList getTracksForCuratorID(String ID, int limit) throws Exception {

        String urla = urlprefix.concat("tracks").concat(".xml?api_key=")
                .concat(apikey).concat("&curator_id=" + ID);

        if (limit != 0) {
            urla = urla.concat("&limit=" + limit);
        }

        return getReturnData(urla);
    }

    public NodeList getGenresForCuratorID(String ID, int limit) throws Exception {

        String urla = urlprefix.concat("genres").concat(".xml?api_key=")
                .concat(apikey).concat("&curator_id=" + ID);

        if (limit != 0) {
            urla = urla.concat("&limit=" + limit);
        }

        return getReturnData(urla);
    }

    public NodeList getTracksForAlbumID(String ID, int limit) throws Exception {

        String urla = urlprefix.concat("tracks").concat(".xml?api_key=")
                .concat(apikey).concat("&album_id=" + ID);

        if (limit != 0) {
            urla = urla.concat("&limit=" + limit);
        }

        return getReturnData(urla);
    }

    public String getTracksDownloadLinkForTrackID(String ID) throws Exception {

        String urla = urlprefix.concat("tracks").concat(".xml?api_key=")
                .concat(apikey).concat("&track_id=" + ID);

        urla = getReturnData(urla).item(0).getOwnerDocument()
                .getElementsByTagName(FMAClient.track_url).item(0)
                .getTextContent();

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(
                    new URL(urla).openStream(), "UTF-8"));
            for (String line; (line = reader.readLine()) != null;) {
                builder.append(line.trim());
            }

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException logOrIgnore) {
                }
            }
        }
        String fdc = builder.toString();
        // terrible coding here
        fdc = fdc.subSequence(
                fdc.indexOf("http://freemusicarchive.org/music/download/"),
                fdc.indexOf("\" class=\"icn-arrow")).toString();

        return getTrackDownloadUrl(fdc);

    }

    public String getCuratorDataSet(String dataset, String value) throws Exception {
        String urla = urlprefix.concat("curators").concat(".xml?api_key=")
                .concat(apikey).concat("&" + dataset).concat("=").concat(value);

        return urla;

    }

    /**
     * return mp3 link for 'http://freemusicarchive.org/music/download/' type
     * links
	 *
     */
    private String getTrackDownloadUrl(String downl_url) throws Exception {
        HttpURLConnection con = (HttpURLConnection) (new URL(downl_url)
                .openConnection());
        con.setInstanceFollowRedirects(false);
        con.connect();
        int responseCode = con.getResponseCode();
        // System.out.println(responseCode);
        if (responseCode == 302) { // found
            String location = con.getHeaderField("Location");
            // System.out.println(location);
            return location;
        } else {
            return null;
        }
    }

    private NodeList getReturnData(String url) throws Exception {
        // System.out.println(url);
        Element ele = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                .parse(new URL(url).openStream()).getDocumentElement();
        ele.normalize();

        return ele.getElementsByTagName(DATASET).item(0).getOwnerDocument()
                .getElementsByTagName(VALUE);
    }

    public String getAllTracks() {

        return urlprefix.concat("tracks").concat(".xml?api_key=")
                .concat(apikey);
    }
}