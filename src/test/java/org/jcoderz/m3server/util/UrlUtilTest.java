package org.jcoderz.m3server.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test the UrlUtil methods.
 *
 * @author mrumpf
 */
public class UrlUtilTest {

    @Test
    public void testEncodePathFileUrl() {
        String path = "file:///tmp/x[y]= ";
        String expResult = "file:///tmp/x%5By%5D%3D%20";
        String result = UrlUtil.encodePath(path);
        assertEquals(expResult, result);
    }
}
