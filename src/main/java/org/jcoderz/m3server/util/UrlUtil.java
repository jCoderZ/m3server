package org.jcoderz.m3server.util;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class with URL related helper methods.
 *
 * @author mrumpf
 */
public class UrlUtil {

    private static final Logger logger = Logging.getLogger(UrlUtil.class);

    private UrlUtil() {
        // do not allow instances
    }

    /**
     * URL encodes the path.
     *
     * @param path the path to encode
     * @return the encoded path
     */
    public static String encodePath(String url) {
        StringBuilder strbuf = new StringBuilder();
        String sub = url;
        String result = null;
        if (sub != null) {
            if (sub.startsWith("file://")) {
                sub = sub.substring("file://".length());
            }
            StringTokenizer strtok = new StringTokenizer(sub, "/");
            while (strtok.hasMoreTokens()) {
                String tok = strtok.nextToken();
                strbuf.append('/');
                strbuf.append(encodeURLComponent(tok));
            }
            result = strbuf.toString();
            if (!url.equals(sub)) {
                result = "file://" + result;
            }
        }
        return result;
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

    /**
     * Returns the hostname for the specified IP address or the IP address when
     * hostname resolution failed.
     *
     * @param inetAddr the IP address to find the hostname for
     * @return the hostname of the IP address
     */
    public static String getHostByName(String inetAddr) {
        String result = inetAddr;
        try {
            InetAddress addr = InetAddress.getByName(inetAddr);
            result = addr.getHostName();
        } catch (UnknownHostException ex) {
            logger.log(Level.WARNING, "", ex);
        }
        return result;

    }
}
