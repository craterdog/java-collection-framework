/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.interfaces;

import craterdog.collections.abstractions.Iterator;


/**
 * This interface defines the methods that must be implemented by each collection
 * whose elements can be iterated over.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface Accessible<E> {

    /**
     * This method creates a new default iterator for the collection.
     *
     * @return A new default iterator for the collection.
     */
    Iterator<E> createDefaultIterator();

    /**
     * This method determines if an element is contained in the collection.
     *
     * @param element The element to be checked for in the collection.
     * @return Whether or not the specified element is contained in the collection.
     */
    boolean containsElement(E element);

    /**
     * This method determines whether any of the specified elements are contained in
     * the collection.
     *
     * @param elements The elements to be checked for in the collection.
     * @return Whether or not any of the specified elements are contained in the collection.
     */
    boolean containsAnyElementsIn(Iterable<? extends E> elements);

    /**
     * This method determines whether all of the specified elements are contained in
     * the collection.
     *
     * @param elements The elements to be checked for in the collection.
     * @return Whether or not all of the specified elements are contained in the collection.
     */
    boolean containsAllElementsIn(Iterable<? extends E> elements);

    /**
     * This method return the index of the specified element, or zero if the element
     * is not found.
     *
     * @param element The element to be checked for in the collection.
     * @return The index of the element of zero if the element was not found.
     */
    int getIndexOfElement(E element);

    /**
     * This method returns the element at the specified index.
     *
     * @param index The index of the element to be returned.
     * @return The element at the specified index.
     */
    E getElementAtIndex(int index);

    /**
     * This method returns a collection of the elements in the specified index range.
     *
     * @param firstIndex The index of the first element to be returned.
     * @param lastIndex the index of the last element to be returned.
     * @return A collection of elements in the specified range.
     */
    Accessible<E> getElementsInRange(int firstIndex, int lastIndex);

    /**
     * This method removes all elements from the collection.
     */
    void removeAllElements();

}
