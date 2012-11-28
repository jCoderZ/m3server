package org.jcoderz.m3server.library;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jcoderz.m3server.util.Logging;

/**
 * This class provides the common implementation for all item types.
 *
 * @author mrumpf
 */
public abstract class AbstractItem implements Item {

    private static final Logger logger = Logging.getLogger(AbstractItem.class);
    protected Item parent;
    protected boolean isRoot = false;
    protected String name;
    protected String displayName;
    protected String icon;
    protected String creator;
    protected Object data;
    protected URL url;

    /**
     * Empty default constructor.
     */
    public AbstractItem() {
    }

    /**
     * Constructor.
     *
     * @param parent the parent item
     * @param name the name of the item
     */
    public AbstractItem(Item parent, String name) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean isSubtreeRoot() {
        return isRoot;
    }

    @Override
    public void setSubtreeRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    @Override
    public URL getUrl() {
        try {
            String u = createUrl(this);
            // TODO: Is this replace correct?
            return new URL(u.replaceAll(" ", "%20"));
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        // TODO: throw exception
        return null;
    }

    private String createUrl(Item item) {
        String result = null;
        if (item.getParent() != null && !item.isSubtreeRoot()) {
            String p = createUrl(item.getParent());
            if (p.endsWith("/")) {
                result = p + item.getName();
            } else {
                result = p + "/" + item.getName();
            }
        } else if (item.isSubtreeRoot()) {
            result = item.getSubtreeRootUrl();
        } else {
            // TODO: parent is null
        }
        return result;
    }

    @Override
    public String getSubtreeRootUrl() {
        return null;
    }

    @Override
    public String getPath() {
        return createPath(this, false);
    }

    @Override
    public String getSubtreePath() {
        return createPath(this, true);
    }

    private String createPath(Item item, boolean subTreeOnly) {
        String result = null;
        if (item.getParent() != null && !(subTreeOnly && item.isSubtreeRoot())) {
            String p = createPath(item.getParent(), subTreeOnly);
            if (p.endsWith("/")) {
                result = p + item.getName();
            } else {
                result = p + "/" + item.getName();
            }
        } else {
            result = "/";
        }
        return result;
    }

    @Override
    public Item getParent() {
        return parent;
    }

    @Override
    public String getCreator() {
        return creator;
    }

    @Override
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String path) {
        this.name = path;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Item o) {
        return name.compareTo(o.getName());
    }
}
