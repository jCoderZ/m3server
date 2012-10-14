package org.jcoderz.m3server.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author mrumpf
 */
public class Node {

    private List<Node> children = new ArrayList<>();
    private Node parent;
    private Item item;

    /**
     * Constructor.
     * 
     * @param item the node data
     * @param parent the parent node
     */
    public Node(Item item, Node parent) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    public List<Node> getChildren() {
        return Collections.unmodifiableList(children);
    }

    public int getChildCount() {
        return children.size();
    }

    public String getPath() {
        createPath(parent);
        return "";
    }

    private String createPath(Node parent) {
        if (parent != null) {
            
        }
        return "";
    }
    public Node getParent() {
        return parent;
    }
}
