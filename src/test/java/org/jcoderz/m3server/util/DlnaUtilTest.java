package org.jcoderz.m3server.util;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mrumpf
 */
public class DlnaUtilTest {

    public DlnaUtilTest() {
    }

    /**
     * Test of contentFeatures method, of class DlnaUtil.
     */
    @Test
    public void testContentFeatures() {
        int additionalFlags = DlnaUtil.DLNA_FLAGS_LIMOP_BYTES;
        String expResult = "DLNA.ORG_PN=MP3;DLNA.ORG_OP=01;DLNA.ORG_CI=0;DLNA.ORG_FLAGS=21500000000000000000000000000000";
        String result = DlnaUtil.contentFeatures(additionalFlags);
        assertEquals(expResult, result);
    }
}
