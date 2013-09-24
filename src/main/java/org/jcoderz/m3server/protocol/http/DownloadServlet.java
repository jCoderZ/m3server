package org.jcoderz.m3server.protocol.http;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.jcoderz.m3server.library.LibraryException;
import org.jcoderz.m3server.library.Path;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.jcoderz.m3server.library.filesystem.FolderItem;
import org.jcoderz.m3server.playlist.Playlist;
import org.jcoderz.m3server.playlist.PlaylistManager;
import org.jcoderz.m3server.protocol.http.RangeSet.Range;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.DlnaUtil;
import org.jcoderz.m3server.util.ImageUtil;
import org.jcoderz.m3server.util.IoUtil;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.MimetypeUtil;
import org.jcoderz.m3server.util.ThreadContext;
import org.jcoderz.m3server.util.UrlUtil;

/**
 * This servlet implements the HTTP range functionality. Tests can easily be
 * done by the following command:
 * <code>curl -v --range -5 "http://localhost:8080/m3server/download/audio/03-bronze/test.txt</code>.
 *
 * @author mrumpf
 */
public class DownloadServlet extends HttpServlet {

    private static final Logger logger = Logging.getLogger(DownloadServlet.class);
    // TODO: Move mimetypes to utility class
    public static final String FILE_EXTENSION_MP3 = ".mp3";

