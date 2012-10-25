package org.jcoderz.m3server.library;

import java.util.List;

/**
 * The library class implements the media hierarchy in a protocol-neutral way.
 * Thus the media library can be used from the web client and from the UPnP
 * Media Server.
 *
 * @author mrumpf
 */
public class Library {

    private static final FolderItem treeRoot;

    static {
        // create the root node
        treeRoot = new FolderItem(null, "Root");

        // Create the sub-folders

        // audio
        FolderItem audio = new FolderItem(treeRoot, "audio");
        treeRoot.addChild(audio);
        FolderItem filesystem = new FileSystemFolderItem(audio, "filesystem");
        filesystem.setSubtreeRoot(true);
        audio.addChild(filesystem);

        // video
        FolderItem video = new FolderItem(treeRoot, "video");
        treeRoot.addChild(video);

        // photos
        FolderItem photos = new FolderItem(treeRoot, "photos");
        treeRoot.addChild(photos);
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
        return treeRoot;
    }

    /**
     * Returns the item that matches the path.
     *
     * @param path the path to look for
     * @return the item that matches the path
     */
    public static Item getPath(String path) throws LibraryException {
        String[] token = path.split("/");
        Item node = treeRoot;
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
