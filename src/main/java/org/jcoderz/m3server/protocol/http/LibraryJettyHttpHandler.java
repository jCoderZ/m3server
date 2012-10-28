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

/**
 *
 * @author mrumpf
 */
public class LibraryJettyHttpHandler extends AbstractHandler {

    public static void sendFile(final HttpServletResponse response, URL u)
            throws IOException {

        try (InputStream fis = u.openStream()) {
            File file = new File(u.getPath());
            String path = file.getAbsolutePath();
            response.setStatus(HttpStatus.OK_200);
            int dot = path.lastIndexOf('.');
            if (path.endsWith(".mp3")) {
                response.setContentType("audio/mpeg");
            } else {
            }

            final int length = (int) file.length();
            response.setContentLength(length);
            response.setBufferSize(8192);

            final OutputStream outputBuffer = response.getOutputStream();

            byte b[] = new byte[8192];
            int rd;
            while ((rd = fis.read(b)) > 0) {
                //chunk.setBytes(b, 0, rd);
                outputBuffer.write(b, 0, rd);
            }
        } catch (Exception ex) {
            Logger.getLogger(LibraryJettyHttpHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void handle(String target, org.eclipse.jetty.server.Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            System.err.println("RequestURI=" + request.getRequestURI());
            System.err.println("Method=" + request.getMethod());
            System.err.println("path=" + target);
            Item item = Library.getPath(target);
            if (item == null) {
                response.setStatus(HttpStatus.NOT_FOUND_404);
            } else {
                URL url = item.getFullSubtreeUrl();
                sendFile(response, url);
            }
        } catch (LibraryException ex) {
            Logger.getLogger(LibraryJettyHttpHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
