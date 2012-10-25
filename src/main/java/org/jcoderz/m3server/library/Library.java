package org.jcoderz.m3server.library;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3util.intern.util.Environment;

/**
 * The library class implements the media hierarchy in a protocol-neutral way.
 * Thus the media library can be used from the web client and from the UPnP
 * Media Server.
 *
 * @author mrumpf
 */
public class Library {

    private static final FolderItem TREE_ROOT;

    static {
        // create the root node
        TREE_ROOT = new FolderItem(null, "Root");
        try {

            // Create the sub-folders

            // Create the sub-folders
            FolderItem audio = new FolderItem(TREE_ROOT, "audio");
            TREE_ROOT.addChild(audio);

            FolderItem filesystem = new FileSystemFolderItem(audio, "filesystem");
            filesystem.setUrl(Environment.getAudioFolder().toURI().toURL());
            filesystem.setSubtreeRoot(true);
            audio.addChild(filesystem);

            // video
            FolderItem video = new FolderItem(TREE_ROOT, "video");
            TREE_ROOT.addChild(video);

            // photos
            FolderItem photos = new FolderItem(TREE_ROOT, "photos");
            TREE_ROOT.addChild(photos);
        } catch (MalformedURLException ex) {
            // TODO: Throw runtime exception
            Logger.getLogger(Library.class.getName()).log(Level.SEVERE, null, ex);
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
     * Returns the item that matches the path.
     *
     * @param path the path to look for
     * @return the item that matches the path
     */
    public static Item getPath(String path) throws LibraryException {
        String[] token = path.split("/");
        Item node = TREE_ROOT;
        for (String tok : token) {
            if (!tok.isEmpty()) {
                if (node instanceof FolderItem) {
                    FolderItem fi = (FolderItem) node;
                    node = fi.getChild(tok);
                } else {
                    // TODO: ...
                }
            }
        }
        return node;
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
}
