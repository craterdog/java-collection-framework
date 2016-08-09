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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines the invariant methods that all collections must inherit.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class Collection<E> extends Sequence<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Collection.class);


    /**
     * This method return the index of the specified element, or zero if the element
     * is not found.
     *
     * @param element The element to be checked for in the collection.
     * @return The index of the element of zero if the element was not found.
     */
    public abstract int getIndex(E element);


    /**
     * This method returns the element at the specified index.
     *
     * @param index The index of the element to be returned.
     * @return The element at the specified index.
     */
    public abstract E getElement(int index);


    /**
     * This method returns a collection of the elements in the specified index range.
     *
     * @param firstIndex The index of the first element to be returned.
     * @param lastIndex the index of the last element to be returned.
     * @return A collection of elements in the specified range.
     */
    public abstract Collection<E> getElements(int firstIndex, int lastIndex);


    /**
     * This method determines if an element is contained in the collection.
     *
     * @param element The element to be checked for in the collection.
     * @return Whether or not the specified element is contained in the collection.
     */
    public boolean containsElement(E element) {
        logger.entry(element);
        int index = getIndex(element);
        boolean result = index > 0;
        logger.exit(result);
        return result;
    }


    /**
     * This method determines whether any of the specified elements are contained in
     * the collection.
     *
     * @param elements The elements to be checked for in the collection.
     * @return Whether or not any of the specified elements are contained in the collection.
     */
    public boolean containsAny(Iterable<? extends E> elements) {
        logger.entry(elements);
        boolean result = false;
        for (E element : elements) {
            result = containsElement(element);
            if (result) break;
        }
        logger.exit(result);
        return result;
    }


    /**
     * This method determines whether all of the specified elements are contained in
     * the collection.
     *
     * @param elements The elements to be checked for in the collection.
     * @return Whether or not all of the specified elements are contained in the collection.
     */
    public boolean containsAll(Iterable<? extends E> elements) {
        logger.entry(elements);
        boolean result = false;
        for (E element : elements) {
            result = containsElement(element);
            if (!result) break;
        }
        logger.exit(result);
        return result;
    }


    /**
     * This method removes all elements from the collection.
     */
    public abstract void removeAll();


    /**
     * This method converts negative indexes into their corresponding positive indexes and
     * then checks to make sure the index is in the range [1..size].
     *
     * The mapping between indexes is as follows:
     * <pre>
     * Negative Indexes:   -N      -N + 1     -N + 2     -N + 3   ...   -1
     * Positive Indexes:    1         2          3          4     ...    N
     * </pre>
     *
     * @param index The index to be normalized.
     * @return The normalized [1..N] index.
     */
    protected final int normalizedIndex(int index) {
        int size = getSize();
        if (index < 0) index = index + size + 1;
        if (index < 1 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return index;
    }

}
