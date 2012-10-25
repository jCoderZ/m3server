package org.jcoderz.m3server.protocol.http;

import java.io.CharConversionException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.http.server.io.OutputBuffer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;

/**
 *
 * @author mrumpf
 */
public class LibraryHttpHandler extends HttpHandler {

    private static final Logger LOGGER = Grizzly.logger(ClasspathHttpHandler.class);

    @Override
    public void service(final Request request, final Response response) throws Exception {
        final String uri = getRelativeURI(request);

        if (uri == null || !handle(uri, request, response)) {
            onMissingResource(request, response);
        }
    }

    protected String getRelativeURI(final Request request) {
        try {
            String uri = request.getDecodedRequestURI();
            if (uri.indexOf("..") >= 0) {
                // TODO: return 400 bad request
                return null;
            }
            final String resourcesContextPath = request.getContextPath();
            if (resourcesContextPath.length() > 0) {
                if (uri.startsWith(resourcesContextPath)) {
                    uri = uri.substring(resourcesContextPath.length());
                    return uri;
                }
            }
        } catch (CharConversionException ex) {
            Logger.getLogger(LibraryHttpHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * The method will be called, if the static resource requested by the
     * {@link Request} wasn't found, so {@link StaticHttpHandler} implementation
     * may try to workaround this situation. The default implementation - sends
     * a 404 response page by calling
     * {@link #customizedErrorPage(Request, Response)}.
     *
     * @param request the {@link Request}
     * @param response the {@link Response}
     * @throws Exception
     */
    protected void onMissingResource(final Request request, final Response response)
            throws Exception {
        response.setStatus(HttpStatus.NOT_FOUND_404);
        customizedErrorPage(request, response);
    }

    /**
     * Lookup a resource based on the request path, and send it to the client.
     *
     * @param path The request path
     * @param req the {@link Request}
     * @param res the {@link Response}
     * @throws Exception
     */
    protected boolean handle(final String path,
            final Request req,
            final Response res) throws Exception {

        boolean result = false;
        System.err.println("path=" + path);
        Item item = Library.getPath(path);
        if (item == null) {
            res.setStatus(HttpStatus.NOT_FOUND_404);
        } else if (FileItem.class.isAssignableFrom(item.getClass())) {
            FileItem fi = (FileItem) item;
            try (InputStream is = null) {
                res.setStatus(HttpStatus.OK_200);
                System.err.println("item=" + item);

                final OutputBuffer outputBuffer = res.getOutputBuffer();

                byte b[] = new byte[8192];
                int rd;
                while ((rd = is.read(b)) > 0) {
                    outputBuffer.write(b, 0, rd);
                }
                result = true;
            }
        }
        return result;
    }
}
