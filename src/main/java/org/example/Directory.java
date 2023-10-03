package org.example;

import java.util.*;

// Represents a directory
class Directory extends BaseItem {
    private List<BaseItem> children;

    public Directory(String name) {
        super(name);
        children = new ArrayList<>();
    }

    public List<BaseItem> getChildren() {
        return children;
    }

    public void addChild(BaseItem item) {
        children.add(item);
    }

    public boolean removeChild(BaseItem item) {
        return children.remove(item);
    }
}
