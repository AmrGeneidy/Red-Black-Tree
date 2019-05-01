package eg.edu.alexu.csd.filestructure.redblacktree;

import javax.management.RuntimeErrorException;

public class RedBlackTree<T extends Comparable<T>, V> implements IRedBlackTree<T, V> {

    private INode<T, V> root;
    private INode<T, V> nil;

    RedBlackTree() {
        // TODO Auto-generated constructor stub
        nil = new Node<>();
        nil.setColor(INode.BLACK);
        nil.setLeftChild(null);
        nil.setRightChild(null);
        nil.setParent(null);
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
        if (key == null) throw new RuntimeErrorException(null);
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
        if (key == null || value == null) throw new RuntimeErrorException(null);
        //initialize inserted node
        INode<T, V> z = new Node<>();
        z.setValue(value);
        z.setKey(key);
        z.setLeftChild(this.nil);
        z.setRightChild(this.nil);
        z.setColor(INode.RED);
        if (root == nil) {
            root = z;
            z.setColor(INode.BLACK);
            z.setParent(nil);
            return;
        }
        //insert z in its place
        INode<T, V> follower = this.nil;
        INode<T, V> x = this.root;
        while (x != this.nil && x.getKey().compareTo(key) != 0) {
            follower = x;
            if (z.getKey().compareTo(x.getKey()) < 0) {
                x = x.getLeftChild();
            } else {
                x = x.getRightChild();
            }
        }
        if (x != nil && x.getKey().compareTo(key) == 0) {
            x.setValue(value);
            return;
        }
        z.setParent(follower);
        if (z.getKey().compareTo(follower.getKey()) < 0) {
            follower.setLeftChild(z);
        } else {
            follower.setRightChild(z);
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
            y.setColor(x.getColor());
            y.setLeftChild(x.getLeftChild());
            y.getLeftChild().setParent(y);
        }
        if (orgColor == INode.BLACK) deletionFixUp(child);
        return true;
    }

    private INode<T, V> min(INode<T, V> x) {
        while (x.getLeftChild() != nil) x = x.getLeftChild();
        return x;
    }

    private void deletionFixUp(INode<T, V> x) {
        while (x != root && x.getColor() == INode.BLACK) {
            if (x == x.getParent().getLeftChild()) {
                INode<T, V> w = x.getParent().getRightChild();
                if (w.getColor() == INode.RED) {
                    w.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    rotateLeft(x.getParent());
                    w = x.getParent().getRightChild();
                }

                if (w.getLeftChild().getColor() == INode.BLACK && w.getRightChild().getColor() == INode.BLACK) {
                    w.setColor(INode.RED);
                    x = x.getParent();
                    continue;
                } else if (w.getRightChild().getColor() == INode.BLACK) {
                    w.getLeftChild().setColor(INode.BLACK);
                    w.setColor(INode.RED);
                    rotateRight(w);
                    w = x.getParent().getRightChild();
                }
                if (w.getRightChild().getColor() == INode.RED) {
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(INode.BLACK);
                    w.getRightChild().setColor(INode.BLACK);
                    rotateLeft(x.getParent());
                    x = root;
                }
            } else {
                INode<T, V> w = x.getParent().getLeftChild();
                if (w.getColor() == INode.RED) {
                    w.setColor(INode.BLACK);
                    x.getParent().setColor(INode.RED);
                    rotateRight(x.getParent());
                    w = x.getParent().getLeftChild();
                }
                if (w.getRightChild().getColor() == INode.BLACK && w.getLeftChild().getColor() == INode.BLACK) {
                    w.setColor(INode.RED);
                    x = x.getParent();
                    continue;
                } else if (w.getLeftChild().getColor() == INode.BLACK) {
                    w.getRightChild().setColor(INode.BLACK);
                    w.setColor(INode.RED);
                    rotateLeft(w);
                    w = x.getParent().getLeftChild();
                }
                if (w.getLeftChild().getColor() == INode.RED) {
                    w.setColor(x.getParent().getColor());
                    x.getParent().setColor(INode.BLACK);
                    w.getLeftChild().setColor(INode.BLACK);
                    rotateRight(x.getParent());
                    x = root;
                }
            }
        }
        x.setColor(INode.BLACK);
    }

    private void transplant(INode<T, V> target, INode<T, V> with) {
        if (target.getParent() == nil) root = with;
        else if (target == target.getParent().getLeftChild()) target.getParent().setLeftChild(with);
        else target.getParent().setRightChild(with);
        with.setParent(target.getParent());
    }

    private void rotateLeft(INode<T, V> x) {
        // TODO Auto-generated method stub
        INode<T, V> rightChild = x.getRightChild();
        rightChild.setParent(x.getParent());
        if (x == root) root = rightChild;
        else {
            if (x == x.getParent().getLeftChild()) x.getParent().setLeftChild(rightChild);
            else x.getParent().setRightChild(rightChild);
        }
        x.setParent(rightChild);
        INode<T, V> yOrgLeft = rightChild.getLeftChild();
        rightChild.setLeftChild(x);
        x.setRightChild(yOrgLeft);
        if (yOrgLeft != nil) yOrgLeft.setParent(x);
    }

    private void rotateRight(INode<T, V> x) {
        // TODO Auto-generated method stub
        INode<T, V> leftChild = x.getLeftChild();
        leftChild.setParent(x.getParent());
        if (x == root) root = leftChild;
        else {
            if (x == x.getParent().getLeftChild()) x.getParent().setLeftChild(leftChild);
            else x.getParent().setRightChild(leftChild);
        }
        x.setParent(leftChild);
        INode<T, V> yOrgRight = leftChild.getRightChild();
        leftChild.setRightChild(x);
        x.setLeftChild(yOrgRight);
        if (yOrgRight != nil) yOrgRight.setParent(x);
    }

    private void insertionFixUp(INode<T, V> z) {
        // TODO Auto-generated method stub
        while (z.getParent().getColor() == INode.RED) {
            INode<T, V> uncle;
            if (z.getParent().getParent().getLeftChild() == z.getParent()) {
                uncle = z.getParent().getParent().getRightChild();
            } else {
                uncle = z.getParent().getParent().getLeftChild();
            }
            if (uncle.getColor() == INode.RED) {
                z.getParent().setColor(INode.BLACK);
                uncle.setColor(INode.BLACK);
                z.getParent().getParent().setColor(INode.RED);
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
                    z.getParent().setColor(INode.BLACK);
                    z.getParent().getParent().setColor(INode.RED);
                    rotateRight(z.getParent().getParent());
                } else {
                    //right
                    if (z == z.getParent().getLeftChild()) {
                        // right left
                        rotateRight(z.getParent());
                        z = z.getRightChild();
                    } else {
                        // right right
                        z.getParent().setColor(INode.BLACK);
                        z.getParent().getParent().setColor(INode.RED);
                        rotateLeft(z.getParent().getParent());
                    }
                }
            }
        }
        root.setColor(INode.BLACK);
    }
}
