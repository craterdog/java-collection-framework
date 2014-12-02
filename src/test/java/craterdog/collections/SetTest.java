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

import craterdog.collections.abstractions.CollectionTestUtils;
import craterdog.collections.abstractions.OrderedCollectionTestUtils;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class performs unit tests on the <code>Set</code> collection class.
 *
 * @author Derk Norton
 */
public class SetTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(SetTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Set Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Set Unit Tests.\n");
    }


    /**
     * This method tests the toString() operation.
     */
    @Test
    public void testSetToString() {
        logger.info("Beginning testSetToString()...");

        Integer[] empty = { };
        Set<Integer> emptySet = new Set<>(empty);
        String actual = emptySet.toString();
        String expectedEmpty = "[]";
        assertEquals(expectedEmpty, actual);
        logger.info("  The empty set is: {}", actual);

        Integer[] first = { 1 };
        Set<Integer> firstSet = new Set<>(first);
        actual = firstSet.toString();
        String expectedFirst = "[1]";
        assertEquals(expectedFirst, actual);
        logger.info("  The first set is: {}", actual);

        Integer[] second = { 1, 2, 3 };
        Set<Integer> secondSet = new Set<>(second);
        actual = secondSet.toString();
        String expectedSecond = "[1, 2, 3]";
        assertEquals(expectedSecond, actual);
        logger.info("  The second set is: {}", actual);

        Integer[] third = { 1, 2, 3, 4, 5, 6 };
        Set<Integer> thirdSet = new Set<>(third);
        actual = thirdSet.toString();
        String expectedThird = "[1, 2, 3, 4, 5, 6]";
        assertEquals(expectedThird, actual);
        logger.info("  The third set is: {}", actual);

        logger.info("Completed testSetToString().\n");
    }


    /**
     * This method tests the standard set operations.
     */
    @Test
    public void testSetOperations() {
        logger.info("Beginning testSetOperations()...");

        Integer[] empty = { };
        Integer[] first = { 1, 2, 3, 4 };
        Integer[] second = { 3, 4, 5, 6 };
        Integer[] third = { 3, 4 };
        Integer[] fourth = { 1, 2 };
        Integer[] fifth = { 1, 2, 3, 4, 5, 6 };
        Integer[] sixth = { 1, 2, 5, 6 };

        Set<Integer> emptySet = new Set<>(empty);
        Set<Integer> firstSet = new Set<>(first);
        Set<Integer> secondSet = new Set<>(second);
        Set<Integer> thirdSet = new Set<>(third);
        Set<Integer> fourthSet = new Set<>(fourth);
        Set<Integer> fifthSet = new Set<>(fifth);
        Set<Integer> sixthSet = new Set<>(sixth);

        logger.info("  Testing the logical and operation...");
        Set<Integer> set = Set.and(emptySet, firstSet);
        assertEquals("  The resulting set from the logical and is incorrect.", emptySet, set);
        set = Set.and(firstSet, emptySet);
        assertEquals("  The resulting set from the logical and is incorrect.", emptySet, set);
        set = Set.and(firstSet, secondSet);
        assertEquals("  The resulting set from the logical and is incorrect.", thirdSet, set);

        logger.info("  Testing the logical sans operation...");
        set = Set.sans(emptySet, firstSet);
        assertEquals("  The resulting set from the logical sans is incorrect.", emptySet, set);
        set = Set.sans(firstSet, emptySet);
        assertEquals("  The resulting set from the logical sans is incorrect.", firstSet, set);
        set = Set.sans(firstSet, secondSet);
        assertEquals("  The resulting set from the logical sans is incorrect.", fourthSet, set);

        logger.info("  Testing the logical or operation...");
        set = Set.or(emptySet, firstSet);
        assertEquals("  The resulting set from the logical or is incorrect.", firstSet, set);
        set = Set.or(firstSet, firstSet);
        assertEquals("  The resulting set from the logical or is incorrect.", firstSet, set);
        set = Set.or(firstSet, secondSet);
        assertEquals("  The resulting set from the logical or is incorrect.", fifthSet, set);

        logger.info("  Testing the logical xor operation...");
        set = Set.xor(emptySet, firstSet);
        assertEquals("  The resulting set from the logical xor is incorrect.", firstSet, set);
        set = Set.xor(firstSet, emptySet);
        assertEquals("  The resulting set from the logical xor is incorrect.", firstSet, set);
        set = Set.xor(firstSet, secondSet);
        assertEquals("  The resulting set from the logical xor is incorrect.", sixthSet, set);

        logger.info("Completed testSetOperations().\n");
    }


    /**
     * This method runs the standard time test on a set without a comparator.
     */
    @Test
    public void testSetTimingWithoutComparator() {
        logger.info("Beginning testSetTimingWithoutComparator()...");
        Set<Integer> set = new Set<>();
        CollectionTestUtils.runCDTimeTest(set);
        logger.info("Completed testSetTimingWithoutComparator().\n");
    }


    /**
     * This method runs the standard time test on a set with a custom comparator.
     */
    @Test
    public void testSetTimingWithComparator() {
        logger.info("Beginning testSetTimingWithComparator()...");
        CollectionTestUtils.InOrderIntegerComparator comparator = new CollectionTestUtils.InOrderIntegerComparator();
        Set<Integer> set = new Set<>(comparator);
        CollectionTestUtils.runCDTimeTest(set);
        logger.info("Completed testSetTimingWithComparator().\n");
    }


    /**
     * This method reorders a set without a comparator.
     */
    @Test
    public void testReorderingSetWithoutComparator() {
        logger.info("Beginning testReorderingSetWithoutComparator()...");
        Set<Integer> set = new Set<>();
        OrderedCollectionTestUtils.reorderCollection(set);
        logger.info("Completed testReorderingSetWithoutComparator().\n");
    }


    /**
     * This method reorders a set using an in order comparator.
     */
    @Test
    public void testReorderingSetWithComparator() {
        logger.info("Beginning testReorderingSetWithComparator()...");
        CollectionTestUtils.InOrderIntegerComparator comparator = new CollectionTestUtils.InOrderIntegerComparator();
        Set<Integer> set = new Set<>(comparator);
        OrderedCollectionTestUtils.reorderCollection(set);
        logger.info("Completed testReorderingSetWithComparator().\n");
    }

}
