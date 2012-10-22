package org.jcoderz.m3server.library;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author mrumpf
 */
public abstract class AbstractItem implements Item {

    protected List<Item> children = new ArrayList<>();
    protected Item parent;
    protected boolean isRoot = false;
    protected String name;
    protected String displayName;
    protected String icon;
    protected String creator;
    protected Object data;

    public AbstractItem() {
    }

    public AbstractItem(Item parent, String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
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
    public void addChild(Item child) {
        children.add(child);
    }

    @Override
    public void removeChild(Item child) {
        children.remove(child);
    }

    @Override
    public List<Item> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public Item getChild(int index) {
        return children.get(0);
    }

    @Override
    public String getFullPath() {
        return createPath(this, false);
    }

    @Override
    public String getFullSubtreePath() {
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
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public void setDisplayName(String name) {
        this.displayName = name;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
