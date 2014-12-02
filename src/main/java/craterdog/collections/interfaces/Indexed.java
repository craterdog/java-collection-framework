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
 * its elements to be manipulated based on their indexes.  The indexing is unit based
 * and allows positive indexes starting at the beginning of the collection or negative
 * indexes starting at the end of the collection as follows:
 * <pre>
 *         1             2             3               N
 *    [element 1] . [element 2] . [element 3] ... [element N]
 *        -N          -(N-1)        -(N-2)            -1
 * </pre>
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface Indexed<E> extends Iteratable<E> {

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
    Indexed<E> getElementsInRange(int firstIndex, int lastIndex);

    /**
     * This method inserts the specified element into the collection before the element
     * associated with the specified index.
     *
     * @param element The new element to be inserted into the collection.
     * @param index The index of the element before which the new element is to be inserted.
     */
    void insertElementBeforeIndex(E element, int index);

    /**
     * This method inserts the specified elements into the collection before the element
     * associated with the specified index.  The new elements are inserted in the same
     * order as they appear in the specified list.
     *
     * @param elements The new elements to be inserted into the collection.
     * @param index The index of the element before which the new element is to be inserted.
     */
    void insertElementsBeforeIndex(Iterable<? extends E> elements, int index);

    /**
     * This method replaces an existing element in the collection with a new one.  The new
     * element replaces the existing element at the specified index.
     *
     * @param element The new element that will replace the existing one.
     * @param index The index of the existing element.
     *
     * @return The element that was at the specified index.
     */
    E replaceElementAtIndex(E element, int index);

    /**
     * This method removes from the collection the element associated with the specified
     * index.
     *
     * @param index The index of the element to be removed.
     * @return The element at the specified index.
     */
    E removeElementAtIndex(int index);

    /**
     * This method removes from the collection the elements associated with the specified
     * index range.
     *
     * @param firstIndex The index of the first element to be removed.
     * @param lastIndex The index of the last element to be removed.
     * @return The list of the elements that were removed from the collection.
     */
    Indexed<E> removeElementsInRange(int firstIndex, int lastIndex);

}
