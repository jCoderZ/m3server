package org.jcoderz.m3server.library;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.jcoderz.m3server.util.Logging;

/**
 * This is the base class for all folder items.
 *
 * @author mrumpf
 */
public class FolderItem extends AbstractItem {

    private static final Logger logger = Logging.getLogger(FolderItem.class);
    protected List<Item> children = new ArrayList<>();
    protected List<String> childrenNames = new ArrayList<>();
    protected Properties properties = new Properties();

    /**
     * Standard constructor.
     *
     * @param parent the parent item
     * @param name the name of the folder
     */
    public FolderItem(Item parent, String name) {
        super(parent, name);
    }

    /**
     * Constructor for sub-tree root items.
     *
     * @param parent the parent item
     * @param name the name of the folder
     * @param properties initialization properties for the root item
     */
    public FolderItem(Item parent, String name, Properties properties) {
        super(parent, name);
        isRoot = true;
        this.properties = properties;
    }

    /**
     * Returns the properties for this instance.
     *
     * @return the properties for this instance
     */
    @JsonIgnore
    public Properties getProperties() {
        return properties;
    }

    /**
     * Adds a child to the internal child list.
     *
     * @param child the child to add
     */
    public void addChild(Item child) {
        // TODO: check whether name is unique
        children.add(child);
        childrenNames.add(child.getName());
    }

    /**
     * Removes the specified child from the list.
     *
     * @param child the child to remove
     */
    public void removeChild(Item child) {
        children.remove(child);
        childrenNames.remove(child.getName());
    }

    /**
     * Returns the children list.
     *
     * @return the children list
     */
    @JsonIgnore
    public List<Item> getChildren() {
        return children;
    }

    /**
     * Returns the children list.
     *
     * @return the children list
     */
    public List<String> getChildrenNames() {
        return childrenNames;
    }

    /**
     * Returns the number of children.
     *
     * @return the number of children
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * Returns the child at the given list position.
     *
     * @param index the index
     * @return the child from the index position
     * @throws LibraryException when the child cannot be found
     */
    public Item getChild(int index) throws LibraryException {
        if (index >= children.size()) {
            throw new LibraryException("The child with index '" + index + "' could not be found");
        }
        return children.get(index);
    }

    /**
     * Returns the child with the given name.
     *
     * @param name the child's name
     * @return the child with the name
     * @throws LibraryException when the child cannot be found
     */
    public Item getChild(String name) throws LibraryException {
        Item result = null;
        for (Item i : children) {
            if (i.getName().equals(name)) {
                result = i;
            }
        }
        if (result == null) {
            throw new LibraryException("The child '" + name + "' could not be found");
        }
        return result;
    }
}
