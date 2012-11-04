package org.jcoderz.m3server.library;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
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
                String clazz = Config.getConfig().getString(Config.LIBRARY_ROOTS + path.replace('/', '.') + ".clazz", FolderItem.class.getName());
                Properties props = Config.getConfig().getProperties(Config.LIBRARY_ROOTS + path.replace('/', '.') + ".properties");
                logger.log(Level.CONFIG, "Properties for folder {0}: {1}", new Object[]{path, props});
                FolderItem i = addFolder(path, clazz, props);
                rootFolderItems.add((FolderItem) i);
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
     * @param clazz the class to instantiate
     * @throws LibraryException when the path cannot be added
     */
    public static FolderItem addFolder(String path, String clazz) throws LibraryException {
        return addFolder(path, clazz, null);
    }

    /**
     * Adds a path to the library.
     *
     * @param path the path to add
     * @param clazz the class to instantiate
     * @param properties initialization properties
     * @throws LibraryException when the path cannot be added
     */
    public static FolderItem addFolder(String path, String clazz, Properties properties) throws LibraryException {
        FolderItem newItem = null;
        // TODO: Handle / at the end
        int idx = path.lastIndexOf('/');
        String newElement = path.substring(idx + 1);
        Item item = getParent(path);

        if (FolderItem.class
                .isAssignableFrom(item.getClass())) {
            FolderItem fi = (FolderItem) item;
            try {
                Class clz = Thread.currentThread().getContextClassLoader().loadClass(clazz);
                if (properties != null) {
                    Constructor c = clz.getConstructor(new Class[]{Item.class, String.class, Properties.class});
                    newItem = (FolderItem) c.newInstance(new Object[]{item, newElement, properties});
                } else {
                    Constructor c = clz.getConstructor(new Class[]{Item.class, String.class});
                    newItem = (FolderItem) c.newInstance(new Object[]{item, newElement});
                }
                fi.addChild(newItem);
            } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                logger.log(Level.SEVERE, "TODO", ex);
            }
        } else {
            throw new LibraryRuntimeException("Unknown class " + clazz);
            // TODO: throw Exception("path does not denote a folder item")
        }
        return newItem;
    }

    /**
     * Returns the parent item for the specified path.
     *
     * @param path the path to get the parent item for
     * @return the parent item for the specified path
     * @throws LibraryException when the parent item could not be found
     */
    public static Item getParent(String path) throws LibraryException {
        String p = path;
        if (path.endsWith("/") && path.length() >= 2) {
            p = path.substring(0, path.length() - 2);
        }
        Item result = TREE_ROOT;
        // TODO: Handle / at the end
        int idx = p.lastIndexOf('/');
        String oldPath = (idx >= 1 ? p.substring(0, idx) : "");
        if (!oldPath.isEmpty()) {
            result = browse(oldPath);
        }
        return result;
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
                    logger.log(Level.FINE, "Browsing folder {0}", fi);
                    node = fi.getChild(tok);
                } else {
                    // TODO: ...
                    logger.log(Level.FINE, "Browsing {0}", node);
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
            if (Searchable.class
                    .isAssignableFrom(fi.getClass())) {
                Searchable s = (Searchable) fi;

                logger.log(
                        Level.FINE, "Searching ''{0}'' in {1}", new Object[]{query, fi});
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

        if (FolderItem.class
                .isAssignableFrom(node.getClass())) {
            FolderItem fi = (FolderItem) node;
            List<Item> c = fi.getChildren();
            for (Item i : c) {
                visitTree(i, visitor);
            }
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
