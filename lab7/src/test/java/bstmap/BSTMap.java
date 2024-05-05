package bstmap;

import java.util.Set;
import java.util.Iterator;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private class BSTNode {
        private K key;
        private V val;
        private BSTNode left, right;

        public BSTNode(K key, V val) {
            this.key = key;
            this.val = val;
        }
    }

    private BSTNode root;
    private int size;

    public BSTMap() {
        root = null;
        size = 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        if (root == null)
            return false;
        return getHelp(root, key) != null;
    }

    @Override
    public V get(K key) {
        BSTNode temp = getHelp(root, key);
        if (temp == null)
            return null;
        else {
            return getHelp(root, key).val;
        }
    }

    private BSTNode getHelp(BSTNode p, K key) {
        if (p == null) {
            return null;
        }

        int cmp = key.compareTo(p.key);
        if (cmp == 0) {
            return p;
        }
        if (cmp < 0)
            return getHelp(p.left, key);
        else
            return getHelp(p.right, key);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void put(K key, V value) {
        size += 1;
        root = putHelp(root, key, value);
    }

    private BSTNode putHelp(BSTNode p, K key, V value) {
        if (p == null) {
            return new BSTNode(key, value);
        }
        int cmp = key.compareTo(p.key);

        if (cmp < 0)
            p.left = putHelp(p.left, key, value);
        else
            p.right = putHelp(p.right, key, value);
        return p;
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void printInOrder() {
        printInOrderHelp(root);
    }

    private void printInOrderHelp(BSTNode p) {
        if (p == null)
            return;

        System.out.println("key " + p.key + "value " + p.val);
        printInOrderHelp(p.left);
        printInOrderHelp(p.right);
    }
}
