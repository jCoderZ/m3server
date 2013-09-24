package org.jcoderz.m3server.library;

import org.jcoderz.m3server.library.Library.Category;
import org.junit.Ignore;
import org.junit.Test;

public class LibraryTest {

	@Ignore
	@Test
	public void testx() {
		Library l = new Library();
		l.addCategory(Category.AUDIO);
		l.addProvider(Category.AUDIO, "test", null);
	}
}
