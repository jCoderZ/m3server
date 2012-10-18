package org.jcoderz.m3server.util;

import java.util.logging.LogManager;
import org.jcoderz.m3server.Main;

/**
 * This is a utility class that hides the logger initialization behind an easy
 * method.
 *
 * @author mrumpf
 */
public class Logging {

    private static boolean initialized = false;

    public static synchronized java.util.logging.Logger getLogger(Class clazz) {
        if (!initialized) {
            try {
                LogManager.getLogManager().readConfiguration(Main.class.getResourceAsStream("logging.properties"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return java.util.logging.Logger.getLogger(clazz.getName());
    }
}
