package org.example;

class Symlink extends BaseItem {
    private String targetPath;

    public Symlink(String name, String targetPath) {
        super(name);
        this.targetPath = targetPath;
    }

    public String getTargetPath() {
        return targetPath;
    }
}
