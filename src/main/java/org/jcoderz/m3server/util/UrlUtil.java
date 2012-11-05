package org.jcoderz.m3server.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
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
            strbuf.append(encodeURLComponent(tok));
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

    public static String encodeURLComponent(final String s) {
        if (s == null) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();

        try {
            for (int i = 0; i < s.length(); i++) {
                final char c = s.charAt(i);

                if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))
                        || ((c >= '0') && (c <= '9'))
                        || (c == '-') || (c == '.') || (c == '_') || (c == '~')) {
                    sb.append(c);
                } else {
                    final byte[] bytes = ("" + c).getBytes("UTF-8");

                    for (byte b : bytes) {
                        sb.append('%');

                        int upper = (((int) b) >> 4) & 0xf;
                        sb.append(Integer.toHexString(upper).toUpperCase(Locale.US));

                        int lower = ((int) b) & 0xf;
                        sb.append(Integer.toHexString(lower).toUpperCase(Locale.US));
                    }
                }
            }

            return sb.toString();
        } catch (UnsupportedEncodingException uee) {
            throw new RuntimeException("UTF-8 unsupported!?", uee);
        }
    }
}
