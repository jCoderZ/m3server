package org.jcoderz.m3server.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility methods for input/output operations.
 *
 * @author mrumpf
 */
public class IoUtil {

    private static final int DEFAULT_BUFFER_SIZE = 8192;

    private IoUtil() {
        // do not allow instances of this class
    }

    public static void writeFile(OutputStream os, File f)
            throws IOException {
        try (InputStream fis = new FileInputStream(f)) {
            writeFile(os, fis);
        }
    }

    public static void writeFile(OutputStream os, InputStream is) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        os.flush();
    }
}
