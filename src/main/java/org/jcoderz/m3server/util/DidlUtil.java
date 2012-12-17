package org.jcoderz.m3server.util;

import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.filesystem.AudioFileItem;
import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.cling.support.model.DIDLObject;
import org.teleal.cling.support.model.ProtocolInfo;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.item.MusicTrack;

/**
 *
 * @author mrumpf
 */
public class DidlUtil {

    public static final String MIMETYPE_AUDIO_MPEG = "audio/mpeg";
    public static final DIDLObject.Class DIDL_CLASS_OBJECT_CONTAINER = new DIDLObject.Class("object.container");

    private DidlUtil() {
        // do not allow instances
    }

    public static MusicTrack createMusicTrack(AudioFileItem audioFileItem, String url, long nextId, long parentId) throws InvalidValueException {
        String creator = audioFileItem.getArtist();
        Res res = new Res(new ProtocolInfo("http-get:*:" + MIMETYPE_AUDIO_MPEG + ":" + DlnaUtil.DLNA_ORG_PN + ";" + DlnaUtil.DLNA_ORG_OP + ";" + DlnaUtil.DLNA_ORG_CI + ";" + DlnaUtil.DLNA_ORG_FLAGS),
                audioFileItem.getSize(), TimeUtil.convertMillis(audioFileItem.getLengthInMilliseconds()),
                audioFileItem.getBitrate(), url);
        MusicTrack result = new MusicTrack(
                "" + nextId, "" + parentId,
                audioFileItem.getTitle(),
                creator,
                audioFileItem.getAlbum(),
                creator,
                res);
        // Missing:
        // nrAudioChannels="2"
        // sampleFrequency="44100"
        //result.setDate("TODO");
        //result.setDescription("TODO");
        result.setOriginalTrackNumber(2);
        result.setGenres(new String[]{audioFileItem.getGenre()});
        result.setRestricted(false);
        return result;
    }

    public static Container createContainer(long nextId, long parentId, Item item) {
        Container c = new Container();
        c.setId("" + nextId);
        c.setParentID("" + parentId);
        /*
         if (FolderItem.class.isAssignableFrom(item.getClass())) {
         FolderItem fi = (FolderItem) item;
         c.setChildCount(fi.getChildren().size());
         }
         */
        c.setRestricted(true);
        c.setTitle(item.getName());
        c.setCreator(Config.getConfig().getString(Config.UPNP_SERVER_NAME_KEY));
        c.setClazz(DIDL_CLASS_OBJECT_CONTAINER);
        return c;
    }
}
