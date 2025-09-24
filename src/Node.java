/*∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*
  @file: Node.java
  @description: This file represents a single node in the generic BST
  @author: Calvin Malaney
  @date: September 18, 2025
∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗∗*/

public class Node<T extends Comparable<? super T>> implements Comparable<Node<T>> {
    T value;
    Node<T> left;
    Node<T> right;

    // Implement the constructor
    public Node(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Node value cannot be null");
        }
        this.value = value;
        this.left = null;
        this.right = null;
    }

    // Implement the setElement method
    public void setValue(T value) {
        this.value = value;
    }

    // Implement the setLeft method
    public void setLeft(Node<T> left) {
        this.left = left;
    }

    // Implement the setRight method
    public void setRight(Node<T> right) {
        this.right = right;
    }

    // Implement the getLeft method
    public Node<T> getLeft() {
        return left;
    }

    // Implement the getRight method
    public Node<T> getRight() {
        return right;
    }

    // Implement the getElement method
    public T getValue() {
        return value;
    }

    //comparable implementation (compares nodes by their values)
    @Override
    public int compareTo(Node<T> other) {
        return this.value.compareTo(other.value);
    }

    //toString method
    @Override
    public String toString() {
        return String.valueOf(value);
    }

    // Implement the isLeaf method
    public boolean isLeaf(Node<T> node) {
        if (node == null) return false;
        return node.getLeft() == null && node.getRight() == null;
    }

}