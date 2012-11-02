package org.jcoderz.m3server.protocol.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.UrlUtil;

/**
 *
 * @author mrumpf
 */
public class LibraryJettyHttpHandler extends AbstractHandler {

    private static final Logger logger = Logging.getLogger(LibraryJettyHttpHandler.class);
    public static void sendFile(final HttpServletResponse response, URL u)
            throws IOException {

        int bytesRead = -2;
        try (InputStream fis = u.openStream()) {
            response.setStatus(HttpStatus.OK_200);
            File file = new File(u.getPath());
            String path = file.getAbsolutePath();
            int dot = path.lastIndexOf('.');
            if (path.endsWith(".mp3")) {
                response.setContentType("audio/mpeg");
            } else {
                throw new RuntimeException("Unknown extension");
            }

            final int length = (int) file.length();
            response.setContentLength(length);

            final OutputStream outputBuffer = response.getOutputStream();

            long size = 0L;
            byte[] buffer = new byte[8192];
            while ((bytesRead = fis.read(buffer)) != -1) {
                size += bytesRead;
                outputBuffer.write(buffer, 0, bytesRead);
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "TODO", ex);
        }
    }

    @Override
    public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            System.err.println("RequestURI=" + request.getRequestURI());
            System.err.println("Method=" + request.getMethod());
            System.err.println("path=" + target);
            String path = UrlUtil.decodePath(target);
            System.err.println("RequestURI(decoded)=" + path);
            Item item = Library.browse(path);
            if (item == null) {
                response.setStatus(HttpStatus.NOT_FOUND_404);
            } else {
                URL url = item.getFullSubtreeUrl();
                sendFile(response, url);
            }
        } catch (LibraryException ex) {
            logger.log(Level.SEVERE, "TODO", ex);
        }
    }
}
