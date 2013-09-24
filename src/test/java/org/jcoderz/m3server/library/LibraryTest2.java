package org.jcoderz.m3server.library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.logging.Logger;

import org.jcoderz.m3server.library.filesystem.FileItem;
import org.jcoderz.m3server.library.filesystem.FilesystemProvider;
import org.jcoderz.m3server.library.filesystem.FolderItem;
import org.jcoderz.m3server.util.Logging;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author mrumpf
 */
public class LibraryTest2 {

	private static final Logger logger = Logging.getLogger(LibraryTest2.class);

	private static final Library LIBRARY = new Library();
	@Ignore
	@Test
	public void testGetRoot() {
//		Item root = Library.getRoot();
//		assertNotNull(root);
//		assertNull(root.getParent());
	}

	@BeforeClass
	public static void setup() {
		FilesystemProvider fs = new FilesystemProvider();
		fs.setRoot("src/test/library/audio");
	}

	@Test
	public void testGetAudio() throws Exception {
		Item audio = LIBRARY.item(new Path("/audio"));
		assertNotNull(audio);
		assertEquals("The item name does not match", "audio", audio.getName());
		Item parent = audio.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "Root", parent.getName());
	}

	@Test
	public void testGetAudioFilesystem() throws Exception {
		Item fs = LIBRARY.item(new Path("/m3library"));
		assertNotNull(fs);
		assertEquals("The item name does not match", "filesystem", fs.getName());
		Item parent = fs.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "audio", parent.getName());
	}

	@Test
	public void testGetAudioFilesystem01Gold() throws Exception {
		Item i = LIBRARY.item(new Path("/m3library/01-gold"));
		assertNotNull(i);
		assertEquals("The item name does not match", "01-gold", i.getName());
		Item parent = i.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "filesystem",
				parent.getName());
	}

	@Test
	public void testGetAudioFilesystem01GoldD() throws Exception {
		Item d = LIBRARY.item(new Path("/m3library/01-gold/D"));
		assertNotNull(d);
		assertEquals("The item name does not match", "D", d.getName());
		Item parent = d.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "01-gold",
				parent.getName());
	}

	@Test
	public void testGetAudioFilesystem01GoldDDsk2000() throws Exception {
		Item dsk = LIBRARY.item(new Path("/m3library/01-gold/D/Dsk-2000"));
		assertNotNull(dsk);
		assertEquals("The item name does not match", "Dsk-2000", dsk.getName());
		Item parent = dsk.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "D", parent.getName());
	}

	@Test
	public void testGetAudioFilesystem01GoldDDsk2000ElectroDef()
			throws Exception {
		Item def = LIBRARY
				.item(new Path("/m3library/01-gold/D/Dsk-2000/Electro-def"));
		assertNotNull(def);
		assertEquals("The item name does not match", "Electro-def",
				def.getName());
		Item parent = def.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "Dsk-2000",
				parent.getName());
		assertTrue("The item is not of type FolderItem",
				FolderItem.class.isAssignableFrom(def.getClass()));
		FolderItem fi = (FolderItem) def;
		Map<String, Item> c = fi.getChildren();
		assertEquals("Number of children does not match: " + c, 4, c.size());
		for (Item i : c.values()) {
			assertTrue("The item is not of type FileItem",
					FileItem.class.isAssignableFrom(i.getClass()));
		}
	}
/*
	@Test
	public void testGetAudioFile() throws Exception {
		Item f = LIBRARY
				.item(new Path("/m3library/01-gold/D/Dsk-2000/Electro-def/01 - intro.mp3"));
		assertNotNull(f);
		assertEquals("The item name does not match", "01 - intro.mp3",
				f.getName());
		Item parent = f.getParent();
		assertNotNull(parent);
		assertEquals("The item name does not match", "Electro-def",
				parent.getName());
		assertTrue("The item is not of type FileItem",
				FileItem.class.isAssignableFrom(f.getClass()));
		FileItem fi = (FileItem) f;
		assertEquals(
				"The full path does not match",
				"/m3library/01-gold/D/Dsk-2000/Electro-def/01 - intro.mp3",
				fi.getPath());
		assertEquals("The file length does not match", 5236640, fi.getSize());
		assertEquals("The full sub-tree path does not match",
				"/01-gold/D/Dsk-2000/Electro-def/01 - intro.mp3",
				fi.getSubtreePath());
		assertNotNull("The creator must not be null", fi.getCreator());

		String url = fi.getUrl();
		assertNotNull("The url must not be null", url);
		File file = new File(new URI(UrlUtil.encodePath(url)));
		assertNotNull("The file must not be null", file);
		assertTrue("The file does not exist", file.exists());
	}

	@Test
	public void testAddFolder() throws Exception {
		Item root = Library.getRoot();
		Item xxx = Library.addFolder("/xxx", FolderItem.class.getName(), null);
		Item yyy = Library.addFolder("/xxx/yyy", FolderItem.class.getName(),
				new Properties());
		Item zzz = Library.addFolder("/xxx/yyy/zzz",
				FolderItem.class.getName(), null);
		assertNotNull("The xxx item must not be null", xxx);
		assertTrue("The xxx is not a root folder", !xxx.isSubtreeRoot());
		assertTrue("The zzz is not a root folder", !zzz.isSubtreeRoot());
		assertTrue("The yyy is a root folder", yyy.isSubtreeRoot());
		assertNull("The parent of the root element must be null",
				root.getParent());
		assertEquals("Root must be the parent of the xxx folder item", root,
				xxx.getParent());
		assertEquals("xxx must be the parent of the yyy folder item", xxx,
				yyy.getParent());
		assertEquals(
				"Root must be the parent of the parent of the parent of the zzz folder item",
				root, zzz.getParent().getParent().getParent());
		assertEquals("yyy must be the parent of the zzz folder item", yyy,
				zzz.getParent());
	}

	@Test
	public void testGetChildren() {
		Item root = Library.getRoot();
		assertEquals("Library root is not of Type FolderItem", root.getClass(),
				FolderItem.class);
		FolderItem fi = (FolderItem) root;
		List<Item> children = fi.getChildren();
		assertNotNull(children);
		assertEquals(fi.getChildCount(), children.size());
	}

	@Test
	@Ignore
	public void testPrintTree() {
		Item root = Library.getRoot();
		Visitor v = new LibraryVisitor();
		Library.visitTree(root, v);
	}
*/
	public static final class FullPathVisitor implements Visitor {

		@Override
		public void visit(Item item) {
			System.out.println("---" + item.getPath());
		}
	}

	public static final class LibraryVisitor implements Visitor {

		@Override
		public void visit(Item item) {
			if (FolderItem.class.isAssignableFrom(item.getClass())) {
				FolderItem fi = (FolderItem) item;
				System.out.println(item.getPath() + " " + fi.getChildren());
			} else {
				System.out.println(item.getPath());
			}
		}
	}
}
