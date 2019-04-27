package eg.edu.alexu.csd.filestructure.redblacktree;

public class RedBlackTree <T extends Comparable<T>, V> implements IRedBlackTree<T, V> {

	private INode<T, V> root;
	private INode<T, V> nill;
	public RedBlackTree() {
		// TODO Auto-generated constructor stub
		nill.setColor(true);
		nill.setLeftChild(root);
		nill.setRightChild(root);
		
	}
	@Override
	public INode<T, V> getRoot() {
		// TODO Auto-generated method stub
		return this.root;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return this.root == this.nill;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		this.root = this.nill;
	}

	@Override
	public V search(T key) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public boolean contains(T key) {
		// TODO Auto-generated method stub
		
		return true;
			
		
	}

	@Override
	public void insert(T key, V value) {
		// TODO Auto-generated method stub
		//initialize inserted node
		INode<T, V> z = new Node<>();
		z.setValue(value);
		z.setKey(key);
		z.setLeftChild(this.nill);
		z.setRightChild(this.nill);
		//insert z in its place
		INode<T, V> y = this.nill;
		INode<T, V> x = this.root;
		while(x != this.nill) {
			y = x;
			if(z.getKey().compareTo(x.getKey()) < 0) {
				x = x.getLeftChild();
			}
			else {
				x = x.getRightChild();
			}
		}
		z.setParent(y);
		if(z.getKey().compareTo(y.getKey()) < 0) {
			y.setLeftChild(z);
		}
		else {
			y.setRightChild(z);
		}
		//fix tree after insertion
		fixTree(z);
	}
	

	@Override
	public boolean delete(T key) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void rotateLeft(INode<T, V> x) {
		// TODO Auto-generated method stub
		INode<T, V> y = x.getRightChild();
		x.setRightChild(y.getLeftChild());
		if(y.getLeftChild() != this.nill) {
			y.getLeftChild().setParent(x);
		}
		y.setParent(x.getParent());
		if(x.getParent() == this.root) {
			this.root = y;
		}
		else if(x == x.getParent().getLeftChild()) {
			x.getParent().setLeftChild(y);
		}
		else if(x == x.getParent().getRightChild()) {
			x.getParent().setRightChild(y);
		}
		y.setLeftChild(x);
		x.setParent(y);
	}
	
	private void rotateRight(INode<T, V> y) {
		// TODO Auto-generated method stub
		INode<T, V> x = y.getLeftChild();
		y.setLeftChild(x.getRightChild());
		if(x.getRightChild() != this.nill) {
			x.getRightChild().setParent(y);
		}
		x.setParent(y.getParent());
		if(y.getParent() == this.root) {
			this.root = x;
		}
		else if(y == y.getParent().getLeftChild()) {
			y.getParent().setLeftChild(x);
		}
		else if(y == y.getParent().getRightChild()) {
			y.getParent().setRightChild(x);
		}
		x.setRightChild(y);
		y.setParent(x);
	}

	private void fixTree(INode<T, V> z) {
		// TODO Auto-generated method stub
		
	}
}
