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

import craterdog.collections.List;
import craterdog.collections.primitives.MergeSorter;
import craterdog.collections.primitives.RandomSorter;
import craterdog.core.Manipulator;
import craterdog.utils.NaturalComparator;
import java.util.Comparator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines the invariant methods that all sortable collections must inherit.
 * A sortable collection allows the order of its elements to be determined externally.  By
 * default, the elements will be placed in the order in which they were added to the collection.
 * Additionally, the elements can be sorted in various ways depending on a specified sorting
 * algorithm and comparison function.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class SortableCollection<E> extends OpenCollection<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(SortableCollection.class);


    /**
     * This method shuffles the elements in the collection using a randomizing algorithm.
     */
    public void shuffleElements() {
        logger.entry();
        Sorter<E> sorter = new RandomSorter<>();
        sorter.sortCollection(this);
        logger.exit();
    }


    /**
     * This method sorts the elements in the collection using the default (merge) sorting
     * algorithm and the elements' <code>compareTo</code> method. It provides an easy way
     * to sort a collection using its natural ordering.
     */
    public void sortElements() {
        logger.entry();
        Sorter<E> sorter = new MergeSorter<>();
        Comparator<? super E> comparator = new NaturalComparator<>();
        sorter.sortCollection(this, comparator);
        logger.exit();
    }


    /**
     * This method sorts the elements in the collection using the default (merge) sorting
     * algorithm and the specified comparison function.
     *
     * @param comparator The desired comparison function.
     */
    public void sortElements(Comparator<? super E> comparator) {
        logger.entry(comparator);
        Sorter<E> sorter = new MergeSorter<>();
        sorter.sortCollection(this, comparator);
        logger.exit();
    }


    /**
     * This method sorts the elements in the collection using the specified sorting
     * algorithm and the specified comparison function.
     *
     * @param sorter The desired sorting algorithm.
     * @param comparator The desired comparison function.
     */
    public void sortElements(Sorter<E> sorter, Comparator<? super E> comparator) {
        logger.entry(sorter, comparator);
        sorter.sortCollection(this, comparator);
        logger.exit();
    }


    /**
     * This method creates a new default manipulator for the collection.
     *
     * @return The new manipulator.
     */
    public abstract Manipulator<E> createManipulator();


    /**
     * This method returns a new collection that is the concatenation of this collection with
     * the specified collection.
     *
     * @param <E> The type of element contained in the collections.
     * @param collection1 The first collection to be concatenated.
     * @param collection2 The second collection to be concatenated.
     * @return The resulting collection.
     */
    static public <E> SortableCollection<E> concatenate(SortableCollection<E> collection1, SortableCollection<E> collection2) {
        logger.entry(collection1, collection2);
        List<E> result = new List<>(collection1);
        result.addElements(collection2);
        logger.exit(result);
        return result;
    }

}