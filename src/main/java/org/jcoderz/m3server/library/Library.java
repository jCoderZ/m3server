package org.jcoderz.m3server.library;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jcoderz.m3server.library.search.Searchable;
import org.jcoderz.m3server.util.Config;
import org.jcoderz.m3server.util.ImageUtil;
import org.jcoderz.m3server.util.Logging;
import org.jcoderz.m3util.intern.MusicBrainzMetadata;
import org.jcoderz.m3util.intern.util.Environment;

/**
 * The library class implements the media hierarchy in a protocol-neutral way.
 * Thus the media library can be used from the web client and from the UPnP
 * Media Server.
 *
 * @author mrumpf
 */
public class Library {

    private static final Logger logger = Logging.getLogger(Library.class);
    private static final FolderItem TREE_ROOT;
    private static final List<FolderItem> rootFolderItems = new ArrayList<>();

    static {
        // create the root node
        TREE_ROOT = new FolderItem(null, "Root");
        try {
            // Create the sub-folders
            List<Object> l = Config.getConfig().getList(Config.LIBRARY_ROOTS);
            for (Object o : l) {
                String path = (String) o;
                logger.log(Level.CONFIG, "Adding virtual folder {0}", path);
                Item i = addFolder(path);
                if (FolderItem.class.isAssignableFrom(i.getClass())) {
                    rootFolderItems.add((FolderItem) i);
                }
                /*
                 filesystem.setUrl(Environment.getAudioFolder().toURI().toURL());
                 filesystem.setSubtreeRoot(true);
                 */
            }
        } catch (LibraryException ex) {
            // TODO: Move init away from static initializer
            final String msg = "An exception occured while configuring the top-level folder structure";
            logger.log(Level.SEVERE, msg, ex);
            throw new LibraryRuntimeException(msg, ex);
        }
    }

    private Library() {
        // do not allow instances
    }

    /**
     * Returns the library root item.
     *
     * @return the library root item
     */
    public static Item getRoot() {
        return TREE_ROOT;
    }

    /**
     * Adds a path to the library.
     *
     * @param path the path to add
     * @throws LibraryException when the path cannot be added
     */
    public static Item addFolder(String p) throws LibraryException {
        FolderItem newItem = null;
        // TODO: Handle / at the end
        int idx = p.lastIndexOf('/');
        String newElement = p.substring(idx + 1);
        String oldPath = (idx >= 1 ? p.substring(0, idx) : "");
        Item item = (oldPath.isEmpty() ? TREE_ROOT : browse(oldPath));
        if (FolderItem.class.isAssignableFrom(item.getClass())) {
            FolderItem fi = (FolderItem) item;
            newItem = new FolderItem(item, newElement);
            fi.addChild(newItem);
        } else {
            // TODO: throw Exception("path does not denote a folder item")
        }
        return newItem;
    }

    /**
     * Returns the item that matches the path.
     *
     * @param path the path to look for
     * @return the item that matches the path
     */
    public static Item browse(String path) throws LibraryException {
        String[] token = path.split("/");
        Item node = TREE_ROOT;
        for (String tok : token) {
            if (!tok.isEmpty()) {
                if (node instanceof FolderItem) {
                    FolderItem fi = (FolderItem) node;
                    logger.fine("Browsing folder " + fi);
                    node = fi.getChild(tok);
                } else {
                    // TODO: ...
                    logger.fine("Browsing " + node);
                }
            }
        }
        return node;
    }

    /**
     * Returns the item that matches the path.
     *
     * @param query the query string
     * @return the item that matches the path
     */
    public static List<Item> search(String query) throws LibraryException {
        List<Item> result = new ArrayList<>();
        for (FolderItem fi : rootFolderItems) {
            if (Searchable.class.isAssignableFrom(fi.getClass())) {
                Searchable s = (Searchable) fi;
                logger.fine("Searching '" + query + "' in " + fi);
                List<Item> items = s.search(query);
                if (!items.isEmpty()) {
                    result.addAll(items);
                }
            }
        }
        return result;
    }

    /**
     * Helper method that traverses the tree and applies the visitor to each
     * node.
     *
     * @param node the root element of the tree
     */
    public static void visitTree(Item node, Visitor visitor) {
        node.accept(visitor);
        if (FolderItem.class.isAssignableFrom(node.getClass())) {
            FolderItem fi = (FolderItem) node;
            List<Item> c = fi.getChildren();
            for (Item i : c) {
                visitTree(i, visitor);
            }
        } else {
            // node is not a FolderItem and thus there are no more children
        }
    }

    public Artwork coverImage(String file) {
        File root = Environment.getAudioFolder();
        if (file == null || file.isEmpty()) {
            // TODO throw Exception
        }

        Artwork result = null;
        File f = new File(root, file);
        if (!f.exists()) {
            // TODO throw Exception
            throw new RuntimeException("File " + file + " does not exist!");
        }
        if (f.isDirectory()) {
            String[] files = f.list();
            for (String ff : files) {
                File folderFile = new File(f, ff);
                if (folderFile.isFile()) {
                    MusicBrainzMetadata mb = new MusicBrainzMetadata(folderFile);
                    result = mb.getCoverImage();
                }
            }
            if (result == null) {
                result = new Artwork();
                result.setBinaryData(ImageUtil.FOLDER_ICON_DEFAULT);
                result.setMimeType("image/png");
            }
        } else if (f.isFile()) {
            MusicBrainzMetadata mb = new MusicBrainzMetadata(f);
            result = mb.getCoverImage();
            // when no image has been found inside the file
            if (result == null || result.getBinaryData() == null) {
                result = new Artwork();
                result.setBinaryData(ImageUtil.FILE_ICON_DEFAULT);
                result.setMimeType("image/png");
            }
        } else {
            // TODO throw Exception
            throw new RuntimeException("Unknown file type " + file);
        }

        return result;
    }
}
