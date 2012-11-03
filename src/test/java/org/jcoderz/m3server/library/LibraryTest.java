package org.jcoderz.m3server.library;

import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import org.jcoderz.m3server.util.Logging;
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

    private static final Logger logger = Logging.getLogger(LibraryTest.class);

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
    public void testAddFolder() throws Exception {
        Item root = Library.getRoot();
        Item xxx = Library.addFolder("/xxx", FolderItem.class.getName(), null);
        Item yyy = Library.addFolder("/xxx/yyy", FolderItem.class.getName(), new Properties());
        Item zzz = Library.addFolder("/xxx/yyy/zzz", FolderItem.class.getName(), null);
        assertNotNull("The xxx item must not be null", xxx);        
        assertTrue("The xxx is not a root folder", !xxx.isSubtreeRoot());        
        assertTrue("The zzz is not a root folder", !zzz.isSubtreeRoot());        
        assertTrue("The yyy is a root folder", yyy.isSubtreeRoot());        
        assertNull("The parent of the root element must be null", root.getParent());
        assertEquals("Root must be the parent of the xxx folder item", root, xxx.getParent());
        assertEquals("xxx must be the parent of the yyy folder item", xxx, yyy.getParent());
        assertEquals("Root must be the parent of the parent of the parent of the zzz folder item", root, zzz.getParent().getParent().getParent());
        assertEquals("yyy must be the parent of the zzz folder item", yyy, zzz.getParent());
    }

    @Test
    public void testGetChildren() {
        Item root = Library.getRoot();
        assertEquals("Library root is not of Type FolderItem", root.getClass(), FolderItem.class);
        FolderItem fi = (FolderItem) root;
        List<Item> children = fi.getChildren();
        assertNotNull(children);
        assertEquals(fi.getChildCount(), children.size());
    }

    @Test
    public void testPrintTree() {
        Item root = Library.getRoot();
        //Library.visitTree(root);
    }

    @Test
    public void testPrintSubtrees() {
        Item root = Library.getRoot();
        Visitor v = new FindSubtreeVisitor();
        Library.visitTree(root, v);
    }

    public static final class FullPathVisitor implements Visitor {

        @Override
        public void visit(Item item) {
            System.out.println(item.getFullPath());
        }
    }

    public static final class FullSubtreePathVisitor implements Visitor {

        @Override
        public void visit(Item item) {
            System.out.println(item.getFullSubtreePath());
        }
    }

    public static final class FindSubtreeVisitor implements Visitor {

        @Override
        public void visit(Item item) {
            if (item.isSubtreeRoot()) {
                System.out.println("### subtree=" + item.getFullPath());
                Visitor visitor = new FullSubtreePathVisitor();
                Library.visitTree(item, visitor);
            }
        }
    }
}
