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
 * This collection class implements an ordered collection that allows duplicate elements.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public final class Bag<E> extends OrderedCollection<E> {

    /**
     * This constructor creates a new empty bag with no comparator function.
     */
    public Bag() {
        super(true);
    }


    /**
     * This constructor creates a new bag with no comparator function and seeds
     * it with the elements from the specified array.
     *
     * @param elements The elements to be used to seed the new bag.
     */
    public Bag(E[] elements) {
        this(elements, null);
    }


    /**
     * This constructor creates a new bag with no comparator function and seeds
     * it with the elements from the specified collection.
     *
     * @param elements The elements to be used to seed the new bag.
     */
    public Bag(Iterable<? extends E> elements) {
        this(elements, null);
    }


    /**
     * This constructor creates a new bag with the specified comparator function.
     *
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    public Bag(Comparator<? super E> comparator) {
        super(true, comparator);
    }


    /**
     * This constructor creates a new bag with the specified comparator function and seeds
     * it with the elements from the specified array.
     *
     * @param elements The elements to be used to seed the new bag.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    public Bag(E[] elements, Comparator<? super E> comparator) {
        super(true, comparator);
        addElements(elements);
    }


    /**
     * This constructor creates a new bag with the specified comparator function and seeds
     * it with the elements from the specified collection.
     *
     * @param elements The elements to be used to seed the new bag.
     * @param comparator The comparator to be used to compare two elements during ordering.
     */
    public Bag(Iterable<? extends E> elements, Comparator<? super E> comparator) {
        super(true, comparator);
        for (E element : elements) {
            addElement(element);
        }
    }


    /**
     * This function returns a new bag that contains the all the elements from
     * both the specified bags.
     *
     * @param <E> The type of element contained in the bags.
     * @param bag1 The first bag whose elements are to be added.
     * @param bag2 The second bag whose elements are to be added.
     * @return The resulting bag.
     */
    static public <E> Bag<E> merge(Bag<E> bag1, Bag<E> bag2) {
        Bag<E> result = new Bag<>(bag1);
        result.addElements(bag2);
        return result;
    }


    /**
     * This function returns a new bag that contains the elements that are
     * in the first bag but not contained in the second bag specified.
     *
     * @param <E> The type of element contained in the bags.
     * @param bag1 The bag whose elements are to be removed.
     * @param bag2 The bag whose elements are to be removed.
     * @return The resulting bag.
     */
    static public <E> Bag<E> difference(Bag<E> bag1, Bag<E> bag2) {
        Bag<E> result = new Bag<>(bag1);
        result.removeElements(bag2);
        return result;
    }

}
