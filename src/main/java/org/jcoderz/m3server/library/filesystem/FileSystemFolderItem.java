package org.jcoderz.m3server.library.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserPrincipal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.FolderItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.LibraryRuntimeException;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3server.util.UrlUtil;

import org.jcoderz.m3util.intern.MusicBrainzMetadata;

/**
 * This class represents a file-system folder.
 *
 * @author mrumpf
 *
 */
public class FileSystemFolderItem extends FolderItem {

    private static final Logger logger = Logging.getLogger(FileSystemFolderItem.class);
    private static final FilenameFilter MP3_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            final File file = new File(dir, name);
            boolean result = false;
            if (!file.isHidden()) {
                if (file.isDirectory()) {
                    result = !file.getName().startsWith(".");
                } else if (file.isFile()) {
                    result = file.getName().toLowerCase().endsWith(".mp3");
                }
            }
            return result;
        }
    };
    private File root = null;

    /**
     * Standard constructor.
     *
     * @param parent the parent item
     * @param name the name of the item
     */
    public FileSystemFolderItem(Item parent, String name) {
        super(parent, name);
    }

    /**
     * Constructor for sub-tree root elements.
     *
     * @param parent the parent item
     * @param name the name of the item
     * @param properties a list of initialization properties
     */
    public FileSystemFolderItem(Item parent, String name, Properties properties) {
        super(parent, name);
        this.properties = properties;
        isRoot = true;
        // TODO: Do we need a better properties library?
        String rootStr = ((String) properties.get("root")).replaceAll("\\$\\{M3_LIBRARY_HOME\\}", System.getProperty("M3_LIBRARY_HOME"));
        if (rootStr != null && !rootStr.isEmpty()) {
            root = new File(rootStr);
            logger.log(Level.CONFIG, "File system '" + name + "' root folder: {0}", root);
            if (!root.exists()) {
                throw new LibraryRuntimeException("The folder defined by the 'root' property does not exist: " + rootStr);
            }
        }
    }

    @Override
    public String getSubtreeRootUrl() {
        String result = null;
        if (root != null) {
            result = root.getAbsolutePath();
            if (result == null) {
                FileSystemFolderItem i = (FileSystemFolderItem) getParent();
                result = i.getSubtreeRootUrl();
            }
            result = "file://" + result;
        }
        return result;
    }

    @Override
    public List<Item> getChildren() {
        URI uri = null;
        try {
            uri = new URI(UrlUtil.encodePath(getUrl()));
        } catch (URISyntaxException ex) {
            Logger.getLogger(FileSystemFolderItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        File key = new File(uri);
        if (key.exists()) {
            String p = getSubtreePath();
            if (key.isDirectory()) {
                handleDirectory(key);
            } else if (key.isFile()) {
                handleFile(p, key);
            } else {
                final String msg = "Don't know what to do, path '" + p + "' is neither a folder nor a file: " + key;
                logger.severe(msg);
                throw new LibraryRuntimeException(msg);
            }
        } else {
            // TODO: throw exception: file not found
        }
        Collections.sort(children);
        return children;
    }

    private void addAudioFileItemChild(File file, String name) {
        MusicBrainzMetadata mb = new MusicBrainzMetadata(file, true);
        AudioFileItem fi = new AudioFileItem(this, name);
        fi.setFile(file);
        fi.setBitrate(mb.getBitrate() * 1024L / 8);
        fi.setSize(file.length());
        fi.setGenre(mb.getGenre());
        fi.setName(name);
        try {
            Path path = Paths.get(file.toURI());
            UserPrincipal owner = Files.getOwner(path);
            fi.setCreator(owner.getName());
        } catch (IOException ex) {
            Logger.getLogger(FileSystemFolderItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        fi.setLengthString(mb.getLengthString());
        fi.setLengthInMilliseconds(mb.getLengthInMilliSeconds());
        fi.setAlbum(mb.getAlbum());
        fi.setArtist(mb.getArtist());
        fi.setTitle(mb.getTitle());
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "Adding audio file child ''{0}'' to folder: {1}", new Object[]{fi, this});
        }
        addChild(fi);
    }

    private void handleDirectory(File dir) {
        String[] files = dir.list(MP3_FILTER);
        for (String file : files) {
            File f = new File(dir, file);
            if (f.isDirectory()) {
                FileSystemFolderItem fi = new FileSystemFolderItem(this, file);
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, "Adding folder child ''{0}'' to folder: {1}", new Object[]{fi, this});
                }
                addChild(fi);
            } else {
                addAudioFileItemChild(f, file);
            }
        }
    }

    private void handleFile(String name, File key) {
        FileItem fi = new FileItem(this, name);
        fi.setSize(key.length());
        if (logger.isLoggable(Level.FINEST)) {
            logger.log(Level.FINEST, "Adding non-audio file child ''{0}'' to folder: {1}", new Object[]{fi, this});
        }
        addChild(fi);
    }
}
