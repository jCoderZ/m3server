package org.jcoderz.m3server.library;

import java.net.URL;

/**
 * This is the common base interface for all item types.
 *
 * @author mrumpf
 */
public interface Item {

    /**
     * Returns the parent of this item.
     *
     * @return the parent or null if this item is the root node
     */
    Item getParent();

    /**
     * Returns the full path in the library tree.
     *
     * @return the full path in the library tree
     */
    String getFullPath();

    /**
     * Returns the path only to the next subtree root.
     *
     * @return the path to the next subtree root
     */
    String getFullSubtreePath();

    /**
     * Returns the url for the item.
     *
     * @return the url for the item
     */
    URL getUrl();

    /**
     * Sets the base url of the item.
     *
     * @param url the base URL of the item
     */
    void setUrl(URL url);

    URL getFullSubtreeUrl();

    /**
     * Returns whether the item is the root of a subtree.
     *
     * @return whether the item is the root of a subtree
     */
    boolean isSubtreeRoot();

    /**
     * Sets the flag to indicate that the item is the root of a subtree.
     *
     * @param isSubtreeRoot true when the node is a subtree root item
     */
    void setSubtreeRoot(boolean isSubtreeRoot);

    String getCreator();

    String getName();

    void setCreator(String creator);

    void setName(String path);

    void accept(Visitor visitor);
}
