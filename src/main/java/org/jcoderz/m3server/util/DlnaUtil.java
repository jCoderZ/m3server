package org.jcoderz.m3server.util;

import java.util.Formatter;

/**
 * This class provides constants and helper methods for DLNA related stuff.
 *
 * @author mrumpf
 */
public class DlnaUtil {

    public static final String DLNA_DEVICE_VERSION = "M-DMS-1.50";
    public static final String DLNA_DEVICE_CLASS = "DMS-1.50";
    public static final String DLNA_CONTENT_FEATURES_KEY = "contentFeatures.dlna.org";
    public static final String DLNA_TRANSFER_MODE_KEY = "transferMode.dlna.org";
    public static final String DLNA_TRANSFER_MODE_STREAMING = "Streaming";
    /**
     * Operations Parameter for HTTP. <ul><li>01 - Support of the range HTTP
     * header</li></ul> NOTE: DLNA.ORG_OP=01 is necessary, if not set the PS3
     * will show "incompatible data"
     */
    public static final String DLNA_ORG_OP = "DLNA.ORG_OP=01";
    /**
     * Conversion Indicator Flag. The CI parameter is not mandatory. <ul> <li>00
     * - no conversion</li> </ul>
     */
    public static final String DLNA_ORG_CI = "DLNA.ORG_CI=0";
    /**
     * Media format profile: MP3.
     */
    public static final String DLNA_ORG_PN = "DLNA.ORG_PN=MP3";
    /**
     * DLNA Flags.
     *
     */
    public static final String DLNA_ORG_FLAGS = "DLNA.ORG_FLAGS=01500000000000000000000000000000";
    public static final int DLNA_FLAGS_VERSION_1_5 = 0b00000000000100000000000000000000;
    public static final int DLNA_FLAGS_BACKGR_MODE = 0b00000000010000000000000000000000;
    public static final int DLNA_FLAGS_STREAM_MODE = 0b00000001000000000000000000000000;
    /**
     * A standard set of DLNA flags.
     */
    public static final int DLNA_FLAGS_STANDARD = DLNA_FLAGS_VERSION_1_5 | DLNA_FLAGS_BACKGR_MODE | DLNA_FLAGS_STREAM_MODE;
    /**
     * Limited Operations Flags: Byte-Based Seek.
     */
    public static int DLNA_FLAGS_LIMOP_BYTES = 0b00100000000000000000000000000000;

    private DlnaUtil() {
        // do not allow instances
    }

    public static String contentFeatures() {
        return contentFeatures(0);
    }

    public static String contentFeatures(int additionalFlags) {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        formatter.format("%s;%s;%s;%s=%x000000000000000000000000", DLNA_ORG_PN, DLNA_ORG_OP, DLNA_ORG_CI, "DLNA.ORG_FLAGS", (DLNA_FLAGS_STANDARD | additionalFlags));
        return sb.toString();
    }
}
