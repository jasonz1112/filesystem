package org.example;

import java.util.ArrayList;
import java.util.List;

public class Directory {

    private String dirname;
    private List<File> files;
    private List<Directory> subdirs;

    public Directory(String name) {
        dirname = name;
        files = new ArrayList<>();
        subdirs = new ArrayList<>();
    }

    public void add(File file) {
        files.add(file);
    }

    public void add(Directory dir) {
        subdirs.add(dir);
    }

    public boolean empty() {
        return checkRecursively(this);
    }

    public boolean checkRecursively(Directory dir) {
        if (dir.files.size() > 0) {
            return false;
        }
        for (Directory subdir : dir.subdirs) {
            if (!checkRecursively(subdir)) {
                return false;
            }
        }
        return true;
    }

}