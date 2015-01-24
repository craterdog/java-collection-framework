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

        logger.debug("  Adding entries to the collection...");
        collection.addElements(inOrder);

        logger.debug("  Confirming that the entries are in order...");
        Integer[] elements = collection.toArray();
        Assert.assertArrayEquals(inOrder, elements);

        logger.debug("  Reversing the entries in the collection...");
        OpenCollectionTestUtils.ReverseOrderIntegerComparator reverseComparator = new OpenCollectionTestUtils.ReverseOrderIntegerComparator();
        collection = new Bag<>(collection, reverseComparator);

        logger.debug("  Confirming that the entries are in reverse order...");
        elements = collection.toArray();
        Assert.assertArrayEquals(reverseOrder, elements);

        logger.debug("  Re-reversing the entries in the collection...");
        OpenCollectionTestUtils.InOrderIntegerComparator inOrderComparator = new OpenCollectionTestUtils.InOrderIntegerComparator();
        collection = new Set<>(collection, inOrderComparator);

        logger.debug("  Confirming that the entries are back in order...");
        elements = collection.toArray();
        Assert.assertArrayEquals(inOrder, elements);

    }

}
