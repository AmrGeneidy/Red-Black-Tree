package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.management.RuntimeErrorException;

public class TreeMap<T extends Comparable<T>, V> implements ITreeMap<T, V> {
    final class MyEntry<K, A> implements Entry<T, V> {
        private final T key;
        private V value;

        MyEntry(T key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public T getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V old = this.value;
            this.value = value;
            return old;
        }

        @Override
        public boolean equals(Object o) {
            return (o instanceof ITreeMap) && (this.getKey().equals(((Entry<?, ?>) o).getKey())) && this.getValue().equals(((Entry<?, ?>) o).getValue());
        }

    }

    private IRedBlackTree<T, V> tree;
    private int size = 0;
    private Set<Entry<T, V>> set;

    TreeMap() {
        tree = new RedBlackTree<>();
        set = new LinkedHashSet<>();
    }

    @Override
    public Entry<T, V> ceilingEntry(T key) {
        INode<T, V> runner = tree.getRoot();
        INode<T, V> follower = tree.getRoot();
        if (runner.getKey() == null) throw new RuntimeErrorException(null);
        Entry<T, V> entry;
        while (runner.getKey() != null && runner.getKey().compareTo(key) != 0) {
            follower = runner;
            if (runner.getKey().compareTo(key) > 0) {
                runner = runner.getLeftChild();
            } else {
                runner = runner.getRightChild();
            }
        }
        if (runner.getKey() == null) {
            if (key.compareTo(follower.getKey()) < 0) {
                runner = follower;
            } else {
                runner = getSuccessor(follower);
            }
        }
        entry = new MyEntry<T, V>(runner.getKey(), runner.getValue());
        return entry;
    }

    @Override
    public T ceilingKey(T key) {
        return ceilingEntry(key) == null ? null : ceilingEntry(key).getKey();
    }

    @Override
    public void clear() {
        tree = new RedBlackTree<>();
        size = 0;
    }

    @Override
    public boolean containsKey(T key) {
        if (key == null) throw new RuntimeErrorException(null);
        return tree.contains(key);
    }

    @Override
    public boolean containsValue(V value) {
        if (value == null) throw new RuntimeErrorException(null);
        Collection<V> values = values();
        return values.contains(value);
    }

    @Override
    public Set<Entry<T, V>> entrySet() {
        set.clear();
        if (tree.getRoot().isNull()) return set;
        return inorderSet(tree.getRoot());
    }

    @Override
    public Entry<T, V> firstEntry() {
        INode<T, V> runner = tree.getRoot();
        INode<T, V> follower = tree.getRoot();
        if (runner.isNull()) return null;
        while (!runner.isNull()) {
            follower = runner;
            runner = runner.getLeftChild();
        }
        return new MyEntry<T, V>(follower.getKey(), follower.getValue());
    }

    @Override
    public T firstKey() {
        Entry<T, V> firstEntry = firstEntry();
        return (firstEntry == null ? null : firstEntry.getKey());
    }

    @Override
    public Entry<T, V> floorEntry(T key) {
        if (key == null) throw new RuntimeErrorException(null);
        INode<T, V> runner = tree.getRoot();
        INode<T, V> follower = tree.getRoot();
        Map.Entry<T, V> entry;
        while (runner.getKey() != null && runner.getKey().compareTo(key) != 0) {
            follower = runner;
            if (runner.getKey().compareTo(key) > 0) {
                runner = runner.getLeftChild();
            } else {
                runner = runner.getRightChild();
            }
        }
        if (runner.getKey() == null) {
            if (follower.getKey().compareTo(key) > 0) {
                runner = predecessor(follower);
            } else {
                runner = follower;
            }
        }
        entry = new MyEntry<T, V>(runner.getKey(), runner.getValue());
        return entry;
    }

    @Override
    public T floorKey(T key) {
        return floorEntry(key).getKey();
    }

    @Override
    public V get(T key) {
        return tree.search(key);
    }

