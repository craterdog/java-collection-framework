/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.primitives;

import java.lang.reflect.Array;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import org.apache.commons.lang.math.RandomUtils;


/**
 * This class provides an implementation of the randomized binary search tree (RBST). The approach
 * was defined by CONRADO MART ́INEZ And SALVADOR ROURA in their paper published in Journal of
 * the ACM, Vol. 45, No. 2, March 1998, pp. 288–323.  It implements a standard binary search tree
 * but uses random choices to keep the tree statically balanced after each insertion or deletion.
 * All nodes in the tree have an equal probability of being the root node.  This implementation is
 * configurable such that it allows the programmer to specify whether or not duplicate entities are
 * allowed in the tree.  Since this is a binary tree it implements an ordered collection.
 *
 *
 * @author Derk Norton
 * @param <E> The type of the elements in the tree.
 */
public final class RandomizedTree<E> extends AbstractCollection<E> implements Cloneable {

    private boolean duplicatesAllowed;
    private Comparator<? super E> comparator;
    private TreeNode root;

    // index values used with subtree arrays containing the left and right subtrees
    static private final int LEFT = 0;
    static private final int RIGHT = 1;


    /**
     * This default constructor creates an instance of a tree that does not allow duplicate
     * elements and uses the default comparison mechanism.
     */
    public RandomizedTree() {
        this(false, null);
    }


    /**
     * This constructor creates an instance of a tree that uses the default comparison
     * mechanism and allows the caller to specify whether duplicates are allowed.
     *
     * @param duplicatesAllowed Whether or not duplicate elements are allowed in the tree.
     */
    public RandomizedTree(boolean duplicatesAllowed) {
        this(duplicatesAllowed, null);
    }


    /**
     * This constructor creates an instance of a tree that does not allow duplicate elements
     * and uses the specified comparator for ordering the elements in the tree.
     *
     * @param comparator The comparator to be used to order the elements in the tree.
     */
    public RandomizedTree(Comparator<? super E> comparator) {
        this(false, comparator);
    }


    /**
     * This constructor creates an instance of a tree that uses the specified comparator
     * for ordering the elements in the tree and allows the caller to specify whether
     * duplicates are allowed.
     *
     * @param duplicatesAllowed Whether or not duplicate elements are allowed in the tree.
     * @param comparator The comparator to be used to order the elements in the tree.
     */
    public RandomizedTree(boolean duplicatesAllowed, Comparator<? super E> comparator) {
        super();
        this.duplicatesAllowed = duplicatesAllowed;
        this.comparator = comparator;
        this.root = null;
    }


    /**
     * This constructor creates an instance of a tree that does not allow duplicate
     * elements and uses the default comparison mechanism.  The specified elements
     * are used to seed the tree.
     *
     * @param elements The elements that should be used to seed the tree.
     */
    public RandomizedTree(Collection<? extends E> elements) {
        this(elements, false, null);
    }


    /**
     * This constructor creates an instance of a tree that uses the default comparison
     * mechanism and allows the caller to specify whether duplicates are allowed.  The
     * specified elements are used to seed the tree.
     *
     * @param elements The elements that should be used to seed the tree.
     * @param duplicatesAllowed Whether or not duplicate elements are allowed in the tree.
     */
    public RandomizedTree(Collection<? extends E> elements, boolean duplicatesAllowed) {
        this(elements, duplicatesAllowed, null);
    }


    /**
     * This constructor creates an instance of a tree that does not allow duplicate elements
     * and uses the specified comparator for ordering the elements in the tree.  The specified
     * elements are used to seed the tree.
     *
     * @param elements The elements that should be used to seed the tree.
     * @param comparator The comparator to be used to order the elements in the tree.
     */
    public RandomizedTree(Collection<? extends E> elements, Comparator<? super E> comparator) {
        this(elements, false, comparator);
    }


    /**
     * This constructor creates an instance of a tree that uses the specified comparator
     * for ordering the elements in the tree and allows the caller to specify whether
     * duplicates are allowed.  The specified elements are used to seed the tree.
     *
     * @param elements The elements that should be used to seed the tree.
     * @param duplicatesAllowed Whether or not duplicate elements are allowed in the tree.
     * @param comparator The comparator to be used to order the elements in the tree.
     */
    public RandomizedTree(Collection<? extends E> elements, boolean duplicatesAllowed, Comparator<? super E> comparator) {
        this(duplicatesAllowed, comparator);
        for (E element : elements) {
            add(element);
        }
    }


