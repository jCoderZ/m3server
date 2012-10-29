/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jcoderz.m3server.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;

/**
 *
 * @author micha
 */
public class UrlUtil {

    public static String encodePath(String url) {
        StringBuilder strbuf = new StringBuilder();
        StringTokenizer strtok = new StringTokenizer(url, "/");
        while (strtok.hasMoreTokens()) {
            String tok = strtok.nextToken();
            strbuf.append('/');
            strbuf.append(URLEncoder.encode(tok));
        }
        return strbuf.toString();
    }

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
