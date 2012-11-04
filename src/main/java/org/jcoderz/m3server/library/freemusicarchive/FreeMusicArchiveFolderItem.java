package org.jcoderz.m3server.library.freemusicarchive;

import org.jcoderz.m3server.library.filesystem.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.library.FolderItem;
import org.jcoderz.m3server.library.Item;
import org.jcoderz.m3server.library.LibraryRuntimeException;
import org.jcoderz.m3server.util.Logging;

/**
 * This class represents a freemusicarchive.org folder item.
 *
 * @author mrumpf
 *
 */
public class FreeMusicArchiveFolderItem extends FolderItem {

    private static final Logger logger = Logging.getLogger(FileSystemFolderItem.class);
    private static final List<String> PROPERTY_KEYS = new ArrayList<>();
    private static final String PROPERTY_KEY_API_BASE_URL = "api.base.url";
    private static final String PROPERTY_KEY_API_KEY = "api.key";
    private static final String CATEGORIES_CURATORS = "Curators";
    private static final String CATEGORIES_GENRES = "Genres";
    private static final String CATEGORIES_ARTISTS = "Artists";

    static {
        PROPERTY_KEYS.add(PROPERTY_KEY_API_BASE_URL);
        PROPERTY_KEYS.add(PROPERTY_KEY_API_KEY);
    }

    /**
     * Standard constructor.
     *
     * @param parent the parent item
     * @param name the name of the item
     */
    public FreeMusicArchiveFolderItem(Item parent, String name) {
        super(parent, name);
    }

    /**
     * Constructor for sub-tree root elements.
     *
     * @param parent the parent item
     * @param name the name of the item
     * @param properties a list of initialization properties
     */
    public FreeMusicArchiveFolderItem(Item parent, String name, Properties properties) {
        super(parent, name);
        this.properties = properties;
        isRoot = true;
        checkPropertiesAvailable();

        logger.log(Level.CONFIG, "FreeMusicArchive '" + name + "' properties: {0}", properties);
        addChild(new FreeMusicArchiveFolderItem(this, CATEGORIES_CURATORS));
        addChild(new FreeMusicArchiveFolderItem(this, CATEGORIES_GENRES));
        addChild(new FreeMusicArchiveFolderItem(this, CATEGORIES_ARTISTS));

        FreeMusicArchiveClient.init(properties.getProperty(PROPERTY_KEY_API_BASE_URL), properties.getProperty(PROPERTY_KEY_API_KEY));
        // TODO: check for base url and 
    }

    @Override
    public List<Item> getChildren() {
        //children = new ArrayList<>();
        switch (name) {
            case CATEGORIES_CURATORS:
                List<String> curators = FreeMusicArchiveClient.getCurators();
                for (String curator : curators) {
                    children.add(new FreeMusicArchiveFolderItem(this, curator));
                }
                break;
            case CATEGORIES_GENRES:
                List<String> genres = FreeMusicArchiveClient.getGenres();
                for (String genre : genres) {
                    children.add(new FreeMusicArchiveFolderItem(this, genre));
                }
                break;
            case CATEGORIES_ARTISTS:
                List<String> artists = FreeMusicArchiveClient.getArtists();
                for (String artist : artists) {
                    children.add(new FreeMusicArchiveFolderItem(this, artist));
                }
                break;
        }
        return children;
    }

    private void checkPropertiesAvailable() {
        for (String property : PROPERTY_KEYS) {
            if (!properties.containsKey(property)) {
                final String msg = "Properties for class " + this.getClass().getSimpleName() + " do not contain the key " + property;
                logger.log(Level.SEVERE, msg);
                throw new LibraryRuntimeException(msg);
            }
        }
    }
}
