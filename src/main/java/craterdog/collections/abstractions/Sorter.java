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

import craterdog.utils.NaturalComparator;
import java.util.Comparator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines a framework for each concrete sorter class
 * that sorts the elements in a collection using a specific algorithm and
 * comparator function.
 *
 * @author Derk Norton
 * @param <E> The type of element being sorted.
 */
public abstract class Sorter<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Sorter.class);


    /**
     * This method sorts the elements of its collection using a specific
     * sorting algorithm.  The natural comparator is used to compare
     * each pair of elements during sorting.
     *
     * @param collection The collection to be sorted.
     */
    public final void sortCollection(SortableCollection<E> collection) {
        logger.entry(collection);
        Comparator<? super E> comparator = new NaturalComparator<>();
        sortCollection(collection, comparator);
        logger.exit();
    }


    /**
     * This method sorts the elements of its collection using a specific
     * sorting algorithm.  The specified comparator is used to compare
     * each pair of elements during sorting.
     *
     * @param collection The collection to be sorted.
     * @param comparator The comparison function to be used during sorting.
     */
    public abstract void sortCollection(SortableCollection<E> collection, Comparator<? super E> comparator);

}
