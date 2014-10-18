/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections;

import craterdog.collections.abstractions.*;
import java.util.Comparator;


/**
 * This collection class implements an ordered collection that does not allow duplicate elements.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public final class Set<E> extends OrderedCollection<E> {

    /**
     * This constructor creates a new empty set with no comparator function.
     */
    public Set() {
        super(false);
    }


    /**
     * This constructor creates a new set with no comparator function and seeds
     * it with the elements from the specified array.
     *
     * @param elements The elements to be used to seed the new set.
     */
    public Set(E[] elements) {
        this(elements, null);
    }


    /**
     * This constructor creates a new set with no comparator function and seeds
     * it with the elements from the specified collection.
     *
     * @param elements The elements to be used to seed the new set.
     */
    public Set(Iterable<? extends E> elements) {
        this(elements, null);
    }


    /**
     * This constructor creates a new set with the specified comparator function.
     *
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    public Set(Comparator<? super E> comparator) {
        super(false, comparator);
    }


    /**
     * This constructor creates a new set with the specified comparator function and seeds
     * it with the elements from the specified array.
     *
     * @param elements The elements to be used to seed the new set.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    public Set(E[] elements, Comparator<? super E> comparator) {
        super(false, comparator);
        addElements(elements);
    }


    /**
     * This constructor creates a new set with the specified comparator function and seeds
     * it with the elements from the specified collection.
     *
     * @param elements The elements to be used to seed the new set.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    public Set(Iterable<? extends E> elements, Comparator<? super E> comparator) {
        super(false, comparator);
        for (E element : elements) {
            addElement(element);
        }
    }


    /**
     * This function returns a new set that contains the elements that are in
     * both the first set and the second set.
     *
     * @param <E> The type of element contained in the sets.
     * @param set1 The first set to be operated on.
     * @param set2 The second set to be operated on.
     * @return The resulting set.
     */
    static public <E> Set<E> and(Set<E> set1, Set<E> set2) {
        Set<E> result = new Set<>();
        for (E element : set1) {
            if (set2.containsElement(element)) result.addElement(element);
        }
        return result;
    }


    /**
     * This function returns a new set that contains the elements that are in
     * the first set but not in the second set.
     *
     * @param <E> The type of element contained in the sets.
     * @param set1 The first set to be operated on.
     * @param set2 The second set to be operated on.
     * @return The resulting set.
     */
    static public <E> Set<E> sans(Set<E> set1, Set<E> set2) {
        Set<E> result = new Set<>(set1);
        result.removeElements(set2);
        return result;
    }


    /**
     * This function returns a new set that contains all the elements that are in
     * the first set or the second set or both.
     *
     * @param <E> The type of element contained in the sets.
     * @param set1 The first set to be operated on.
     * @param set2 The second set to be operated on.
     * @return The resulting set.
     */
    static public <E> Set<E> or(Set<E> set1, Set<E> set2) {
        Set<E> result = new Set<>(set1);
        result.addElements(set2);
        return result;
    }


    /**
     * This function returns a new set that contains all the elements that are in
     * the first set or the second set but not both.
     *
     * @param <E> The type of element contained in the sets.
     * @param set1 The first set to be operated on.
     * @param set2 The second set to be operated on.
     * @return The resulting set.
     */
    static public <E> Set<E> xor(Set<E> set1, Set<E> set2) {
        Set<E> result = new Set<>();
        for (E element : set1) {
            if (!set2.containsElement(element)) result.addElement(element);
        }
        for (E element : set2) {
            if (!set1.containsElement(element)) result.addElement(element);
        }
        return result;
    }

}
