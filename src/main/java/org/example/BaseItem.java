package org.example;

// base class, represents a File or Directory
public class BaseItem {
    private String name;

    public BaseItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

