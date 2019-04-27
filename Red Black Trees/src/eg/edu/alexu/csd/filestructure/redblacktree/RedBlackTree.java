package eg.edu.alexu.csd.filestructure.redblacktree;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T, V> {

    private INode<T, V> root;
    private INode<T, V> nil;

    public RedBlackTree() {
        // TODO Auto-generated constructor stub
        nil = new Node<>();
        nil.setColor(true);
        nil.setLeftChild(nil);
        nil.setRightChild(nil);
        nil.setParent(nil);
        root = nil;
    }

    @Override
    public INode<T, V> getRoot() {
        // TODO Auto-generated method stub
        return this.root;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return this.root == this.nil;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        this.root = this.nil;
    }

    @Override
    public V search(T key) {
        // TODO Auto-generated method stub
        INode<T, V> x = root;
        while (x != nil && x.getKey().compareTo(key) != 0) {
            if (x.getKey().compareTo(key) > 0) x = x.getLeftChild();
            else x = x.getRightChild();
        }
        return x == nil ? null : x.getValue();
    }

    @Override
    public boolean contains(T key) {
        // TODO Auto-generated method stub
        return search(key) != null;
    }

    @Override
    public void insert(T key, V value) {
        // TODO Auto-generated method stub
        //initialize inserted node
        INode<T, V> z = new Node<>();
        z.setValue(value);
        z.setKey(key);
        z.setLeftChild(this.nil);
        z.setRightChild(this.nil);
        z.setColor(false);
        if (root == nil) {
            root = z;
            z.setColor(true);
            z.setParent(nil);
            return;
        }
        //insert z in its place
        INode<T, V> y = this.nil;
        INode<T, V> x = this.root;
        while (x != this.nil) {
            y = x;
            if (z.getKey().compareTo(x.getKey()) < 0) {
                x = x.getLeftChild();
            } else {
                x = x.getRightChild();
            }
        }
        z.setParent(y);
        if (z.getKey().compareTo(y.getKey()) < 0) {
            y.setLeftChild(z);
        } else {
            y.setRightChild(z);
        }
        insertionFixUp(z);
    }

    @Override
    public boolean delete(T key) {
        // TODO Auto-generated method stub
        if (!contains(key)) return false;
        INode<T, V> x = root;
        while (x.getKey().compareTo(key) != 0) {
            if (x.getKey().compareTo(key) > 0) x = x.getLeftChild();
            else x = x.getRightChild();
        }
        INode<T, V> child;
        boolean orgColor = x.getColor();
        if (x.getRightChild() == nil) {
            child = x.getLeftChild();
            transplant(x, child);
        } else if (x.getLeftChild() == nil) {
            child = x.getRightChild();
            transplant(x, child);
        } else {
            INode<T, V> y = min(x.getRightChild());
            orgColor = y.getColor();
            child = y.getRightChild();
            if (y.getParent() == x) child.setParent(y);
            else {
                transplant(y, child);
                y.setRightChild(x.getRightChild());
                y.getRightChild().setParent(y);
            }
            transplant(x, y);
            y.setLeftChild(x.getLeftChild());
            y.getLeftChild().setParent(y);
        }
        if (orgColor) deletionFixUp(child);
        return true;
    }

    private INode<T, V> min(INode<T, V> x) {
        while (x.getLeftChild() != nil) x = x.getLeftChild();
        return x;
    }

    private void deletionFixUp(INode<T, V> x) {
        while (x != root && x.getColor()) {
            if (x == x.getParent().getLeftChild()) {
                INode<T, V> w = x.getParent().getRightChild();
                if (!w.getColor()) {
                    w.setColor(true);
                    x.getParent().setColor(false);
                    rotateLeft(x.getParent());
                    w = x.getParent().getRightChild();
                }
                if (w.getLeftChild().getColor() && w.getRightChild().getColor()) {
                    w.setColor(false);
                    x = x.getParent();
                    continue;
                } else if (w.getRightChild().getColor()) {
                    w.getLeftChild().setColor(true);
                    w.setColor(false);
                    rotateRight(w);
                    w = x.getParent().getRightChild();
                }
                if (!w.getRightChild().getColor()) {
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(true);
                    w.getRightChild().setColor(true);
                    rotateLeft(x.getParent());
                    x = root;
                }
            } else {
                INode<T, V> w = x.getParent().getRightChild();
                if (!w.getColor()) {
                    w.setColor(true);
                    x.getParent().setColor(false);
                    rotateRight(x.getParent());
                    w = x.getParent().getLeftChild();
                }
                if (w.getRightChild().getColor() && w.getLeftChild().getColor()) {
                    w.setColor(false);
                    x = x.getParent();
                    continue;
                } else if (w.getLeftChild().getColor()) {
                    w.getRightChild().setColor(true);
                    w.setColor(false);
                    rotateLeft(w);
                    w = x.getParent().getLeftChild();
                }
                if (!w.getLeftChild().getColor()) {
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(true);
                    w.getLeftChild().setColor(true);
                    rotateRight(x.getParent());
                    x = root;
                }
            }
        }
        x.setColor(true);
    }

    void transplant(INode<T, V> target, INode<T, V> with) {
        if (target.getParent() == nil) root = with;
        else if (target == target.getParent().getLeftChild()) target.getParent().setLeftChild(with);
        else target.getParent().setRightChild(with);
        with.setParent(target.getParent());
    }

    private void rotateLeft(INode<T, V> x) {
        // TODO Auto-generated method stub
        INode<T, V> y = x.getRightChild();
        y.setParent(x.getParent());
        if (x == root) root = y;
        else {
            if (x == x.getParent().getLeftChild()) x.getParent().setLeftChild(y);
            else x.getParent().setRightChild(y);
        }
        x.setParent(y);
        INode<T, V> yOrgLeft = y.getLeftChild();
        y.setLeftChild(x);
        x.setRightChild(yOrgLeft);
        if (yOrgLeft != nil) yOrgLeft.setParent(x);
    }

    private void rotateRight(INode<T, V> x) {
        // TODO Auto-generated method stub
        INode<T, V> y = x.getLeftChild();
        y.setParent(x.getParent());
        if (x == root) root = y;
        else {
            if (x == x.getParent().getLeftChild()) x.getParent().setLeftChild(y);
            else x.getParent().setRightChild(y);
        }
        x.setParent(y);
        INode<T, V> yOrgRight = y.getRightChild();
        y.setRightChild(x);
        x.setLeftChild(yOrgRight);
        if (yOrgRight != nil) yOrgRight.setParent(x);
    }

    private void insertionFixUp(INode<T, V> z) {
        // TODO Auto-generated method stub
        while (z.getParent().getColor() == false) {
            INode<T, V> uncle;
            if (z.getParent().getParent().getLeftChild() == z.getParent()) {
                uncle = z.getParent().getParent().getRightChild();
            } else {
                uncle = z.getParent().getParent().getLeftChild();
            }
            if (uncle.getColor() == false) {
                z.getParent().setColor(true);
                uncle.setColor(true);
                z.getParent().getParent().setColor(false);
                z = z.getParent().getParent();
            } else {
                // 4 cases : left left, left right, right right, right left
                if (z.getParent() == z.getParent().getParent().getLeftChild()) {
                    // left
                    if (z == z.getParent().getRightChild()) {
                        // left right
                        rotateLeft(z.getParent());
                        z = z.getLeftChild();
                    }
                    // left left
                    z.getParent().setColor(true);
                    z.getParent().getParent().setColor(false);
                    rotateRight(z.getParent().getParent());
                } else {
                    //right
                    if (z == z.getParent().getLeftChild()) {
                        // right left
                        rotateRight(z.getParent());
                        z = z.getRightChild();
                    } else {
                        // right right
                        z.getParent().setColor(true);
                        z.getParent().getParent().setColor(false);
                        rotateLeft(z.getParent().getParent());
                    }
                }
            }
        }
        root.setColor(true);
    }
}
