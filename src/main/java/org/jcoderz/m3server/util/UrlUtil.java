package org.jcoderz.m3server.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 * Utility class with URL related helper methods.
 *
 * @author mrumpf
 */
public class UrlUtil {

    private UrlUtil() {
        // do not allow instances
    }

    /**
     * URL encodes the path.
     *
     * @param path the path to encode
     * @return the encoded path
     */
    public static String encodePath(String path) {
        StringBuilder strbuf = new StringBuilder();
        StringTokenizer strtok = new StringTokenizer(path, "/");
        while (strtok.hasMoreTokens()) {
            String tok = strtok.nextToken();
            strbuf.append('/');
            strbuf.append(URLEncoder.encode(tok));
        }
        return strbuf.toString();
    }

    /**
     * URL decodes the path.
     *
     * @param path the path to decode
     * @return the decoded path
     */
    public static String decodePath(String url) {
        StringBuilder strbuf = new StringBuilder();
        StringTokenizer strtok = new StringTokenizer(url, "/");
        while (strtok.hasMoreTokens()) {
            String tok = strtok.nextToken();
            strbuf.append('/');
            strbuf.append(URLDecoder.decode(tok));
        }
        return strbuf.toString();
    }
}