    @Override
    public ArrayList<Entry<T, V>> headMap(T toKey) {
        ArrayList<Entry<T, V>> result = new ArrayList<>();
        Set<Entry<T, V>> set = entrySet();
        for (Entry<T, V> e : set) {
            if (e.getKey().compareTo(toKey) < 0) result.add(e);
        }
        return result;
    }

    @Override
    public ArrayList<Entry<T, V>> headMap(T toKey, boolean inclusive) {
        ArrayList<Entry<T, V>> result = new ArrayList<>();
        Set<Entry<T, V>> set = entrySet();
        for (Entry<T, V> e : set) {
            if (e.getKey().compareTo(toKey) < 0 || (e.getKey().compareTo(toKey) == 0 && inclusive)) result.add(e);
        }
        return result;
    }

    @Override
    public Set<T> keySet() {
        Set<Entry<T, V>> set = entrySet();
        Set<T> keys = new LinkedHashSet<>();
        for (Entry<T, V> e : set) {
            keys.add(e.getKey());
        }
        return keys;
    }

    @Override
    public Entry<T, V> lastEntry() {
        INode<T, V> runner = tree.getRoot();
        INode<T, V> follower = tree.getRoot();
        if (runner.isNull()) return null;
        while (!runner.isNull()) {
            follower = runner;
            runner = runner.getRightChild();
        }
        return new MyEntry<T, V>(follower.getKey(), follower.getValue());
    }

    @Override
    public T lastKey() {
        Entry<T, V> lastEntry = lastEntry();
        return (lastEntry == null ? null : lastEntry.getKey());
    }

    @Override
    public Entry<T, V> pollFirstEntry() {
        Entry<T, V> firstEntry = firstEntry();
        if (firstEntry == null) return firstEntry;
        remove(firstEntry.getKey());
        return firstEntry;
    }

    @Override
    public Entry<T, V> pollLastEntry() {
        Entry<T, V> lastEntry = lastEntry();
        if (lastEntry == null) return lastEntry;
        remove(lastEntry.getKey());
        return lastEntry;
    }

    @Override
    public void put(T key, V value) {
        if (!containsKey(key)) size++;
        tree.insert(key, value);
    }

    @Override
    public void putAll(Map<T, V> map) {
        if (map == null) throw new RuntimeErrorException(null);
        size += map.size();
        for (Entry<T, V> entry : map.entrySet()) {
            tree.insert(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public boolean remove(T key) {
        if (key == null) throw new RuntimeErrorException(null);
        boolean found = tree.delete(key);
        if (found) size--;
        return found;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Collection<V> values() {
        Set<Entry<T, V>> set = entrySet();
        Collection<V> values = new LinkedHashSet<>();
        for (Entry<T, V> e : set) {
            values.add(e.getValue());
        }
        return values;
    }

    private INode<T, V> getSuccessor(INode<T, V> n) {
        INode<T, V> node = n;
        if (node.getRightChild().getKey() != null) {
            while (node.getLeftChild().getKey() != null) {
                node = node.getLeftChild();
            }
            return node;
        }
        INode<T, V> y = node.getParent();
        while (y.getKey() != null && node == y.getRightChild()) {
            node = y;
            y = node.getParent();
        }
        return y;
    }

    private INode<T, V> predecessor(INode<T, V> n) {
        INode<T, V> node = n;
        if (node.getLeftChild().getKey() != null) {
            node = node.getLeftChild();
            while (node.getRightChild() != null) {
                node = node.getRightChild();
            }
            return node;
        }
        INode<T, V> y = node.getParent();
        while (y.getKey() != null && node == y.getLeftChild()) {
            node = y;
            y = node.getParent();
        }
        return y;
    }

    private Set<Entry<T, V>> inorderSet(INode<T, V> node) {
        if (node.isNull()) return set;
        set = inorderSet(node.getLeftChild());
        Entry<T, V> entry = new MyEntry<T, V>(node.getKey(), node.getValue());
        set.add(entry);
        set = inorderSet(node.getRightChild());
        return set;
    }
}
