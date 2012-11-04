package org.jcoderz.m3server.library.filesystem;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.jcoderz.m3server.library.AudioFileItem;
import org.jcoderz.m3server.library.FileItem;
import org.jcoderz.m3server.library.FolderItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.LibraryRuntimeException;
import org.jcoderz.m3server.util.Logging;

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
        String rootStr = (String) properties.get("root");
        if (rootStr != null && !rootStr.isEmpty()) {
            root = new File(rootStr);
            logger.log(Level.CONFIG, "File system '" + name + "' root folder: {0}", root);
        }
    }

    /**
     * Returns the file-system root.
     *
     * @return the file-system root
     */
    @JsonIgnore
    public File getRoot() {
        File result = root;
        if (result == null) {
            FileSystemFolderItem i = (FileSystemFolderItem) getParent();
            result = i.getRoot();
        }
        return result;
    }

    @Override
    public List<Item> getChildren() {
        children = new ArrayList<>();
        File key = getRoot();
        String p = getFullSubtreePath();
        if (!"/".equals(p)) {
            key = new File(key, p);
        }
        if (key.exists()) {
            if (key.isDirectory()) {
                String[] files = key.list(MP3_FILTER);
                for (String file : files) {
                    File f = new File(key, file);
                    if (f.isDirectory()) {
                        FileSystemFolderItem fi = new FileSystemFolderItem(this, file);
                        if (logger.isLoggable(Level.FINEST)) {
                            logger.log(Level.FINEST, "Adding folder child ''{0}'' to folder: {1}", new Object[]{fi, this});
                        }
                        addChild(fi);
                    } else {
                        MusicBrainzMetadata mb = new MusicBrainzMetadata(f);
                        AudioFileItem fi = new AudioFileItem(this, file);
                        fi.setBitrate(mb.getBitrate() * 1024L / 8);
                        fi.setSize(f.length());
                        fi.setGenre(mb.getGenre());
                        fi.setName(file);
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
                }
            } else if (key.isFile()) {
                FileItem fi = new FileItem(this, p);
                fi.setSize(key.length());
                if (logger.isLoggable(Level.FINEST)) {
                    logger.log(Level.FINEST, "Adding non-audio file child ''{0}'' to folder: {1}", new Object[]{fi, this});
                }
                addChild(fi);
            } else {
                final String msg = "Don't know what to do, path '" + p + "' is neither a folder nor a file: " + key;
                logger.severe(msg);
                throw new LibraryRuntimeException(msg);
            }
        } else {
            // TODO: throw exception: file not found
        }
        return children;
    }
}
