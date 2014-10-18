/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.abstractions;

import craterdog.collections.interfaces.Ordered;
import craterdog.collections.primitives.RandomizedTree;
import java.util.Arrays;
import java.util.Comparator;


/**
 * This abstract class defines the invariant methods that all ordered collections must inherit.
 * An ordered collection automatically orders its elements based on the comparison function
 * implemented by a specified <code>Comparator</code>.  If no comparator is specified, the
 * elements extend the <code>Comparable</code> interface.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by this collection.
 */
public abstract class OrderedCollection<E> extends Collection<E> implements Ordered<E> {

    private final RandomizedTree<E> tree;


    /**
     * This constructor creates a new empty collection with no comparator function.
     *
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     */
    protected OrderedCollection(boolean duplicatesAllowed) {
        tree = new RandomizedTree<>(duplicatesAllowed);
    }


    /**
     * This constructor creates a new collection with no comparator function and seeds
     * it with the elements from the specified array.
     *
     * @param elements The elements to be used to seed the new collection.
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     */
    protected OrderedCollection(E[] elements, boolean duplicatesAllowed) {
        this(elements, duplicatesAllowed, null);
    }


    /**
     * This constructor creates a new collection with no comparator function and seeds
     * it with the elements from the specified collection.
     *
     * @param elements The elements to be used to seed the new collection.
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     */
    protected OrderedCollection(Iterable<? extends E> elements, boolean duplicatesAllowed) {
        this(elements, duplicatesAllowed, null);
    }


    /**
     * This constructor creates a new collection with the specified comparator function.
     *
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    protected OrderedCollection(boolean duplicatesAllowed, Comparator<? super E> comparator) {
        tree = new RandomizedTree<>(duplicatesAllowed, comparator);
    }


    /**
     * This constructor creates a new collection with the specified comparator function and seeds
     * it with the elements from the specified array.
     *
     * @param elements The elements to be used to seed the new collection.
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    protected OrderedCollection(E[] elements, boolean duplicatesAllowed, Comparator<? super E> comparator) {
        tree = new RandomizedTree<>(duplicatesAllowed, comparator);
        tree.addAll(Arrays.asList(elements));
    }


    /**
     * This constructor creates a new collection with the specified comparator function and seeds
     * it with the elements from the specified collection.
     *
     * @param elements The elements to be used to seed the new collection.
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    protected OrderedCollection(Iterable<? extends E> elements, boolean duplicatesAllowed, Comparator<? super E> comparator) {
        tree = new RandomizedTree<>(duplicatesAllowed, comparator);
        for (E element : elements) {
            tree.add(element);
        }
    }


    @Override
    public final int getNumberOfElements() {
        return tree.size();
    }


    @Override
    public final boolean containsElement(E element) {
        boolean result = tree.contains(element);
        return result;
    }


    @Override
    public final Iterator<E> createDefaultIterator() {
        Iterator<E> iterator = new OrderedIterator();
        return iterator;
    }


    @Override
    public final boolean addElement(E element) {
        return tree.add(element);
    }


    @Override
    public final boolean removeElement(E element) {
        return tree.remove(element);
    }


    @Override
    public final void removeAllElements() {
        tree.clear();
    }


    /*
     * This iterator class implements the <code>Iterator</code> abstraction  It utilizes the
     * underlying tree iterators from the underlying tree implementation.  Like most iterators,
     * it should be used to access a collection exclusively without any other requests, especially
     * requests that change the length of the collection, being made directly on the same collection.
     */
    private class OrderedIterator extends Iterator<E> {

        private java.util.ListIterator<E> iterator = tree.iterator();

        @Override
        public void goToStart() {
            iterator = tree.iterator();
        }

        @Override
        public void goToIndex(int index) {
            index = normalizedIndex(index);
            iterator = tree.iterator(index - 1);
        }

        @Override
        public void goToEnd() {
            iterator = tree.iterator(tree.size());
        }

        @Override
        public boolean hasPreviousElement() {
            return iterator.nextIndex() > 0;
        }

        @Override
        public boolean hasNextElement() {
            return iterator.nextIndex() < tree.size();
        }

        @Override
        public E getNextElement() {
            if (!hasNextElement()) throw new IllegalStateException("The iterator is at the end of the ordered collection.");
            return iterator.next();
        }

        @Override
        public E getPreviousElement() {
            if (!hasPreviousElement()) throw new IllegalStateException("The iterator is at the beginning of the ordered collection.");
            return iterator.previous();
        }

    }

}