    @Override
    public int size() {
        if (root == null) return 0;
        return root.size;
    }


    @Override
    public boolean contains(Object element) {
        @SuppressWarnings("unchecked")
        int index = indexOf((E) element);
        boolean result = index > -1;
        return result;
    }

    /**
     * This method returns the element with the specified index.
     *
     * @param index The index of the element to be returned.
     * @return The element at the specified index.
     */
    public E get(int index) {
        // starting at the root, search for the node with the specified index
        TreeNode node = findNode(root, index);
        return node.element;
    }


    /**
     * This method returns the index of the specified element or -1 if the element
     * does not exist in the collection.
     *
     * @param element The element to be searched for.
     * @return The index of the element or -1 if it was not found.
     */
    public int indexOf(E element) {
        // check for empty tree
        if (root == null) return -1;

        // startng at the root, work our way down comparing each node
        TreeNode currentTree = root;
        int index = currentTree.getLeftSubtreeSize();
        while (true) {
            int comparison = compareElements(element, currentTree.element);
            if (comparison == 0) {
                // found it
                return index;
            } else if (comparison < 0) {
                // travel down the left subtree
                currentTree = currentTree.left;
                if (currentTree == null) break;  // its not in the tree
                index -= 1 + currentTree.getRightSubtreeSize();
            } else {
                // travel down the right subtree
                currentTree = currentTree.right;
                if (currentTree == null) break;  // its not in the tree
                index += 1 + currentTree.getLeftSubtreeSize();
            }
        }
        // didn't find it
        return -1;
    }


    @Override
    public boolean add(E newElement) {
        // record the current size of the tree
        int sizeBefore = size();
        // starting at the root, look for a place to insert the new node
        root = insertNode(root, newElement);  // slight chance the root will change to the new node
        // see if the size of the tree changed to determine if an element was actually added
        int sizeAfter = size();
        boolean wasAdded = sizeAfter > sizeBefore;
        return wasAdded;
    }


    @Override
    public boolean remove(Object oldElement) {
        @SuppressWarnings("unchecked")
        E element = (E) oldElement;
        // record the current size of the tree
        int sizeBefore = size();
        // starting at the root, look for the element to be deleted
        root = deleteNode(root, element);
        // see if the size of the tree changed to determine if an element was actually removed
        int sizeAfter = size();
        boolean wasRemoved = sizeAfter < sizeBefore;
        return wasRemoved;
    }


    @Override
    public void clear() {
        root = null;
    }


    @Override
    public ListIterator<E> iterator() {
        return new TreeIterator();
    }


    /**
     * This method returns an iterator for the collection which is currently pointing
     * at the slot right before the first element.
     *
     * @return A list iterator pointing at the slot before the first element.
     */
    public ListIterator<E> listIterator() {
        return new TreeIterator();
    }


    /**
     * This method returns an iterator for the collection which is currently pointing
     * at the slot right before the specified index.
     *
     * @param index The index before the next element in the collection to be returned by the iterator.
     * @return A list iterator pointing at the slot before the element referenced by the specified index.
     */
    public ListIterator<E> listIterator(int index) {
        return new TreeIterator(index);
    }


