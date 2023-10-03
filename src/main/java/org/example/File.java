package org.example;

// Represents a file
class File extends BaseItem {
    private StringBuilder content;
    public File(String name) {
        super(name);
        content = new StringBuilder();
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(String newContent) {
        content = new StringBuilder(newContent);
    }
}