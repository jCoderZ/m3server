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

    private static Item treeRoot;

    private Library() {
        // do not allow instances
    }

    public static Item getRoot() {
        return treeRoot;
    }

    static {
        // create the root node
        treeRoot = new FolderItem(null, "", "Root");

        // Create the sub-folders

        // audio
        Item audio = new FolderItem(treeRoot, "audio", "Audio");
        treeRoot.addChild(audio);
        Item filesystem = new FileSystemFolderItem(audio, "filesystem", "File-System");
        filesystem.setSubtreeRoot(true);
        audio.addChild(filesystem);

        // video
        Item video = new FolderItem(treeRoot, "video", "Video");
        treeRoot.addChild(video);

        // photos
        Item photos = new FolderItem(treeRoot, "photos", "Photos");
        treeRoot.addChild(photos);
    }

    /**
     * Helper method that traverses the tree and applies the visitor to each
     * node.
     *
     * @param root the root element of the tree
     */
    public static void visitTree(Item root, Visitor visitor) {
        root.accept(visitor);
        List<Item> c = root.getChildren();
        for (Item i : c) {
            visitTree(i, visitor);
        }
    }
}
