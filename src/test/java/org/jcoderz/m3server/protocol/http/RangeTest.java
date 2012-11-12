package org.jcoderz.m3server.protocol.http;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * bytes=500-1233/1234, bytes=500-, bytes=500-1233, bytes=-500
 *
 * @author mrumpf
 */
public class RangeTest {

    public RangeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetSingleGroup() {
        Range r = new Range("bytes=0-499");
        assertTrue("", r.getGroupCount() == 1);
    }

    @Test
    public void testGetMultipleGroups() {
        Range r = new Range("bytes=0-499,500-999");
        assertTrue("", r.getGroupCount() == 2);
    }

    @Test
    public void testGetGroupWithoutMin() {
        Range r = new Range("bytes=-499");
        assertTrue("", r.getGroupCount() == 1);
    }

    @Test
    public void testGetGroupWithoutMax() {
        Range r = new Range("bytes=0-");
        assertTrue("", r.getGroupCount() == 1);
    }
}
