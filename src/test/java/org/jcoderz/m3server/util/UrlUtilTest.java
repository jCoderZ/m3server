package org.jcoderz.m3server.util;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
