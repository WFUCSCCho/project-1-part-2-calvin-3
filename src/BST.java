/*∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*
  @file: BST.java
  @description: This file is the generic BST implementation
  @author: Calvin Malaney
  @date: September 23, 2025
∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*/

import java.util.*;

public class BST<T extends Comparable<? super T>> implements Iterable<T> {
    private Node<T> root;
    private int size;

    // Implement the constructor
    public BST() {
        this.root = null;
    }

    // Implement the clear method
    public void clear() {
        root = null;
        size = 0;
    }

    // Implement the size method
    public int size() {
        return size;
    }

    // Implement the insert method
    public boolean add(T data) {
        if (data == null) throw new IllegalArgumentException("null value not allowed");
        int before = size;
        root = addRecursive(root, data);
        return size > before;
    }

    private Node<T> addRecursive(Node<T> current, T data) {
        if (current == null) {
            size++;
            return new Node<>(data);
        }
        int comparisonResult = data.compareTo(current.getValue());
        if (comparisonResult < 0) {
            current.setLeft(addRecursive(current.getLeft(), data));
        } else if (comparisonResult > 0) {
            current.setRight(addRecursive(current.getRight(), data));
        }
        // if comparisonResult = 0, it is a duplicate and will be ignored
        return current;
    }

    // Implement the remove method
    public Node<T> remove(T data) {
        if (data == null) return null;
        Wrapper<Node<T>> removed = new Wrapper<>();
        root = removeRecursive(root, data, removed);
        if (removed.value != null) {
            size--;
        }
        return removed.value;
    }

    private Node<T> removeRecursive(Node<T> current, T data, Wrapper<Node<T>> removed) {
        if (current == null) {
            return null; // not found
        }

        int comparisonResult = data.compareTo(current.getValue());

        if (comparisonResult < 0) {
            current.setLeft(removeRecursive(current.getLeft(), data, removed));
        } else if (comparisonResult > 0) {
            current.setRight(removeRecursive(current.getRight(), data, removed));
        } else {
            removed.value = new Node<>(current.getValue()); // capture what was removed
            // Case 1: no children
            if (current.getLeft() == null && current.getRight() == null) {
                return null;
            }
            // Case 2: only one child
            else if (current.getLeft() == null) {
                return current.getRight();
            } else if (current.getRight() == null) {
                return current.getLeft();
            }
            // Case 3: two children
            else {
                Node<T> successor = minNode(current.getRight());
                current.setValue(successor.getValue());
                current.setRight(removeRecursive(current.getRight(), successor.getValue(), new Wrapper<>()));
            }
        }
        return current;
    }

    // helper for min value - case 3
    private Node<T> minNode(Node<T> node) {
        Node<T> current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    // wrapper class - to return removed node through recursion
    private static final class Wrapper<X> {
        X value;
    }

    // Implement the search method
    public Node<T> search(T data) {
        if (data == null) return null;
        Node<T> current = root;
        while (current != null) {
            int comparisonResult = data.compareTo(current.getValue());
            if (comparisonResult == 0) return current;
            current = (comparisonResult < 0) ? current.getLeft() : current.getRight();
        }
        return null;
    }

    // Implement the iterator method
    public Iterator<T> iterator() {
        return new InOrderIterator(root);
    }

    // Implement the BSTIterator class
    private final class InOrderIterator implements Iterator<T> {
        //stack
        private final Deque<Node<T>> stack = new ArrayDeque<>();

        // constructor
        InOrderIterator(Node<T> start) {
            pushLeft(start);
        }

        private void pushLeft(Node<T> n) {
            while (n != null) {
                stack.push(n);
                n = n.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public T next() {
            Node<T> n = stack.pop();
            pushLeft(n.getRight());
            return n.getValue();
        }
    }
}

