package org.jcoderz.m3server.library;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mrumpf
 */
public class LibraryTest {
    
    public LibraryTest() {
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
    public void testGetRoot() {
        Item root = Library.getRoot();
        assertNotNull(root);
        assertNull(root.getParent());
    }
    
    @Test
    public void testGetChildren() {
        Item root = Library.getRoot();
        List<Item> children = root.getChildren();
        assertNotNull(children);
        assertEquals(root.getChildCount(), children.size());
    }

    @Test
    public void testTraverseTree() {
        Item root = Library.getRoot();
        Library.traverseTree(root);
    }
}
