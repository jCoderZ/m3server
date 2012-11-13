package org.jcoderz.m3server.protocol.http;

import org.jcoderz.m3server.protocol.http.RangeSet.Range;
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
    public void testGetSingleRange() {
        RangeSet r = new RangeSet(1000, "bytes=0-499");
        assertTrue("Wrong numer of groups detected", r.getRangeCount() == 1);
        Range re = r.getRange(0);
        assertTrue("Group 0 must not be null", re != null);
        assertTrue("Wrong minimum", re.getFirstPos() == 0);
        assertTrue("Wrong maximum", re.getLastPos() == 499);
    }

    @Test
    public void testGetMultipleRanges() {
        RangeSet r = new RangeSet(1000, "bytes=0-499,500-999");
        assertTrue("Wrong numer of groups detected", r.getRangeCount() == 2);
        Range re0 = r.getRange(0);
        assertTrue("Group 0 must not be null", re0 != null);
        assertTrue("Wrong minimum", re0.getFirstPos() == 0);
        assertTrue("Wrong maximum", re0.getLastPos() == 499);
        Range re1 = r.getRange(1);
        assertTrue("Group 1 must not be null", re1 != null);
        assertTrue("Wrong minimum", re1.getFirstPos() == 500);
        assertTrue("Wrong maximum", re1.getLastPos() == 999);
    }

    @Test
    public void testGetRangeWithoutMin() {
        RangeSet r = new RangeSet(1000, "bytes=-500");
        assertTrue("Wrong numer of groups detected", r.getRangeCount() == 1);
        Range re = r.getRange(0);
        assertTrue("Group 0 must not be null", re != null);
        assertTrue("Wrong minimum", re.getFirstPos() == 499);
        assertTrue("Wrong maximum", re.getLastPos() == 999);
    }

    @Test
    public void testGetRangeWithoutMax() {
        RangeSet r = new RangeSet(1000, "bytes=0-");
        assertTrue("Wrong numer of groups detected", r.getRangeCount() == 1);
        Range re = r.getRange(0);
        assertTrue("Group 0 must not be null", re != null);
        assertTrue("Wrong minimum", re.getFirstPos() == 0);
        assertTrue("Wrong maximum", re.getLastPos() == 999);
    }

    @Test
    public void testGetRangeWithoutLastGreaterThanSize() {
        RangeSet r = new RangeSet(1000, "bytes=0-1000");
        assertTrue("Wrong numer of groups detected", r.getRangeCount() == 1);
        Range re = r.getRange(0);
        assertTrue("Group 0 must not be null", re != null);
        assertTrue("Wrong minimum", re.getFirstPos() == 0);
        assertTrue("Wrong maximum", re.getLastPos() == 999);
    }

    @Test
    public void testGetRangeWithoutFirstGreaterThanLast() {
        try {
            RangeSet r = new RangeSet(1000, "bytes=100-50");
            fail("Exception was expected");
        } catch (RangeRuntimeException ex) {
            // expected
        }
    }
}
