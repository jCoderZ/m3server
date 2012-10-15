package org.jcoderz.m3server.library;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jcoderz.mp3.intern.MusicBrainzMetadata;
import org.jcoderz.mp3.intern.util.Environment;

/**
 *
 * @author mrumpf
 *
 */
public class FileSystemFolderItem extends AbstractItem {

    public FileSystemFolderItem(Item parent, String name, String displayName) {
        super(parent, name, displayName);
    }

    public List<Item> getChildren() {
        children = new ArrayList<Item>();
        File key = null;
        File root = Environment.getAudioFolder();
        String p = getFullSubtreePath();
        if ("/".equals(p)) {
            key = root;
        } else {
            key = new File(root, p);
        }
        if (key.exists()) {
            if (key.isDirectory()) {
                String[] files = key.list();
                for (String file : files) {
                    File f = new File(key, file);
                    if (f.isDirectory()) {
                        FileSystemFolderItem fi = new FileSystemFolderItem(parent, file, file);
                        children.add(fi);
                    } else {
                        MusicBrainzMetadata mb = new MusicBrainzMetadata(f);
                        AudioFileItem fi = new AudioFileItem();
                        fi.setSize(f.length());
                        fi.setName(p);
                        fi.setDisplayName(file);
                        fi.setLengthString(mb.getLengthString());
                        fi.setLengthInMilliseconds(mb.getLengthInMilliSeconds());
                        fi.setAlbum(mb.getAlbum());
                        fi.setArtist(mb.getArtist());
                        fi.setTitle(mb.getTitle());
                        fi.setUrl(p + "/" + file);
                        children.add(fi);
                    }
                }
            } else if (key.isFile()) {
                FileItem fi = new FileItem();
                fi.setSize(key.length());
                fi.setName(p);
                fi.setDisplayName(key.getName());
                fi.setUrl(p + "/" + key.getName());
                children.add(fi);
            } else {
                // TODO: throw exception: unknown type
            }
        }

        return children;
    }

}
