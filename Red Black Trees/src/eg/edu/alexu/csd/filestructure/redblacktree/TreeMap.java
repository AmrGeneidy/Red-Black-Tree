package eg.edu.alexu.csd.filestructure.redblacktree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;

import javax.management.RuntimeErrorException;

public class TreeMap <T extends Comparable<T>, V> implements ITreeMap<T, V> {
	final class MyEntry<K, A> implements Entry<T, V> {
	    private final T key;
	    private V value;
	    

	    public MyEntry(T key, V value) {
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
			if((this.getKey().equals(((Entry<?, ?>) o).getKey())) && (this.getValue()).equals(((Entry<?, ?>) o).getValue())) {
				return true;
			}
	      return false;
		}
	       
	}
	private IRedBlackTree<T, V> tree;
	private int size = 0;
	private Set<Entry<T, V>> set;
	
	public TreeMap() {
		// TODO Auto-generated constructor stub
		tree = new RedBlackTree<T, V>();
		set = new LinkedHashSet<>();
	}
	@Override
	public Entry<T, V> ceilingEntry(T key) {
		// TODO Auto-generated method stub
		INode<T, V> x = tree.getRoot();
		INode<T, V> y = tree.getRoot();
		if(x.getKey() == null) throw new RuntimeErrorException(null);
		Entry<T, V> entry;
		while(x.getKey() != null && x.getKey().compareTo(key) != 0) {
			y = x;
			if(x.getKey().compareTo(key) > 0) {
				x = x.getLeftChild();
			}
			else {
				x = x.getRightChild();
			}
		}
		if(x.getKey().compareTo(key) == 0) {
			entry = new MyEntry<T, V>(x.getKey(), x.getValue());
		}
		else if(x.getKey() == null) {
			if(key.compareTo(y.getKey()) < 0) {
				x = y;
			}
			else {
				x = getSuccessor(y);
			}
		}
		entry = new MyEntry<T, V>(x.getKey(), x.getValue());
		return entry;
	}

	
	@Override
	public T ceilingKey(T key) {
		// TODO Auto-generated method stub
		return ceilingEntry(key) == null ? null : ceilingEntry(key).getKey();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		tree = new RedBlackTree<T, V>();
		size = 0;
	}

	@Override
	public boolean containsKey(T key) {
		// TODO Auto-generated method stub
		if(key == null) throw new RuntimeErrorException(null);
		return tree.contains(key);
	}

	@Override
	public boolean containsValue(V value) {
		// TODO Auto-generated method stub
		Collection<V> values = values();
		if(value == null) throw new RuntimeErrorException(null);
		return values.contains(value);
	}

	@Override
	public Set<Entry<T, V>> entrySet() {
		// TODO Auto-generated method stub
		set.clear();
		if(tree.getRoot().isNull()) return set;
		return inorderSet(tree.getRoot());
	}

	
	@Override
	public Entry<T, V> firstEntry() {
		// TODO Auto-generated method stub
		INode<T, V> x = tree.getRoot();
		INode<T, V> y = tree.getRoot();
		if(x.isNull()) return null;
		while(!x.isNull()) {
			y = x;
			x = x.getLeftChild();
		}
		Entry<T, V> entry = new MyEntry<T, V>(y.getKey(), y.getValue());
		return entry;
	}

	@Override
	public T firstKey() {
		// TODO Auto-generated method stub
		if (firstEntry() == null) return null;
		return firstEntry().getKey();
	}

	@Override
	public Entry<T, V> floorEntry(T key) {
		// TODO Auto-generated method stub
		if(key == null) throw new RuntimeErrorException(null);
		INode<T, V> x = tree.getRoot();
		INode<T, V> y = tree.getRoot();
		Map.Entry<T, V> entry;
		while(x.getKey() != null && x.getKey().compareTo(key) != 0) {
			y = x;
			if(x.getKey().compareTo(key) > 0) {
				x = x.getLeftChild();
			}
			else {
				x = x.getRightChild();
			}
		}
		if(x.getKey() != null && x.getKey().compareTo(key) == 0) {
			entry = new MyEntry<T, V>(x.getKey(), x.getValue());
		}
		else if(x.getKey() == null) {
			if(y.getKey().compareTo(key) > 0) {
				x = predecessor(y);
			}
			else {
				x = y;
			}
		}
		entry = new MyEntry<T, V>(x.getKey(), x.getValue());
		return entry;
	}

	
	@Override
	public T floorKey(T key) {
		// TODO Auto-generated method stub
		return floorEntry(key).getKey();
	}

