package org.jcoderz.m3server.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Image utilities.
 *
 * @author mrumpf
 */
public class ImageUtil {

    public static final byte[] FILE_ICON_DEFAULT;
    public static final byte[] FOLDER_ICON_DEFAULT;

    static {
        // TODO: Move to static image service (not yet implemented)
        InputStream folderInputStream = Thread.currentThread()
                .getContextClassLoader().getResourceAsStream("org/jcoderz/m3server/ui/resources/images/folder.png");

        FOLDER_ICON_DEFAULT = ImageUtil.readFromStream(folderInputStream);
        InputStream fileInputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("org/jcoderz/m3server/ui/resources/images/audio-x-generic.png");
        FILE_ICON_DEFAULT = ImageUtil.readFromStream(fileInputStream);
    }

    private ImageUtil() {
        // do not allow instances
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
