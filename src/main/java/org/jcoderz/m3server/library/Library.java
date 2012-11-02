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
    public static final byte[] FOLDER_ICON_DEFAULT;
    public static final byte[] FILE_ICON_DEFAULT;

    static {
        InputStream folderInputStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("org/jcoderz/m3server/ui/resources/images/folder.png");

        FOLDER_ICON_DEFAULT = readFromStream(folderInputStream);
        InputStream fileInputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("org/jcoderz/m3server/ui/resources/images/audio-x-generic.png");
        FILE_ICON_DEFAULT = readFromStream(fileInputStream);

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
        } catch (Exception ex) {
            // TODO: Throw runtime exception
            logger.log(Level.SEVERE, null, ex);
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
    public static Item browse(String path) throws LibraryException {
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
     * Returns the item that matches the path.
     *
     * @param query the query string
     * @return the item that matches the path
     */
    public static List<Item> search(String query) throws LibraryException {
        List<Item> result = new ArrayList<>();
        // TODO
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
                result.setBinaryData(FOLDER_ICON_DEFAULT);
                result.setMimeType("image/png");
            }
        } else if (f.isFile()) {
            MusicBrainzMetadata mb = new MusicBrainzMetadata(f);
            result = mb.getCoverImage();
            // when no image has been found inside the file
            if (result == null || result.getBinaryData() == null) {
                result = new Artwork();
                result.setBinaryData(FILE_ICON_DEFAULT);
                result.setMimeType("image/png");
            }
        } else {
            // TODO throw Exception
            throw new RuntimeException("Unknown file type " + file);
        }

        return result;
    }

    private static byte[] readFromStream(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        try {
            while (in.read(buffer) != -1) {
                out.write(buffer);
            }
        } catch (IOException ex) {
            // TODO: throw exception
        }

        return out.toByteArray();
    }

}