	@Override
	public V get(T key) {
		// TODO Auto-generated method stub
		return tree.search(key);
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey) {
		// TODO Auto-generated method stub
		ArrayList<Entry<T, V>> result = new ArrayList<>();
		Set<Entry<T, V>> set = entrySet();
		for(Entry<T, V> e : set) {
			if (e.getKey().compareTo(toKey) < 0) result.add(e);
		}
		return result;
	}

	@Override
	public ArrayList<Entry<T, V>> headMap(T toKey, boolean inclusive) {
		// TODO Auto-generated method stub
		ArrayList<Entry<T, V>> result = new ArrayList<>();
		Set<Entry<T, V>> set = entrySet();
		for(Entry<T, V> e : set) {
			if (e.getKey().compareTo(toKey) < 0 || (e.getKey().compareTo(toKey) == 0 && inclusive)) result.add(e);
		}
		return result;
	}

	@Override
	public Set<T> keySet() {
		// TODO Auto-generated method stub
		Set<Entry<T, V>> set = entrySet();
		Set<T> keys = new LinkedHashSet<>();
		for(Entry<T, V> e : set) {
			keys.add(e.getKey());
		}
		return keys;
	}

	@Override
	public Entry<T, V> lastEntry() {
		// TODO Auto-generated method stub
		INode<T, V> x = tree.getRoot();
		INode<T, V> y = tree.getRoot();
		if(x.isNull()) return null;
		while(!x.isNull()) {
			y = x;
			x = x.getRightChild();
		}
		Entry<T, V> entry = new MyEntry<T, V>(y.getKey(), y.getValue());
		return entry;
	}

	@Override
	public T lastKey() {
		// TODO Auto-generated method stub
		entrySet();
		if(lastEntry() == null) return null;
		return lastEntry().getKey();
	}

	@Override
	public Entry<T, V> pollFirstEntry() {
		// TODO Auto-generated method stub
		if(firstEntry() == null) return null;
		Entry<T, V> firstEntry = firstEntry();
		remove(firstEntry.getKey());
		return firstEntry;
	}

	@Override
	public Entry<T, V> pollLastEntry() {
		// TODO Auto-generated method stub
		if(lastEntry() == null) return null;
		Entry<T, V> lastEntry = lastEntry();
		remove(lastEntry.getKey());
		return lastEntry;
	}

	@Override
	public void put(T key, V value) {
		// TODO Auto-generated method stub
		if(containsKey(key)) {
			remove(key);
		}
		tree.insert(key, value);
		size++;
	}

	@Override
	public void putAll(Map<T, V> map) {
		// TODO Auto-generated method stub
		if (map == null) throw new RuntimeErrorException(null);
		size += map.size();
		for(Entry<T, V> entry : map.entrySet()) {
			tree.insert(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public boolean remove(T key) {
		// TODO Auto-generated method stub
		if(key == null) throw new RuntimeErrorException(null);
		size--;
		return tree.delete(key);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return size;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		Set<Entry<T, V>> set = entrySet();
		Collection<V> values = new LinkedHashSet<>();
		for(Entry<T, V> e : set) {
			values.add(e.getValue());
		}
		return values;
	}
	private INode<T, V> getSuccessor(INode<T, V> x) {
		// TODO Auto-generated method stub
		if(x.getRightChild().getKey() != null) {
			while(x.getLeftChild().getKey() != null) {
				x = x.getLeftChild();
			}
			return x;
		}
		INode<T, V> y = x.getParent();
		while(y.getKey() != null && x == y.getRightChild()) {
			x = y;
			y = x.getParent();
		}
		return y;
	}
	
	private INode<T, V> predecessor(INode<T, V> x) {
		// TODO Auto-generated method stub
		if(x.getLeftChild().getKey() != null) {
			x = x.getLeftChild();
			while(x.getRightChild() != null) {
				x = x.getRightChild();
			}
			return x;
		}
		INode<T, V> y = x.getParent();
		while(y.getKey() != null && x == y.getLeftChild()) {
			x = y;
			y = x.getParent();
		}
		return y;
}
	
	private Set<Entry<T, V>> inorderSet(INode<T, V> node) {
		// TODO Auto-generated method stub
		if (node.isNull()) return set;
		set = inorderSet(node.getLeftChild());
		Entry<T, V> entry = new MyEntry<T, V>(node.getKey(), node.getValue());
		set.add(entry);
		set = inorderSet(node.getRightChild());
		return set;
	}
}
