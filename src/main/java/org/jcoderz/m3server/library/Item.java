package org.jcoderz.m3server.library;

import java.util.List;

/**
 *
 * @author mrumpf
 */
public interface Item {

    /**
     * Adds a child to the internal child list.
     * 
     * @param child the child to add
     */
    void addChild(Item child);

    /**
     * Returns the number of children.
     *
     * @return  the number of children
     */
    int getChildCount();

    /**
     * Returns the children list.
     * 
     * @return the children list
     */
    List<Item> getChildren();

    /**
     * Returns the child at the given list position.
     *
     * @param index the index
     * @return the child from the index position
     */
    Item getChild(int index);

    /**
     * Removes the specified child from the list.
     *
     * @param child the child to remove
     */
    void removeChild(Item child);

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

    String getDisplayName();

    String getName();

    void setCreator(String creator);

    void setDisplayName(String name);

    void setName(String path);
}