    @Override
    protected void doHead(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        try {
            Item i = Library.LIBRARY.item(new Path(pathInfo));
            File file = getFile(i);
            logger.log(Level.FINE, "File: {0}", file);
            if (!file.exists()) {
                logger.log(Level.SEVERE, "File not found: {0}", file);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            addFileHeaders(response, file);
            response.setStatus(HttpStatus.OK_200);
        } catch (LibraryException ex) {
            logger.log(Level.SEVERE, "Path not found: {0}", pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        Item item;
        try {
            item = Library.LIBRARY.item(new Path(pathInfo));
        } catch (LibraryException ex) {
            logger.log(Level.SEVERE, "Path not found: {0}", pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File file = getFile(item);
        logger.log(Level.FINE, "File: {0}", file);
        if (!file.exists()) {
            logger.log(Level.SEVERE, "File not found: {0}", file);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        addCommonHeaders(response);

        if (request.getParameterMap().size() > 0) {
            handleCover(request, response, item);
        } else {
            if (file.isFile()) {
                handleFile(file, response, request, item);
            } else if (file.isDirectory()) {
                handleFolder(response, request);
            }
        }
    }

    private void handleCover(HttpServletRequest request, HttpServletResponse response, Item item) throws IOException {
        String cover = request.getParameterMap().get("cover")[0];
        // TODO: handle front and back ?
        if (cover != null && !cover.isEmpty()) {
            byte[] data = null;
            String mimetype = MimetypeUtil.MIMETYPE_IMAGE_PNG;
            if (FolderItem.class.isAssignableFrom(item.getClass())) {
                data = ImageUtil.FOLDER_ICON_DEFAULT;
            } else {
                data = ImageUtil.FILE_ICON_DEFAULT;
            }
            /*
            Icon icon = item.getIcon();
            if (icon != null) {
                if (icon.getData() != null) {
                    data = icon.getData();
                }
                if (icon.getMimetype() != null) {
                    mimetype = icon.getMimetype();
                }
            }
            */
            addHeaders(response, data.length, mimetype);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            IoUtil.writeFile(response.getOutputStream(), bais);
        }
    }

    private void handleFile(File file, HttpServletResponse response, HttpServletRequest request, Item item) throws IOException {
        // TODO: handle all file requests via a memory mapped buffer
        addFileHeaders(response, file);
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
                    // TODO: SC_PARTIAL_CONTENT or OK_200 ??
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    // full file requested
                    IoUtil.writeFile(response.getOutputStream(), file);
                    PlaylistManager.getPlaylist(ThreadContext.getContext().getHost()).add(item);
                } else {
                    logger.log(Level.FINE, "Sending partial file as requested: {0}", re);
                    response.setHeader("Content-Range", "bytes " + re.getFirstPos() + "-" + re.getLastPos() + "/" + length);
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    response.setContentLength((int) (re.getLastPos() - re.getFirstPos() + 1));
                    RandomAccessFile raf = new RandomAccessFile(file, "r");
                    try (FileChannel channel = raf.getChannel()) {
                        logger.log(Level.FINE, "channel size: {0}", channel.size());
                        MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, re.getFirstPos(), (re.getLastPos() - re.getFirstPos() + 1));
                        logger.log(Level.FINE, "buf: {0}", buf.toString());
                        ByteBufferBackedInputStream bais = new ByteBufferBackedInputStream(buf);
                        IoUtil.writeFile(response.getOutputStream(), bais);
                        // add the file only when this is the first range request
                        if (re.getFirstPos() == 0) {
                            PlaylistManager.getPlaylist(ThreadContext.getContext().getHost()).add(item);
                        }
                    }
                }
            }
        } else {
            response.setStatus(HttpStatus.OK_200);
            response.setContentLength((int) length);
            IoUtil.writeFile(response.getOutputStream(), file);
            PlaylistManager.getPlaylist(ThreadContext.getContext().getHost()).add(item);
        }
    }

    private void handleFolder(HttpServletResponse response, HttpServletRequest request) throws IOException {
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Cache-Control", "public");
        response.setHeader(DlnaUtil.DLNA_TRANSFER_MODE_KEY, DlnaUtil.DLNA_TRANSFER_MODE_STREAMING);
        response.setHeader(DlnaUtil.DLNA_CONTENT_FEATURES_KEY, DlnaUtil.contentFeatures(DlnaUtil.DLNA_FLAGS_LIMOP_BYTES));
        response.setHeader("Accept-Ranges", "bytes");
        response.setContentType(MimetypeUtil.MIMETYPE_AUDIO_MPEGURL);

        Playlist pls = new Playlist("adhoc");
        Path path = new Path(request.getPathInfo());
        try {
            Item i = Library.LIBRARY.item(path);
            if (FolderItem.class.isAssignableFrom(i.getClass())) {
                FolderItem fi = (FolderItem) i;
                Map<String, Item> children = fi.getChildren();
                for (Item child : children.values()) {
                    if (AudioFileItem.class.isAssignableFrom(child.getClass())) {
                        pls.add(child);
                    }
                }
            }
        } catch (LibraryException ex) {
            Logger.getLogger(DownloadServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        String plsStr = pls.export(Playlist.PlaylistType.M3U, Config.getDownloadBaseUrl());
        byte[] bytes = plsStr.getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        response.setStatus(HttpStatus.OK_200);
        response.setContentLength(bytes.length);
        IoUtil.writeFile(response.getOutputStream(), bais);
    }

    private void addFileHeaders(HttpServletResponse response, File file) {
        long length = file.length();
        String mimetype = detectMimeType(file);
        addHeaders(response, (int) length, mimetype);
    }

    private void addHeaders(HttpServletResponse response, int contentLength, String contentType) {
        response.setContentLength(contentLength);
        response.setContentType(contentType);
    }

    private void addCommonHeaders(HttpServletResponse response) {
        // TODO: set headers on GET reponse also
        // TODO: assemble DLNA String -> DlnaUtil
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Cache-Control", "public");
        response.setHeader(DlnaUtil.DLNA_TRANSFER_MODE_KEY, DlnaUtil.DLNA_TRANSFER_MODE_STREAMING);
        response.setHeader(DlnaUtil.DLNA_CONTENT_FEATURES_KEY, DlnaUtil.contentFeatures(DlnaUtil.DLNA_FLAGS_LIMOP_BYTES));
        response.setHeader("Accept-Ranges", "bytes");
    }

    private String detectMimeType(File f) {
        String result = "";
        String path = f.getAbsolutePath();
        int dot = path.lastIndexOf('.');
        if (path.toLowerCase().endsWith(FILE_EXTENSION_MP3)) {
            result = MimetypeUtil.MIMETYPE_AUDIO_MPEG;
        } else {
            throw new RuntimeException("TODO: Unknown extension");
        }
        return result;
    }

    private File getFile(Item i) {
        File result = null;
        try {
        	Path path = i.getPath();
        	// FIXME: Not correct. How to determine the URL?
            result = new File(new URI(UrlUtil.encodePath(path.toString())));
        } catch (URISyntaxException ex) {
            logger.log(Level.SEVERE, null, ex);
            // TODO: Throw exception
        }
        logger.log(Level.FINE, "Item: {0}", i);
        return result;
    }

    /**
     * This class provides an InputStream interface for a ByteBuffer.
     */
    public static class ByteBufferBackedInputStream extends InputStream {

        private ByteBuffer buf;

        /**
         * Constructor.
         *
         * @param buf the byte buffer
         */
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
