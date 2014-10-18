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

import craterdog.collections.Bag;
import craterdog.collections.Set;
import org.junit.Assert;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This utility class provides functions that are used across all unit tests for ordered collections.
 *
 * @author Derk Norton
 */
public class OrderedCollectionTestUtils {

    static private final XLogger logger = XLoggerFactory.getXLogger(OrderedCollectionTestUtils.class);


    /**
     * This utility function tests various ways of reordering the elements in an ordered collection.
     *
     * @param collection The collection to be tested.
     */
    static public void reorderCollection(OrderedCollection<Integer> collection) {
        Integer[] inOrder = { 1, 2, 3, 4, 5 };
        Integer[] reverseOrder = { 5, 4, 3, 2, 1 };

        logger.info("  Adding entries to the collection...");
        collection.addElements(inOrder);

        logger.info("  Confirming that the entries are in order...");
        Integer[] elements = new Integer[inOrder.length];
        collection.toArray(elements);
        Assert.assertArrayEquals(inOrder, elements);

        logger.info("  Reversing the entries in the collection...");
        CollectionTestUtils.ReverseOrderIntegerComparator reverseComparator = new CollectionTestUtils.ReverseOrderIntegerComparator();
        collection = new Bag<>(collection, reverseComparator);

        logger.info("  Confirming that the entries are in reverse order...");
        elements = new Integer[reverseOrder.length];
        collection.toArray(elements);
        Assert.assertArrayEquals(reverseOrder, elements);

        logger.info("  Re-reversing the entries in the collection...");
        CollectionTestUtils.InOrderIntegerComparator inOrderComparator = new CollectionTestUtils.InOrderIntegerComparator();
        collection = new Set<>(collection, inOrderComparator);

        logger.info("  Confirming that the entries are back in order...");
        elements = new Integer[inOrder.length];
        collection.toArray(elements);
        Assert.assertArrayEquals(inOrder, elements);

    }

}
