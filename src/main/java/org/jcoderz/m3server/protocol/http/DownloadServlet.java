package org.jcoderz.m3server.protocol.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.EofException;
import org.jcoderz.m3server.protocol.http.RangeSet.Range;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3util.intern.util.Environment;

/**
 * This servlet implements the HTTP range functionality. Tests can easily be
 * done by the following command:
 * <code>curl -v --range -5 "http://localhost:8080/m3server/download/audio/03-bronze/test.txt</code>.
 *
 * @author mrumpf
 */
public class DownloadServlet extends HttpServlet {

    private static final Logger logger = Logging.getLogger(DownloadServlet.class);
    public static final String MIMETYPE_AUDIO_MPEG = "audio/mpeg";
    public static final String MIMETYPE_TEXT_PLAIN = "text/plain";
    public static final String FILE_EXTENSION_MP3 = ".mp3";

    @Override
    protected void doHead(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String requestedFile = request.getPathInfo().substring("/audio/filesystem".length());
        logger.log(Level.FINE, "HEAD pathInfo={0}", requestedFile);
        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        File root = Environment.getAudioFolder().getAbsoluteFile();
        logger.log(Level.FINE, "Library root: {0}", root);
        File file = new File(root, requestedFile);
        if (!file.exists()) {
            logger.log(Level.SEVERE, "File not found: {0}", file);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        long length = file.length();
        response.setContentLength((int) length);
        setMimeType(response, file);
        // TODO: set headers on GET reponse also
        // TODO: assemble DLNA String -> DlnaUtil
        // TODO: Cache-Control: public
        // TODO: Connection: keep-alive
        response.setHeader("transferMode.dlna.org", "Streaming");
        response.setHeader("contentFeatures.dlna.org", "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=21500000000000000000000000000000");
        response.setHeader("Accept-Ranges", "bytes");
        response.setStatus(HttpStatus.OK_200);
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        // TODO: Temporary hack to make the servlet work
        String requestedFile = request.getPathInfo().substring("/audio/filesystem".length());
        logger.log(Level.FINE, "GET pathInfo={0}", requestedFile);
        if (requestedFile == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        File root = Environment.getAudioFolder().getAbsoluteFile();
        logger.log(Level.FINE, "Library root: {0}", root);
        File file = new File(root, requestedFile);
        if (!file.exists()) {
            logger.log(Level.SEVERE, "File not found: {0}", file);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        // TODO: use a servlet filter and attach those information to the current thread
        String clientHost = request.getRemoteHost();
        setMimeType(response, file);
        long length = file.length();
        String range = request.getHeader("Range");
        logger.log(Level.FINE, "range={0}", range);
        if (range != null) {
            RangeSet rg = new RangeSet(length, range);
            if (rg.getRangeCount() > 1) {
                logger.log(Level.SEVERE, "Multiple ranges are not supported!");
                response.setHeader("Content-Range", "bytes */" + length);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
            } else {
                Range re = rg.getRange(0);
                if (re.getFirstPos() == 0 && re.getLastPos() == length - 1) {
                    logger.log(Level.FINE, "Sending full file as requested: {0}", re);
                    response.setHeader("Content-Range", "bytes 0-" + (length - 1) + "/" + length);
                    response.setContentLength((int) length);
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    // full file requested
                    sendFile(response.getOutputStream(), file, clientHost);
                } else {
                    logger.log(Level.FINE, "Sending partial file as requested: {0}", re);
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    response.setContentLength((int) (re.getLastPos() - re.getFirstPos() + 1));
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    try (FileChannel channel = raf.getChannel()) {
                        logger.log(Level.FINE, "channel size: {0}", channel.size());
                        MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, re.getFirstPos(), (re.getLastPos() - re.getFirstPos() + 1));
                        logger.log(Level.FINE, "buf: {0}", buf.toString());
                        ByteBufferBackedInputStream bais = new ByteBufferBackedInputStream(buf);
                        sendFile(response.getOutputStream(), bais, clientHost);
                    }
                }
            }
        } else {
            response.setStatus(HttpStatus.OK_200);
            response.setContentLength((int) length);
            sendFile(response.getOutputStream(), file, clientHost);
        }
    }

    public void sendFile(OutputStream os, File f, String clientHost)
            throws IOException {
        try (InputStream fis = new FileInputStream(f)) {
            sendFile(os, fis, clientHost);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "TODO: sendFile", ex);
        }
    }

    public void setMimeType(HttpServletResponse response, File f) {
        String path = f.getAbsolutePath();
        int dot = path.lastIndexOf('.');
        if (path.toLowerCase().endsWith(FILE_EXTENSION_MP3)) {
            response.setContentType(MIMETYPE_AUDIO_MPEG);
        } else {
            response.setContentType(MIMETYPE_TEXT_PLAIN);
            //throw new RuntimeException("TODO: Unknown extension");
        }
    }

    public void sendFile(OutputStream os, InputStream is, String clientHost) {
        long count = 0L;
        try {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                count += bytesRead;
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        } catch (EofException ex) {
            logger.log(Level.FINER, "Connection reset by peer: " + clientHost);
        } catch (IOException ex) {
            logger.log(Level.FINER, "I/O Exception occured: " + ex, ex);
        }
        logger.log(Level.FINER, "Sent {0} bytes", count);
    }

    public static class ByteBufferBackedInputStream extends InputStream {

        private ByteBuffer buf;

        public ByteBufferBackedInputStream(ByteBuffer buf) {
            this.buf = buf;
        }

        @Override
        public synchronized int read() throws IOException {
            if (!buf.hasRemaining()) {
                return -1;
            }
            return buf.get() & 0xFF;
        }

        @Override
        public synchronized int read(byte[] bytes, int off, int len)
                throws IOException {
            if (!buf.hasRemaining()) {
                return -1;
            }

            len = Math.min(len, buf.remaining());
            buf.get(bytes, off, len);
            return len;
        }
    }
}