    @Override
    // NOTE: Only ordered collections whose elements are in the same order will be equal.
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Collection)) return false;
        Collection<?> that = (Collection<?>) object;
        if (this.size() != that.size()) return false;
        Iterator<E> e1 = this.iterator();
        Iterator<?> e2 = that.iterator();
        while(e1.hasNext()) {
            E element1 = e1.next();
            Object element2 = e2.next();
            if (!(element1 == null ? element2 == null : element1.equals(element2))) return false;
        }
        return true;
    }


    @Override
    // NOTE: Only ordered collections whose elements are in the same order will have equal hash codes.
    public int hashCode() {
        int hashCode = 1;
        for (E element : this)
            hashCode = 31 * hashCode + (element == null ? 0 : element.hashCode());
        return hashCode;
    }


    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            RandomizedTree<E> copy = (RandomizedTree<E>) super.clone();
            copy.duplicatesAllowed = this.duplicatesAllowed;
            copy.comparator = this.comparator;
            copy.root = null;
            for (E element : this) {
                copy.add(element);
            }
            return copy;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError();
        }
    }


    private int compareElements(E firstElement, E secondElement) {
        int comparison;
        if (comparator != null) {
            comparison = comparator.compare(firstElement, secondElement);
        } else {
            @SuppressWarnings("unchecked")
            Comparable<E> comparable = (Comparable<E>) firstElement;  // may throw ClassCastException
            comparison = comparable.compareTo(secondElement);
        }
        return comparison;
    }


    private TreeNode findNode(TreeNode tree, int index) {
        int leftSubtreeSize = tree.getLeftSubtreeSize();
        if (index < leftSubtreeSize) {
            // recursively search the left subtree
            return findNode(tree.left, index);
        } else if (index > leftSubtreeSize) {
            // recursively search the right subtree
            return findNode(tree.right, index - leftSubtreeSize - 1);
        } else {
            // the current node has the specified index
            return tree;
        }
    }


    private TreeNode insertNode(TreeNode tree, E newElement) {
        // handle a null tree
        if (tree == null) return new TreeNode(newElement);

        // randomly insert the new node here with a probability of 1 / (treeSize + 1)
        int random = RandomUtils.nextInt(tree.size + 1);
        if (random == 0) {
            // split the current tree into two subtrees and make them the children of the new node
            TreeNode[] subtrees = splitTree(tree, newElement);
            TreeNode newNode = new TreeNode(newElement);
            newNode.setLeftSubtree(subtrees[LEFT]);
            newNode.setRightSubtree(subtrees[RIGHT]);
            return newNode;
        }

        // recursively look for a place to insert the new node
        int comparison = compareElements(newElement, tree.element);
        if (comparison == 0 && !duplicatesAllowed) {
            // when duplicates are not allowed we need to push a matching node further down in the tree to maintain random ordering
            tree = pushTreeDown(tree);
        } else if (comparison < 0) {
            // insert the new node somewhere in the left subtree
            tree.setLeftSubtree(insertNode(tree.left, newElement));
        } else {
            // insert the new node somewhere in the right subtree
            tree.setRightSubtree(insertNode(tree.right, newElement));
        }

        // return the new root (there is a small chance it may be the new node)
        return tree;
    }


    private TreeNode[] splitTree(TreeNode tree, E value) {
        @SuppressWarnings("unchecked")
        TreeNode[] subtrees = (TreeNode[]) Array.newInstance(root.getClass(), 2);

        // handle when currentTree is null
        if (tree == null) return subtrees;

        int comparison = compareElements(value, tree.element);
        if (comparison == 0 && !duplicatesAllowed) {
            // since duplicates are not allowed we need to remove the matching node and split its children
            subtrees[LEFT] = tree.left;
            subtrees[RIGHT] = tree.right;
        } else if (comparison < 0) {
            // recursively split between the tree and its left subtree
            subtrees[RIGHT] = tree;
            TreeNode[] leftSubtrees = splitTree(tree.left, value);
            subtrees[LEFT] = leftSubtrees[LEFT];
            subtrees[RIGHT].setLeftSubtree(leftSubtrees[RIGHT]);
        } else {
            // recursively split between the tree and its right subtree
            subtrees[LEFT] = tree;
            TreeNode[] rightSubtrees = splitTree(tree.right, value);
            subtrees[RIGHT] = rightSubtrees[RIGHT];
            subtrees[LEFT].setRightSubtree(rightSubtrees[LEFT]);
        }

        return subtrees;
    }


    private TreeNode pushTreeDown(TreeNode tree) {
        // calculate the total number of nodes in the tree
        int leftSubtreeSize = tree.getLeftSubtreeSize();
        int rightSubtreeSize = tree.getRightSubtreeSize();
        int totalSize = leftSubtreeSize + rightSubtreeSize + 1;

        // randomly select the new position of the root node
        int random = RandomUtils.nextInt(totalSize);
        if (random < leftSubtreeSize) {  // with a probability of leftSubtreeSize / totalSize
            // rotate node right
            TreeNode newTree = tree.left;
            tree.setLeftSubtree(newTree.right);
            newTree.setRightSubtree(tree);
            return newTree;
        } else if (random < totalSize - 1) {  // with a probability of rightSubtreeSize / totalSize
            // rotate node left
            TreeNode newTree = tree.right;
            tree.setRightSubtree(newTree.left);
            newTree.setLeftSubtree(tree);
            return newTree;
        } else {  // random == totalSize with a probability of 1 / totalSize
            // leave node where it is
            return tree;
        }
    }


    private TreeNode deleteNode(TreeNode tree, E oldElement) {
        // handle the case when the current tree is null
        if (tree == null) return null;

        TreeNode leftSubtree = tree.left;
        TreeNode rightSubtree = tree.right;

        // search for the element to be deleted
        int comparison = compareElements(oldElement, tree.element);
        if (comparison == 0) {
            // found the element, remove it by joining its two children
            tree = joinSubtrees(leftSubtree, rightSubtree);
        } else if (comparison < 0) {
            // recursively search for it in the left subtree
            tree.setLeftSubtree(deleteNode(leftSubtree, oldElement));
        } else {
            // recursively search for it in the right subtree
            tree.setRightSubtree(deleteNode(rightSubtree, oldElement));
        }

        // return the new root
        return tree;
    }


    private TreeNode joinSubtrees(TreeNode leftSubtree, TreeNode rightSubtree) {
        // handle when the subtrees are null
        if (leftSubtree == null) return rightSubtree;
        if (rightSubtree == null) return leftSubtree;

        // calculate the total number of child nodes in the two subtrees
        int leftSubtreeSize = leftSubtree.size;
        int rightSubtreeSize = rightSubtree.size;
        int totalSize = leftSubtreeSize + rightSubtreeSize;

        // randomly select the new root of the joined subtrees
        int random = RandomUtils.nextInt(totalSize);
        if (random < leftSubtreeSize) {  // with a probability of leftSubtreeSize / totalSize
            // join the right branch of the left subtree with the right subtree
            leftSubtree.setRightSubtree(joinSubtrees(leftSubtree.right, rightSubtree));
            return leftSubtree;
        } else {  // with a probability of rightSubtreeSize / totalSize
            // join the left subtree with the left branch of the right subtree
            rightSubtree.setLeftSubtree(joinSubtrees(leftSubtree, rightSubtree.left));
            return rightSubtree;
        }
    }


    private final class TreeIterator implements ListIterator<E> {

        final int size;
        final TreeNode[] nodes;
        int index;

        @SuppressWarnings("unchecked")
        private TreeIterator() {
            if (root != null) {
                this.size = root.size;
                this.nodes = (TreeNode[]) Array.newInstance(root.getClass(), size);
            } else {
                this.size = 0;
                this.nodes = null;
            }
            this.index = 0;
        }

        @SuppressWarnings("unchecked")
        private TreeIterator(int index) {
            if (root != null) {
                this.size = root.size;
                this.nodes = (TreeNode[]) Array.newInstance(root.getClass(), size);
            } else {
                this.size = 0;
                this.nodes = null;
            }
            this.index = index;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }

        @Override
        public int nextIndex() {
            return index;
        }

        @Override
        public E next() {
            TreeNode nextNode = nodes[index];
            if (nextNode == null) {
                nextNode = findNode(root, index);
                nodes[index] = nextNode;
            }
            index++;
            return nextNode.element;
        }

        @Override
        public boolean hasPrevious() {
            return index > 0;
        }

        @Override
        public int previousIndex() {
            return index - 1;
        }

        @Override
        public E previous() {
            index--;
            TreeNode previousNode = nodes[index];
            if (previousNode == null) {
                previousNode = findNode(root, index);
                nodes[index] = previousNode;
            }
            return previousNode.element;
        }

        @Override
        public void add(E element) {
            throw new UnsupportedOperationException("Inserting an element in a specific position in an ordered collection is not allowed.");
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Removing an element from a specific position in an ordered collection is not allowed.");
        }

        @Override
        public void set(E element) {
            throw new UnsupportedOperationException("Removing an element from a specific position in an ordered collection is not allowed.");
        }

    }


    private class TreeNode {

        E element;
        int size;
        TreeNode left;
        TreeNode right;

        TreeNode(E element) {
            this.element = element;
            size = 1;
        }

        int getLeftSubtreeSize() {
            if (left == null) return 0;
            return left.size;
        }

        void setLeftSubtree(TreeNode newLeftSubtree) {
            left = newLeftSubtree;
            resize();
        }

        int getRightSubtreeSize() {
            if (right == null) return 0;
            return right.size;
        }

        void setRightSubtree(TreeNode newRightSubtree) {
            right = newRightSubtree;
            resize();
        }

        void resize() {
            size = 1;
            if (left != null) size += left.size;
            if (right != null) size += right.size;
        }

    }

}
