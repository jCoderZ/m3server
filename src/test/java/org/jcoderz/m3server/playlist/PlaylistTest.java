package org.jcoderz.m3server.playlist;

import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.Library;
import org.junit.Test;

/**
 * A test-case for the Playlist class.
 *
 * @author mrumpf
 */
public class PlaylistTest {

    @Test
    public void testM3u() throws Exception {
        Playlist pl = new Playlist("new");
        Item i = Library.browse("/audio/filesystem/01-gold/D/Dsk-2000/Electro-def/01 - intro.mp3");
        pl.add(i);
        i = Library.browse("/audio/filesystem/01-gold/D/Dsk-2000/Electro-def/02 - lectro-def.mp3");
        pl.add(i);
        String m3u = pl.export(Playlist.PlaylistType.M3U, "");
        System.out.println(m3u);
        String m3uext = pl.export(Playlist.PlaylistType.M3UEXT, "");
        System.out.println(m3uext);
        String pls = pl.export(Playlist.PlaylistType.PLS, "");
        System.out.println(pls);
    }
}
