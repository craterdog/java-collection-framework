/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.primitives;

import java.util.ListIterator;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class performs unit tests on the randomized binary search tree (RBST) class.
 *
 * @author Derk Norton
 */
public class TreeTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(TreeTest.class);

    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running RandomizedTree Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed RandomizedTree Unit Tests.\n");
    }


    static private final int[] inOrder = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    static private final int[] reverseOrder = { 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
    static private final int[] randomOrder = { 4, 11, 1, 7, 0, 3, 9, 5, 12, 2, 8, 10, 6 };
    static private final int[] randomDuplicates = { 4, 12, 11, 1, 7, 0, 8, 3, 2, 9, 5, 12, 2, 8, 10, 6 };
    static private final int[] withDuplicates = { 0, 1, 2, 2, 3, 4, 5, 6, 7, 8, 8, 9, 10, 11, 12, 12 };
    static private final int[] reverseDuplicates = { 12, 12, 11, 10, 9, 8, 8, 7, 6, 5, 4, 3, 2, 2, 1, 0 };


    /**
     * This method tests the loading of a tree with elements inserted in order.
     */
    @Test
    public void testInOrder() {
        logger.info("Beginning testInOrder()...");
        performTest(inOrder, inOrder, false);
        logger.info("Completed testInOrder().\n");
    }


    /**
     * This method tests the loading of a tree with elements inserted in reverse order.
     */
    @Test
    public void testReverseOrder() {
        logger.info("Beginning testReverseOrder()...");
        performTest(reverseOrder, inOrder, false);
        logger.info("Completed testReverseOrder().\n");
    }


    /**
     * This method tests the loading of a tree with elements inserted in random order.
     */
    @Test
    public void testRandomOrder() {
        logger.info("Beginning testRandomOrder()...");
        performTest(randomOrder, inOrder, false);
        logger.info("Completed testRandomOrder().\n");
    }


    /**
     * This method tests the loading of a tree with duplicate elements ignored.
     */
    @Test
    public void testWithDuplicates() {
        logger.info("Beginning testWithDuplicates()...");
        performTest(randomDuplicates, inOrder, false);
        logger.info("Completed testWithDuplicates().\n");
    }


    /**
     * This method tests the loading of a tree with duplicate elements allowed.
     */
    @Test
    public void testWithDuplicatesAllowed() {
        logger.info("Beginning testWithDuplicatesAllowed()...");
        performTest(randomDuplicates, withDuplicates, true);
        logger.info("Completed testWithDuplicatesAllowed().\n");
    }


    @Test
    public void test100() {
        for (int j = 0; j < 100; j++) {
            RandomizedTree<Integer> tree = new RandomizedTree<>();
            for (int i = 0; i < 100; i++) {
                tree.add(i);
            }
        }
    }

    private void performTest(int[] source, int[] expected, boolean duplicatesAllowed) {

        logger.info("  Building the tree...");
        RandomizedTree<Integer> tree = new RandomizedTree<>(duplicatesAllowed);
        for (int i : source) {
            tree.add(i);
        }

        logger.info("  Testing containment searches...");
        assertTrue("  The tree said it does not contain the value 2.", tree.contains(2));
        assertFalse("  The tree said it does contain the value -5.", tree.contains(-5));

        logger.info("  Testing iterating over the elements in order...");
        int i = 0;
        int[] treeValues = new int[expected.length];
        for (int v : tree) {
            treeValues[i++] = v;
        }
        Assert.assertArrayEquals(expected, treeValues);

        logger.info("  Testing iterating over the elements in reverse order...");
        i = 0;
        ListIterator<Integer> iterator = tree.listIterator(tree.size());
        while(iterator.hasPrevious()) {
            int v = iterator.previous();
            treeValues[i++] = v;
        }
        Assert.assertArrayEquals(duplicatesAllowed ? reverseDuplicates : reverseOrder, treeValues);

        logger.info("  Testing the access to indexed elements in random order...");
        if (!duplicatesAllowed) {
            for (int index : randomOrder) {
                int element = tree.get(index);
                if (index != element) fail("  The index (" + index + ") did not match the element(" + element + ").");
                Assert.assertEquals("  The index returned does not match the element's index.", index, tree.indexOf(element));
            }
        }

        logger.info("  Testing the removal of elements in random order...");
        int size = tree.size();
        for (int element : randomOrder) {
            if (tree.remove(element)) size--;
            Assert.assertEquals(size, tree.size());
        }
        if (duplicatesAllowed) {
            Assert.assertEquals(3, tree.size());
        } else {
            Assert.assertEquals(0, tree.size());
        }

    }

}
