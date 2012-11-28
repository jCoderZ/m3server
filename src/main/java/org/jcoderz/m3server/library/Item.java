package org.jcoderz.m3server.library;

import java.net.URL;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * This is the common base interface for all item types.
 *
 * @author mrumpf
 */
public interface Item extends Comparable<Item> {

    /**
     * Returns the parent of this item.
     *
     * @return the parent or null if this item is the root node
     */
    @JsonIgnore
    Item getParent();

    /**
     * Returns the full path in the library tree.
     *
     * @return the full path in the library tree
     */
    String getPath();

    /**
     * Returns the path to the next subtree root.
     *
     * @return the path to the next subtree root
     */
    String getSubtreePath();

    /**
     * Returns the full URL of the item.
     *
     * @return the full URL of the item
     */
    @JsonIgnore
    URL getUrl();

    /**
     * Returns the root file.
     *
     * @return the root file
     */
    @JsonIgnore
    String getRootUrl();

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

    /**
     * Returns the item creator.
     *
     * @return the item creator
     */
    String getCreator();

    /**
     * Sets the item creator.
     *
     * @param creator the item creator
     */
    void setCreator(String creator);

    /**
     * Returns the name of the item.
     *
     * @return the name of the item
     */
    String getName();

    /**
     * Sets the name of the item.
     *
     * @param name the name of the item
     */
    void setName(String name);

    void accept(Visitor visitor);
}
