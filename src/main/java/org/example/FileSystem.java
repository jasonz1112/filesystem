package org.example;

import java.util.*;

public class FileSystem {
    private Directory root;
    private Directory currentDirectory;
    private Map<String, BaseItem> linkMap;

    public FileSystem() {
        root = new Directory("/");
        currentDirectory = root;
        linkMap = new HashMap<>();
        linkMap.put("/", root);
    }

    // Create a directory at the current working directory
    public void createDirectory(String directoryName) {
        Directory newDirectory = new Directory(directoryName);
        currentDirectory.addChild(newDirectory);
    }

    // Create a directory taking the full path
    public void createDirectoryFullPath(String path) {
        String[] parts = path.split("/");
        Directory current = root;

        for (String part : parts) {
            if (!part.isEmpty()) {
                Directory child = findChildDirectory(current, part);
                if (child == null) {
                    child = new Directory(part);
                    current.addChild(child);
                }
                current = child;
            }
        }
    }

    // Create a file at the current working directory
    public void createFile(String fileName) {
        File newFile = new File(fileName);
        currentDirectory.addChild(newFile);
        linkMap.put(fileName, newFile);
    }

    // Create a file taking the full path
    public void createFileFullPath(String path) {
        String[] parts = path.split("/");
        Directory current = root;
        String fileName = parts[parts.length - 1];

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];
            if (!part.isEmpty()) {
                Directory child = findChildDirectory(current, part);
                if (child == null) {
                    child = new Directory(part);
                    current.addChild(child);
                }
                current = child;
            }
        }

        File file = new File(fileName);
        current.addChild(file);
        linkMap.put(fileName, file);
    }

    // Change the current working directory to the given full path
    public void changeDirectory(String path) {
        String[] parts = path.split("/");
        Directory newCurrent = root;

        for (String part : parts) {
            if (!part.isEmpty()) {
                Directory child = findChildDirectory(newCurrent, part);
                if (child != null) {
                    newCurrent = child;
                } else {
                    System.out.println("Directory not found: " + part);
                    return; // Exit the loop if the directory doesn't exist
                }
            }
        }

        currentDirectory = newCurrent;
    }

    // find a child directory by name
    private Directory findChildDirectory(Directory parent, String name) {
        for (BaseItem item : parent.getChildren()) {
            if (item instanceof Directory && item.getName().equals(name)) {
                return (Directory) item;
            }
        }
        return null;
    }

    // Remove a directory from the current working directory's children
    public boolean removeDirectory(String directoryName) {
        Directory directoryToRemove = null;

        for (BaseItem item : currentDirectory.getChildren()) {
            if (item instanceof Directory && item.getName().equals(directoryName)) {
                directoryToRemove = (Directory) item;
                break;
            }
        }

        if (directoryToRemove != null) {
            return currentDirectory.removeChild(directoryToRemove);
        } else {
            return false; // Directory not found among children
        }
    }

    // Write contents to a file in the current working directory
    public boolean writeFile(String fileName, String fileContents) {
        for (BaseItem item : currentDirectory.getChildren()) {
            if (item instanceof File && item.getName().equals(fileName)) {
                ((File) item).setContent(fileContents);
                return true;
            }
        }
        // File not found among children
        return false;
    }

    // Get contents of a file in the current working directory
    public String getFileContents(String fileName) {
        for (BaseItem item : currentDirectory.getChildren()) {
            if (item instanceof File && item.getName().equals(fileName)) {
                return ((File) item).getContent();
            }
        }
        // File not found among children
        return null;
    }

    // Move a file to a new location within the current directory or another directory
    public boolean moveFile(String fileName, String newPath) {
        for (BaseItem item : currentDirectory.getChildren()) {
            if (item instanceof File && item.getName().equals(fileName)) {
                File fileToMove = (File) item;
                currentDirectory.removeChild(fileToMove);

                // Determine the target directory for the move
                String[] pathParts = newPath.split("/");
                Directory targetDirectory = currentDirectory;

                for (String part : pathParts) {
                    if (!part.isEmpty()) {
                        Directory childDir = findChildDirectory(targetDirectory, part);
                        if (childDir == null) {
                            // The target directory does not exist and needs to create
                            childDir = new Directory(part);
                            targetDirectory.addChild(childDir);
                        }
                        targetDirectory = childDir;
                    }
                }

                targetDirectory.addChild(fileToMove);
                return true;
            }
        }
        // File not found among children
        return false;
    }

    // Find all files and directories with the given name in the current working directory
    public List<BaseItem> findItemsByName(String itemName) {
        List<BaseItem> foundItems = new ArrayList<>();
        for (BaseItem item : currentDirectory.getChildren()) {
            if (item.getName().equals(itemName)) {
                foundItems.add(item);
            }
        }
        return foundItems;
    }

    // return current directory
    public Directory getCurrentDirectory() {
        return currentDirectory;
    }

    // get current directory contents
    public List<BaseItem> getCurrentDirectoryContents() {
        return currentDirectory.getChildren();
    }

    // Create a symbolic link at the current working directory
    public void createSymlink(String symlinkName, String targetPath) {
        Symlink symlink = new Symlink(symlinkName, targetPath);
        currentDirectory.addChild(symlink);
        linkMap.put(symlinkName, symlink);
    }

    // Create a hard link in the current working directory
    public void createHardLink(String hardLinkName, String existingFileName) {
        BaseItem itemToLink = linkMap.get(existingFileName);
        if (itemToLink != null && itemToLink instanceof File) {
            File existingFile = (File) itemToLink;
            File hardLink = new File(hardLinkName);
            hardLink.setContent(existingFile.getContent());
            currentDirectory.addChild(hardLink);
            linkMap.put(hardLinkName, hardLink);
        }
    }

    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();

        // Create directories and files
        fileSystem.createDirectoryFullPath("/home");
        fileSystem.createDirectoryFullPath("/home/user");
        fileSystem.createFileFullPath("/home/user/file1.txt");
        fileSystem.createFileFullPath("/home/user/file2.txt");

        // List the contents of the root directory
        Directory root = fileSystem.root;
        System.out.println("Current directory:" + fileSystem.currentDirectory.getName());

        List<BaseItem> rootContents = root.getChildren();
        for (BaseItem item : rootContents) {
            System.out.println(item.getName());
        }

        // Change the current working directory to /home/user
        fileSystem.changeDirectory("/home/user");

        // List the contents of /home/user
        Directory currentDir = fileSystem.getCurrentDirectory();
        System.out.println("Current directory:" + currentDir.getName());
        List<BaseItem> currentDirContents = fileSystem.getCurrentDirectoryContents();
        for (BaseItem item : currentDirContents) {
            System.out.println(item.getName());
        }

        // change directory to /home
        fileSystem.changeDirectory("/home");

        // List the contents of the current working directory
        currentDir = fileSystem.getCurrentDirectory();
        System.out.println("Current directory:" + currentDir.getName());

        // List the contents of /home
        currentDirContents = fileSystem.getCurrentDirectoryContents();
        for (BaseItem item : currentDirContents) {
            System.out.println(item.getName());
        }

        // change directory to one non-exist
        fileSystem.changeDirectory("/home/newNonExistentPath");

        // change directory to /home/user
        fileSystem.changeDirectory("/home/user");

        // create and change to directory /home/user/desktop
        fileSystem.createDirectory("desktop");
        fileSystem.changeDirectory("/home/user/desktop");

        // create 2 files in /home/user/desktop
        fileSystem.createFile("desktopfile1.txt");
        fileSystem.createFile("desktopfile2.txt");

        currentDir = fileSystem.getCurrentDirectory();
        System.out.println("Current directory:" + currentDir.getName());

        // List the contents of /home/user/desktop
        currentDirContents = fileSystem.getCurrentDirectoryContents();
        for (BaseItem item : currentDirContents) {
            System.out.println(item.getName());
        }

        // Write contents to a file in the current working directory
        boolean writeSuccess = fileSystem.writeFile("desktopfile1.txt", "This is the content of desktopfile1.txt.");
        if (writeSuccess) {
            System.out.println("File 'desktopfile1.txt' updated successfully.");
        } else {
            System.out.println("File 'desktopfile1.txt' not found or couldn't be updated.");
        }

        // Get contents of a file in the current working directory
        String fileContents = fileSystem.getFileContents("desktopfile1.txt");
        if (fileContents != null) {
            System.out.println("Contents of desktopfile1.txt:");
            System.out.println(fileContents);
        } else {
            System.out.println("File 'desktopfile1.txt' not found.");
        }

        // Move a file to a new location
        boolean moveSuccess = fileSystem.moveFile("desktopfile1.txt", "desktopFolder1/desktopfile1.txt");
        if (moveSuccess) {
            System.out.println("File 'desktopfile1.txt' moved successfully.");
        } else {
            System.out.println("File 'desktopfile1.txt' not found or couldn't be moved.");
        }

        // Find all items with the name "desktopfile2.txt" in the current working directory
        List<BaseItem> foundItems = fileSystem.findItemsByName("desktopfile2.txt");
        for (BaseItem item : foundItems) {
            System.out.println("Found item: " + item.getName());
        }

        // change directory to /home/user
        fileSystem.changeDirectory("/home/user");

        // remove a directory from the current working directory's children
        boolean removed = fileSystem.removeDirectory("desktop");
        if (removed) {
            System.out.println("Directory 'desktop' removed successfully.");
        } else {
            System.out.println("Directory 'desktop' not found or couldn't be removed.");
        }

        // List the contents of /home/user after removing the desktop directory
        currentDir = fileSystem.getCurrentDirectory();
        System.out.println("Current directory:" + currentDir.getName());

        currentDirContents = fileSystem.getCurrentDirectoryContents();
        for (BaseItem item : currentDirContents) {
            System.out.println(item.getName());
        }

        // simple symlink
        fileSystem.createSymlink("symlink1", "/home/user/file1.txt");
        // Access a symlink
        Symlink symlink = (Symlink) fileSystem.linkMap.get("symlink1");
        System.out.println("Symlink target: " + symlink.getTargetPath());

        // Write contents to a file in the current working directory
        writeSuccess = fileSystem.writeFile("file1.txt", "This is the content of file1.txt.");
        if (writeSuccess) {
            System.out.println("File 'file1.txt' updated successfully.");
        } else {
            System.out.println("File 'file1.txt' not found or couldn't be updated.");
        }

        // Create a hard link
        fileSystem.createHardLink("hardlink1.txt", "file1.txt");
        // Access a hard link
        File hardlink = (File) fileSystem.linkMap.get("hardlink1.txt");
        System.out.println("Hard link content: " + hardlink.getContent());

    }
}