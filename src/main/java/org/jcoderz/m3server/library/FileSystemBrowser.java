package org.jcoderz.m3server.library;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jcoderz.mp3.intern.MusicBrainzMetadata;
import org.jcoderz.mp3.intern.util.Environment;

/**
 * The FileSystemBrowser reads the folder hierarchy and caches the items in a
 * map, so that file-system access can be reduced.
 *
 * TODO: Check memory consumption!!!
 *
 * @author mrumpf
 *
 */
public class FileSystemBrowser {

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
    }

    public static byte[] readFromStream(InputStream in) {
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

    public static List<Item> createItemList() {
        return createItemList(null);
    }

    public static List<Item> createItemList(String path) {
        List<Item> items = new ArrayList<Item>();
        File key = null;
        File root = Environment.getAudioFolder();
        if (path == null || path.isEmpty()) {
            key = root;
        } else {
            key = new File(root, path);
        }
        if (key.exists()) {
            if (key.isDirectory()) {
                String[] files = key.list();
                for (String file : files) {
                    File f = new File(key, file);
                    if (f.isDirectory()) {
                        FolderItem fi = new FolderItem(path, file, path + "/" + file + "/cover", "???");
                        fi.setMimetype(MimeType.FOLDER);
                        items.add(fi);
                    } else {
                        MusicBrainzMetadata mb = new MusicBrainzMetadata(f);
                        AudioFileItem fi = new AudioFileItem();
                        fi.setSize(f.length());
                        fi.setPath(path);
                        fi.setName(file);
                        fi.setMimetype(MimeType.MP3);
                        fi.setLengthString(mb.getLengthString());
                        fi.setLengthInMilliseconds(mb.getLengthInMilliSeconds());
                        fi.setAlbum(mb.getAlbum());
                        fi.setArtist(mb.getArtist());
                        fi.setTitle(mb.getTitle());
                        fi.setIcon(path + "/" + file + "/cover");
                        fi.setUrl(path + "/" + file);
                        items.add(fi);
                    }
                }
            } else if (key.isFile()) {
                FileItem fi = new FileItem();
                fi.setSize(key.length());
                fi.setPath(path);
                fi.setName(key.getName());
                fi.setIcon(path + "/" + key.getName() + "/cover");
                fi.setUrl(path + "/" + key.getName());
                items.add(fi);
            } else {
                // TODO: throw exception: unknown type
            }
        }
        return items;
    }
}
