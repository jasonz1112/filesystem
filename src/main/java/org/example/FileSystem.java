package org.example;

public class FileSystem {

    private Trie trie = new Trie(-1);

    /**
     * @param path: the path to be created
     * @param val: path associated value
     * @return: the result of create
     */
    public boolean createPath(String path, int val) {
        return trie.insert(path, val);
    }

    /**
     * @param path: the path of retrieve
     * @return: path associated value
     */
    public int get(String path) {
        return trie.search(path);
    }
}