package org.jcoderz.m3server.library;

/**
 *
 * @author mrumpf
 */
public class Library {

    private static Node treeRoot;
    public static final String CREATOR = "creator";

    private Library() {
        // do not allow instances
    }

    public static Node getRoot() {
        return treeRoot;
    }

    static {
        treeRoot = new Node(new FolderItem("", "Root", null, CREATOR), null);
        
        // Create the sub-folders
        
        // audio
        Node audio = new Node(new FolderItem("audio", "Audio", null, CREATOR), treeRoot);
        treeRoot.addChild(audio);
        Node filesystem = new Node(new FolderItem("filesystem", "File-System", null, CREATOR), audio);
        audio.addChild(filesystem);

        // video
        Node video = new Node(new FolderItem("/video", "Video", null, CREATOR), treeRoot);
        treeRoot.addChild(video);

        // photos
        Node photos = new Node(new FolderItem("/photos", "Photos", null, CREATOR), treeRoot);
        treeRoot.addChild(photos);
    }
}
