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
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class performs unit tests on the <code>List</code> collection class.
 *
 * @author Derk Norton
 */
public class ListTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(ListTest.class);
    static private final Random generator = new Random();


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running List Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed List Unit Tests.\n");
    }


    /**
     * This method tests the compareTo() operation.
     */
    @Test
    public void testListComparison() {
        logger.info("Beginning testListComparison()...");
        Integer[] values = { 1, 1, 2, 3, 5, 8, 13 };
        List<Integer> firstList = new List<>(values);
        List<Integer> secondList = new List<>(values);
        int comparison = firstList.compareTo(secondList);
        assertEquals(0, comparison);
        logger.info("Completed testListComparison().\n");
    }


    /**
     * This method tests the toString() operation.
     */
    @Test
    public void testListToString() {
        logger.info("Beginning testListToString()...");

        Integer[] empty = { };
        List<Integer> emptyList = new List<>(empty);
        String actual = emptyList.toString();
        String expectedEmpty = "[]";
        assertEquals(expectedEmpty, actual);
        logger.info("  The empty list is: {}", actual);

        Integer[] first = { 1 };
        List<Integer> firstList = new List<>(first);
        actual = firstList.toString();
        String expectedFirst = "[1]";
        assertEquals(expectedFirst, actual);
        logger.info("  The first list is: {}", actual);

        Integer[] second = { 1, 2, 3 };
        List<Integer> secondList = new List<>(second);
        actual = secondList.toString();
        String expectedSecond = "[1, 2, 3]";
        assertEquals(expectedSecond, actual);
        logger.info("  The second list is: {}", actual);

        Integer[] third = { 1, 2, 3, 4, 5, 6 };
        List<Integer> thirdList = new List<>(third);
        actual = thirdList.toString();
        String expectedThird = "[1, 2, 3, 4, 5, 6]";
        assertEquals(expectedThird, actual);
        logger.info("  The third list is: {}", actual);

        logger.info("Completed testListToString().\n");
    }


    /**
     * This method tests the methods in the <code>Indexed</code> interface.
     */
    @Test
    public void testList() {
        logger.info("Beginning testList()...");

        Integer[] values = { 2, 4, 1, 8, 6, 7, 3 };
        Integer[] expected = { 3, 7, 6, 8, 1, 4, 2, 0 };

        logger.info("  Populating the list with values...");
        List<Integer> list = new List<>();
        assertArrayEquals(new Integer[0], list.toArray());
        list.addElement(0);
        int size = values.length;
        for (int i = 0; i < size; i++) {
            int value = values[i];
            list.insertElementBeforeIndex(value, 1);
        }

        logger.info("  Checking the values are in the right order...");
        Integer[] listValues = list.toArray();
        assertArrayEquals(expected, listValues);

        logger.info("  Inserting a list of values...");
        List<Integer> additionalValues = new List<>(values);
        list.insertElementsBeforeIndex(additionalValues, 1);

        logger.info("  Performing random accesses on the collection...");
        for (int i = 0; i < 7; i++) {
            int index = generator.nextInt(size) + 1;  // convert to unit based index
            list.getElementAtIndex(index);
            int first = generator.nextInt(size) + 1;  // convert to unit based index
            int last = first + generator.nextInt(size - first + 1);
            list.getElementsInRange(first, last);
        }

        logger.info("  Replacing a value...");
        list.replaceElementAtIndex(9, 9);
        assertEquals(new Integer(9), list.getElementAtIndex(9));

        logger.info("  Removing elements in a range...");
        list.removeElementsInRange(8, 14);

        logger.info("  Removing elements at an index...");
        list.removeElementAtIndex(size + 1);
        listValues = list.toArray();
        assertArrayEquals(values, listValues);

        logger.info("Completed testList().\n");
    }


    /**
     * This method tests the standard list operations.
     */
    @Test
    public void testListOperations() {
        logger.info("Beginning testListOperations()...");

        Integer[] empty = { };
        Integer[] first = { 1, 2, 3 };
        Integer[] second = { 4, 5, 6 };
        Integer[] third = { 1, 2, 3, 4, 5, 6 };

        List<Integer> emptyList = new List<>(empty);
        List<Integer> firstList = new List<>(first);
        List<Integer> secondList = new List<>(second);
        List<Integer> thirdList = new List<>(third);

        logger.info("  Testing the concatenate operation...");
        List<Integer> list = List.concatenate(emptyList, firstList);
        assertEquals("  The resulting list from the concatenation is incorrect.", firstList, list);
        list = List.concatenate(firstList, emptyList);
        assertEquals("  The resulting list from the concatenation is incorrect.", firstList, list);
        list = List.concatenate(firstList, secondList);
        assertEquals("  The resulting list from the concatenation is incorrect.", thirdList, list);

        logger.info("Completed testListOperations().\n");
    }


    /**
     * This method tests the ordered collection methods from the <code>Sortable</code> interface with a list.
     */
    @Test
    public void testSortingAList() {
        logger.info("Beginning testSortingAList()...");

        List<Integer> list = new List<>();
        Integer[] inOrder = { 1, 2, 3, 4, 5 };
        Integer[] reverseOrder = { 5, 4, 3, 2, 1 };

        logger.info("  Adding entries to the list...");
        list.addElements(inOrder);

        logger.info("  Confirming that the entries are in order...");
        Integer[] elements = list.toArray();
        Assert.assertArrayEquals(inOrder, elements);

        logger.info("  Reversing the entries in the list...");
        OpenCollectionTestUtils.ReverseOrderIntegerComparator reverseComparator = new OpenCollectionTestUtils.ReverseOrderIntegerComparator();
        list.sortElements(reverseComparator);

        logger.info("  Confirming that the entries are in reverse order...");
        elements = list.toArray();
        Assert.assertArrayEquals(reverseOrder, elements);

        logger.info("  Re-reversing the entries in the list...");
        OpenCollectionTestUtils.InOrderIntegerComparator inOrderComparator = new OpenCollectionTestUtils.InOrderIntegerComparator();
        list.sortElements(inOrderComparator);

        logger.info("  Confirming that the entries are back in order...");
        elements = list.toArray();
        Assert.assertArrayEquals(inOrder, elements);

        logger.info("Completed testSortingAList().\n");
    }


    /**
     * This method tests the ordered collection methods from the <code>Indexed</code> interface with a list.
     */
    @Test
    public void testElementInsertionAndDeletionOnAList() {
        logger.info("Beginning testElementInsertionAndDeletion()...");

        List<Integer> list = new List<>();
        Integer[] original = { 1, 2, 3, 4, 5 };

        logger.info("  Adding entries to the list...");
        list.addElements(original);

        logger.info("  Testing the insertion of an element...");
        list.insertElementBeforeIndex(0, 1);
        Assert.assertEquals("  The inserted element is not correct.", 0, (int) list.getElementAtIndex(1));

        logger.info("  Testing the insertion of a collection of elements...");
        Integer[] negatives = { -3, -2, -1 };
        List<Integer> negativesList = new List<>(negatives);
        list.insertElementsBeforeIndex(negativesList, 1);
        Integer[] fullList = { -3, -2, -1, 0, 1, 2, 3, 4, 5 };
        Integer[] elements = list.toArray();
        Assert.assertArrayEquals(fullList, elements);

        logger.info("  Testing the replacement of an element...");
        int value = list.replaceElementAtIndex(7, 4);
        Assert.assertEquals("  The replaced element is not correct.", 0, value);

        logger.info("  Testing the removal of an element...");
        value = list.removeElementAtIndex(4);
        Assert.assertEquals("  The removed element is not correct.", 7, value);

        logger.info("  Testing the removal of a collection of elements...");
        list.removeElementsInRange(1, 3);
        elements = list.toArray();
        Assert.assertArrayEquals(original, elements);

        logger.info("Completed testElementInsertionAndDeletion().\n");
    }

    /**
     * This method tests sorting and shuffling of lists containing instances of simple classes.
     */
    @Test
    public void testSortingAndShufflingOfAList() {
        logger.info("Beginning testSortingAndShufflingOfAList()...");

        List<SimpleElement> list = new List<>();
        list.addElement(new SimpleElement("d", 4));
        list.addElement(new SimpleElement("c", 3));
        list.addElement(new SimpleElement("b", 2));
        list.addElement(new SimpleElement("a", 1));
        logger.info("The initial list: {}", list);

        list.shuffleElements();
        logger.info("The shuffled list: {}", list);

        list.sortElements();
        logger.info("The sorted list: {}", list);

        logger.info("Completed testSortingAndShufflingOfAList().\n");
    }

    static private class SimpleElement implements Comparable<SimpleElement> {
        private final String foo;
        private final int bar;

        SimpleElement(String foo, int bar) {
            this.foo = foo;
            this.bar = bar;
        }

        @Override
        public String toString() {
            return "[" + "foo: " + foo + ", bar: " + bar + "]";
        }

        @Override
        public int compareTo(SimpleElement that) {
            return this.foo.compareTo(that.foo);
        }

    }

}
