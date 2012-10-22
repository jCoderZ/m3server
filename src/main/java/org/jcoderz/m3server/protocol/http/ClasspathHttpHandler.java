package org.jcoderz.m3server.protocol.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import org.glassfish.grizzly.Grizzly;
import org.glassfish.grizzly.http.server.HttpHandler;
import org.glassfish.grizzly.http.server.Request;
import org.glassfish.grizzly.http.server.Response;
import org.glassfish.grizzly.http.server.StaticHttpHandler;
import org.glassfish.grizzly.http.server.io.OutputBuffer;
import org.glassfish.grizzly.http.server.util.MimeType;
import org.glassfish.grizzly.http.util.HttpStatus;

/**
 * ClasspathHttpHandler processes requests to static resources inside a Java
 * archive (JAR).
 *
 * @author mrumpf
 */
public class ClasspathHttpHandler extends HttpHandler {

    private static final Logger LOGGER = Grizzly.logger(ClasspathHttpHandler.class);
    private Class clazz;

    /**
     * Constructor.
     *
     * @param c the resource as the root for the resource lookup
     */
    public ClasspathHttpHandler(Class c) {
        clazz = c;
    }

    @Override
    public void service(final Request request, final Response response) throws Exception {
        final String uri = getRelativeURI(request);

        if (uri == null || !handle(uri, request, response)) {
            onMissingResource(request, response);
        }
    }

    protected String getRelativeURI(final Request request) {
        String uri = request.getRequestURI();
        if (uri.indexOf("..") >= 0) {
            return null;
        }

        final String resourcesContextPath = request.getContextPath();
        if (resourcesContextPath.length() > 0) {
            if (!uri.startsWith(resourcesContextPath)) {
                return null;
            }

            uri = uri.substring(resourcesContextPath.length());
        }

        return uri;
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

        boolean found = false;
        String uri = path.substring(1);

        int dot = uri.lastIndexOf(".");
        if (dot < 0) {
            uri = uri + "index.html";
        }
        // TODO: "ui" prefix ???
        InputStream is = clazz.getResourceAsStream("ui/" + uri);
        if (is == null) {
            return found;
        } else {
            found = true;
        }

        try {
            res.setStatus(HttpStatus.OK_200);
            dot = uri.lastIndexOf('.');
            if (dot > 0) {
                String ext = uri.substring(dot + 1);
                String ct = MimeType.get(ext);
                if (ct != null) {
                    res.setContentType(ct);
                }
            }

            final OutputBuffer outputBuffer = res.getOutputBuffer();

            byte b[] = new byte[8192];
            int rd;
            while ((rd = is.read(b)) > 0) {
                outputBuffer.write(b, 0, rd);
            }
        } finally {
            try {
                is.close();
            } catch (IOException ignore) {
            }
        }
        return found;
    }
}
