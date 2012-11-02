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
 * This class handles file downloads out of the library.
 * <p>TODO: This will probably be substituted by REST APIs.</p>
 *
 * @author mrumpf
 */
public class LibraryJettyHttpHandler extends AbstractHandler {

    private static final Logger logger = Logging.getLogger(LibraryJettyHttpHandler.class);
    public static final String MIMETYPE_AUDIO_MPEG = "audio/mpeg";
    public static final String FILE_EXTENSION_MP3 = ".mp3";

    public static void sendFile(final HttpServletResponse response, URL u)
            throws IOException {

        try (InputStream fis = u.openStream()) {
            response.setStatus(HttpStatus.OK_200);
            File file = new File(u.getPath());
            String path = file.getAbsolutePath();
            int dot = path.lastIndexOf('.');
            if (path.toLowerCase().endsWith(FILE_EXTENSION_MP3)) {
                response.setContentType(MIMETYPE_AUDIO_MPEG);
            } else {
                throw new RuntimeException("TODO: Unknown extension");
            }

            final int length = (int) file.length();
            response.setContentLength(length);

            final OutputStream outputBuffer = response.getOutputStream();

            long size = 0L;
            byte[] buffer = new byte[8192];
            int bytesRead;
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
        // TODO: Use logging filter
        logger.entering(LibraryJettyHttpHandler.class.getSimpleName(), "handle", new Object[]{target, baseRequest, request, response});
        try {
            String path = UrlUtil.decodePath(target);
            logger.fine("RequestURI=" + request.getRequestURI() + ", Method=" + request.getMethod() + ", Target=" + target + ", Target(decoded)=" + path);
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
        // TODO: Use logging filter
        logger.exiting(LibraryJettyHttpHandler.class.getSimpleName(), "handle");
    }
}
