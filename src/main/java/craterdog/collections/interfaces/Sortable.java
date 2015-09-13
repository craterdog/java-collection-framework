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

import craterdog.collections.abstractions.Sorter;
import craterdog.core.Manipulator;
import java.util.Comparator;


/**
 * This interface defines the methods that must be implemented by each collection that
 * allows its elements to be explicitly sorted.  By default, the elements are stored
 * in the order that they were added to the collection.  But they can be explicitly
 * sorted at any time by passing in a specific sort algorithm and comparison function.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public interface Sortable<E> {

    /**
     * This method shuffles the elements in the collection using a randomizing algorithm.
     */
    void shuffleElements();

    /**
     * This method sorts the elements in the collection using the default (merge) sorting
     * algorithm and the elements' <code>compareTo</code> method. It provides an easy way
     * to sort a collection using its natural ordering.
     */
    void sortElements();

    /**
     * This method sorts the elements in the collection using the default (merge) sorting
     * algorithm and the specified comparison function.
     *
     * @param comparator The desired comparison function.
     */
    void sortElements(Comparator<? super E> comparator);

    /**
     * This method sorts the elements in the collection using the specified sorting
     * algorithm and the specified comparison function.
     *
     * @param sorter The desired sorting algorithm.
     * @param comparator The desired comparison function.
     */
    void sortElements(Sorter<E> sorter, Comparator<? super E> comparator);

    /**
     * This method creates a new default manipulator for the collection.
     *
     * @return The new manipulator.
     */
    Manipulator<E> createManipulator();

}
