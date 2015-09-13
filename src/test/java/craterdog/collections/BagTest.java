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

import craterdog.collections.abstractions.OpenCollectionTestUtils;
import craterdog.collections.abstractions.OrderedCollectionTestUtils;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class performs unit tests on the <code>Bag</code> collection class.
 *
 * @author Derk Norton
 */
public class BagTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(BagTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Bag Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Bag Unit Tests.\n");
    }


    /**
     * This method tests the compareTo() operation.
     */
    @Test
    public void testBagComparison() {
        logger.info("Beginning testBagComparison()...");
        Integer[] values = { 1, 1, 2, 3, 5, 8, 13 };
        Bag<Integer> firstBag = new Bag<>(values);
        Bag<Integer> secondBag = firstBag.copy();
        int comparison = firstBag.compareTo(secondBag);
        assertEquals(0, comparison);
        logger.info("Completed testBagComparison().\n");
    }


    /**
     * This method tests the toString() operation.
     */
    @Test
    public void testBagToString() {
        logger.info("Beginning testBagToString()...");

        Integer[] empty = { };
        Bag<Integer> emptyBag = new Bag<>(empty);
        String actual = emptyBag.toString();
        String expectedEmpty = "[ ]";
        assertEquals(expectedEmpty, actual);
        logger.info("  The empty bag is: {}", actual);

        Integer[] first = { 1 };
        Bag<Integer> firstBag = new Bag<>(first);
        actual = firstBag.toString();
        String expectedFirst = "[\n  1\n]";
        assertEquals(expectedFirst, actual);
        logger.info("  The first bag is: {}", actual);

        Integer[] second = { 1, 2, 3 };
        Bag<Integer> secondBag = new Bag<>(second);
        actual = secondBag.toString();
        String expectedSecond = "[\n  1,\n  2,\n  3\n]";
        assertEquals(expectedSecond, actual);
        logger.info("  The second bag is: {}", actual);

        Integer[] third = { 1, 2, 3, 4, 5, 6 };
        Bag<Integer> thirdBag = new Bag<>(third);
        actual = thirdBag.toString();
        String expectedThird = "[\n  1,\n  2,\n  3,\n  4,\n  5,\n  6\n]";
        assertEquals(expectedThird, actual);
        logger.info("  The third bag is: {}", actual);

        logger.info("Completed testBagToString().\n");
    }


    /**
     * This method tests the standard bag operations.
     */
    @Test
    public void testBagOperations() {
        logger.info("Beginning testBagOperations()...");

        Integer[] empty = { };
        Integer[] first = { 1, 2, 3 };
        Integer[] second = { 4, 5, 6 };
        Integer[] third = { 1, 2, 3, 4, 5, 6 };

        Bag<Integer> emptyBag = new Bag<>(empty);
        Bag<Integer> firstBag = new Bag<>(first);
        Bag<Integer> secondBag = new Bag<>(second);
        Bag<Integer> thirdBag = new Bag<>(third);

        logger.info("  Testing the merge operation...");
        Bag<Integer> bag = Bag.aggregation(emptyBag, firstBag);
        assertEquals("  The resulting bag from the merge is incorrect.", firstBag, bag);
        bag = Bag.aggregation(firstBag, emptyBag);
        assertEquals("  The resulting bag from the merge is incorrect.", firstBag, bag);
        bag = Bag.aggregation(firstBag, secondBag);
        assertEquals("  The resulting bag from the merge is incorrect.", thirdBag, bag);

        logger.info("  Testing the difference operation...");
        bag = Bag.difference(emptyBag, firstBag);
        assertEquals("  The resulting bag from the difference is incorrect.", emptyBag, bag);
        bag = Bag.difference(firstBag, emptyBag);
        assertEquals("  The resulting bag from the difference is incorrect.", firstBag, bag);
        bag = Bag.difference(thirdBag, firstBag);
        assertEquals("  The resulting bag from the difference is incorrect.", secondBag, bag);

        logger.info("Completed testBagOperations().\n");
    }


    /**
     * This method reorders a bag without a comparator.
     */
    @Test
    public void testReorderingBagWithoutComparator() {
        logger.info("Beginning testReorderingBagWithoutComparator()...");
        Bag<Integer> bag = new Bag<>();
        OrderedCollectionTestUtils.reorderCollection(bag);
        logger.info("Completed testReorderingBagWithoutComparator().\n");
    }


    /**
     * This method reorders a bag using an in order comparator.
     */
    @Test
    public void testReorderingBagWithComparator() {
        logger.info("Beginning testReorderingBagWithComparator()...");
        OpenCollectionTestUtils.InOrderIntegerComparator comparator = new OpenCollectionTestUtils.InOrderIntegerComparator();
        Bag<Integer> bag = new Bag<>(comparator);
        OrderedCollectionTestUtils.reorderCollection(bag);
        logger.info("Completed testReorderingBagWithComparator().\n");
    }

}
