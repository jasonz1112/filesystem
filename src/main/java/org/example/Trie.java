package org.example;

import java.util.*;
public class Trie {
    Map<String, Trie> children = new HashMap<>();
    int val;

    Trie(int val) {
        this.val = val;
    }

    boolean insert(String path, int val) {
        String[] words = parsePath(path);
        Trie node = this;
        int n = words.length;
        for (int i = 0; i < n; i++) {
            if (!node.children.containsKey(words[i])) {
                if (i != n - 1) return false;
                else {
                    node.children.put(words[i], new Trie(val));
                    return true;
                }
            }
            node = node.children.get(words[i]);
        }
        return false;
    }

    int search(String path) {
        Trie node = this;
        String[] words = parsePath(path);
        for (String word : words) {
            if (!node.children.containsKey(word))
                return -1;
            node = node.children.get(word);
        }
        return node.val;
    }

    String[] parsePath(String path) {
        return path.substring(1).split("/");
    }
}


