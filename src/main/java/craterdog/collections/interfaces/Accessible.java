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


/**
 * This interface defines the methods that must be implemented by each collection that
 * allows direct access to its elements.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface Accessible<E> extends Iteratable<E> {

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
     * This method adds a new element to the collection.  If the collection
     * is empty then the new element will be the first element in the collection.
     *
     * @param element The new element to be added.
     * @return Whether or not the element was actually added to the collection.
     */
    boolean addElement(E element);

    /**
     * This method adds an array of new elements to the collection.  The new
     * elements will be added in the order they appear in the specified array.
     *
     * @param elements The array of new elements to be added.
     * @return The number of elements that were actually added to the collection.
     */
    int addElements(E[] elements);

    /**
     * This method adds a list of new elements to the collection.  The new
     * elements will be added in the order they appear in the specified collection.
     *
     * @param elements The list of new elements to be added.
     * @return The number of elements that were actually added to the collection.
     */
    int addElements(Iterable<? extends E> elements);

    /**
     * This method removes the specified element from the collection.  If the element
     * is not found in the collection, no changes are made.
     *
     * @param element The element to be removed.
     * @return Whether or not the element was found.
     */
    boolean removeElement(E element);

    /**
     * This method removes the elements in the specified array from the collection.
     * The number of matching elements is returned.
     *
     * @param elements The array of elements to be removed from the collection.
     * @return The number of elements that were actually removed.
     */
    int removeElements(E[] elements);

    /** This method removes the specified elements from the collection.  The number of
     * matching elements is returned.
     *
     * @param elements The list of elements to be removed from the collection.
     * @return The number of elements that were actually removed.
     */
    int removeElements(Iterable<? extends E> elements);

}
