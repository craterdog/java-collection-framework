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

import craterdog.core.Iterator;
import craterdog.collections.Bag;
import craterdog.collections.interfaces.Ordered;
import craterdog.collections.primitives.RandomizedTree;
import java.util.Arrays;
import java.util.Comparator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines the invariant methods that all ordered collections must inherit.
 * An ordered collection automatically orders its elements based on the comparison function
 * implemented by a specified <code>Comparator</code>.  If no comparator is specified, the
 * elements extend the <code>Comparable</code> interface.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by this collection.
 */
public abstract class OrderedCollection<E> extends OpenCollection<E> implements Ordered<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(OrderedCollection.class);

    private final RandomizedTree<E> tree;


    /**
     * This constructor creates a new empty collection with no comparator function.
     *
     * @param duplicatesAllowed Whether or not duplicate elements are allowed.
     */
    protected OrderedCollection(boolean duplicatesAllowed) {
        logger.entry(duplicatesAllowed);
        tree = new RandomizedTree<>(duplicatesAllowed);
        logger.exit();
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
        logger.entry(duplicatesAllowed, comparator);
        tree = new RandomizedTree<>(duplicatesAllowed, comparator);
        logger.exit();
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
        logger.entry(elements, duplicatesAllowed, comparator);
        tree = new RandomizedTree<>(duplicatesAllowed, comparator);
        tree.addAll(Arrays.asList(elements));
        logger.exit();
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
        logger.entry(elements, duplicatesAllowed, comparator);
        tree = new RandomizedTree<>(duplicatesAllowed, comparator);
        for (E element : elements) {
            tree.add(element);
        }
        logger.exit();
    }


    @Override
    public Iterator<E> createIterator() {
        logger.entry();
        Iterator<E> iterator = new OrderedIterator();
        logger.exit(iterator);
        return iterator;
    }


    @Override
    public int getSize() {
        logger.entry();
        int result = tree.size();
        logger.exit(result);
        return result;
    }


    @Override
    public E getElement(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        E element = tree.get(index - 1);  // convert to zero based indexing
        logger.exit(element);
        return element;
    }


    @Override
    public int getIndex(E element) {
        logger.entry(element);
        int index = tree.indexOf(element) + 1;  // convert to ordinal based indexing
        logger.exit(index);
        return index;
    }


    @Override
    public OrderedCollection<E> getElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        Bag<E> result = new Bag<>();
        Iterator<E> iterator = createIterator();
        iterator.toIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            E element = iterator.getNext();
            logger.debug("Including element: {}", element);
            result.addElement(element);
        }
        logger.exit(result);
        return result;
    }


    @Override
    public boolean addElement(E element) {
        logger.entry(element);
        boolean result = tree.add(element);
        logger.exit(result);
        return result;
    }


    @Override
    public boolean removeElement(E element) {
        logger.entry(element);
        boolean result = tree.remove(element);
        logger.exit(result);
        return result;
    }


    @Override
    public void removeAll() {
        logger.entry();
        tree.clear();
        logger.exit();
    }


    @Override
    public Comparator<? super E> getComparator() {
        logger.entry();
        Comparator<? super E> comparator = tree.comparator();
        logger.exit(comparator);
        return comparator;
    }


    /*
     * This iterator class implements the <code>Iterator</code> abstraction  It utilizes the
     * underlying tree iterators from the underlying tree implementation.  Like most iterators,
     * it should be used to access a collection exclusively without any other requests, especially
     * requests that change the length of the collection, being made directly on the same collection.
     */
    private class OrderedIterator extends Iterator<E> {

        private java.util.ListIterator<E> iterator = tree.listIterator();

        @Override
        public void toStart() {
            iterator = tree.iterator();
        }

        @Override
        public void toIndex(int index) {
            index = normalizedIndex(index);
            iterator = tree.listIterator(index - 1);
        }

        @Override
        public void toEnd() {
            iterator = tree.listIterator(tree.size());
        }

        @Override
        public boolean hasPrevious() {
            return iterator.nextIndex() > 0;
        }

        @Override
        public boolean hasNext() {
            return iterator.nextIndex() < tree.size();
        }

        @Override
        public E getNext() {
            if (!hasNext()) throw new IllegalStateException("The iterator is at the end of the ordered collection.");
            return iterator.next();
        }

        @Override
        public E getPrevious() {
            if (!hasPrevious()) throw new IllegalStateException("The iterator is at the beginning of the ordered collection.");
            return iterator.previous();
        }

    }

}

