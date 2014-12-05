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

import craterdog.collections.abstractions.Collection;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class tests the code examples listed in the wiki.
 *
 * @author Derk Norton
 */
public class CollectionExamplesTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(CollectionExamplesTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Example Code Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Example Code Unit Tests.\n");
    }


    /**
     * This method tests the general collection code examples.
     */
    @Test
    public void testCollectionCodeExamples() {
        logger.info("Beginning testCollectionCodeExamples()...");

        // add one element to a list
        Collection<Integer> collection = new List<>();
        collection.addElement(2);
        logger.info("collection: {}", collection);

        // add more elements
        Integer[] integers = { 3, 1, 5, 4, 1};
        Collection<Integer> subcollection = new List<>(integers);
        collection.addElements(subcollection);
        assert collection.containsAllElementsIn(subcollection);
        logger.info("collection: {}", collection);

        // remove an element
        collection.removeElement(5);
        assert collection.containsAnyElementsIn(subcollection);
        logger.info("collection: {}", collection);

        // order the elements and removed the duplicates
        collection = new Set<>(collection);
        assert collection.getNumberOfElements() == 4;
        logger.info("collection: {}", collection);

        // remove all the elements
        collection.removeAllElements();
        assert collection.isEmpty();
        logger.info("collection: {}", collection);

        logger.info("Completed testCollectionCodeExamples().\n");
    }


    /**
     * This method tests the bag code examples.
     */
    @Test
    public void testBagCodeExamples() {
        logger.info("Beginning testBagCodeExamples()...");

        // create an empty bag
        Bag<Integer> emptyBag = new Bag<>();

        // create a bag with items in it
        Integer[] first = { 1, 5, 3 };
        Bag<Integer> firstBag = new Bag<>(first);

        // create a second bag with items in it
        Integer[] second = { 4, 2, 6, 4 };
        Bag<Integer> secondBag = new Bag<>(second);

        // create a third bag with all the items in it
        Integer[] third = { 1, 2, 3, 4, 4, 5, 6 };
        Bag<Integer> thirdBag = new Bag<>(third);

        // merge a bag with the empty bag
        Bag<Integer> bag = Bag.merge(emptyBag, firstBag);
        assert bag.equals(firstBag);
        logger.info("{} merged with {} yields {}", emptyBag, firstBag, bag);

        // merge two bags with items in them
        bag = Bag.merge(firstBag, secondBag);
        assert bag.equals(thirdBag);
        logger.info("{} merged with {} yields {}", firstBag, secondBag, bag);

        // find the difference between an empty bag and one with items
        bag = Bag.difference(emptyBag, firstBag);
        assert bag.isEmpty();
        logger.info("The difference between {} and {} is {}", emptyBag, firstBag, bag);

        bag = Bag.difference(firstBag, emptyBag);
        assert bag.equals(firstBag);
        logger.info("bag: {}", bag);
        logger.info("The difference between {} and {} is {}", firstBag, emptyBag, bag);

        bag = Bag.difference(thirdBag, firstBag);
        assert bag.equals(secondBag);
        logger.info("The difference between {} and {} is {}", thirdBag, firstBag, bag);

        logger.info("Completed testBagCodeExamples().\n");
    }


    /**
     * This method tests the set code examples.
     */
    @Test
    public void testSetCodeExamples() {
        logger.info("Beginning testSetCodeExamples()...");

        Set<Integer> emptySet = new Set<>();

        Integer[] first = { 1, 2, 3, 4 };
        Set<Integer> firstSet = new Set<>(first);

        Integer[] second = { 3, 4, 5, 6 };
        Set<Integer> secondSet = new Set<>(second);

        Integer[] third = { 3, 4 };
        Set<Integer> thirdSet = new Set<>(third);

        Integer[] fourth = { 1, 2 };
        Set<Integer> fourthSet = new Set<>(fourth);

        Integer[] fifth = { 1, 2, 3, 4, 5, 6 };
        Set<Integer> fifthSet = new Set<>(fifth);

        Integer[] sixth = { 1, 2, 5, 6 };
        Set<Integer> sixthSet = new Set<>(sixth);

        // logical "and" with empty set
        Set<Integer> set = Set.and(emptySet, firstSet);
        assert set.isEmpty();
        logger.info("{} and {} yields {}", emptySet, firstSet, set);

        // logical "and" with non-empty sets
        set = Set.and(firstSet, secondSet);
        assert set.equals(thirdSet);
        logger.info("{} and {} yields {}", firstSet, secondSet, set);

        // logical "sans" (same as "a and not b")
        set = Set.sans(firstSet, secondSet);
        assert set.equals(fourthSet);
        logger.info("{} sans {} yields {}", firstSet, secondSet, set);

        // logical "or" with empty set
        set = Set.or(emptySet, firstSet);
        assert !set.isEmpty();
        logger.info("{} or {} yields {}", emptySet, firstSet, set);

        // logical "or" with non-empty sets
        set = Set.or(firstSet, secondSet);
        assert set.equals(fifthSet);
        logger.info("{} or {} yields {}", firstSet, secondSet, set);

        // logical "xor" (same as "(a and not b) or (not a and b)")
        set = Set.xor(firstSet, secondSet);
        assert set.equals(sixthSet);
        logger.info("{} xor {} yields {}", firstSet, secondSet, set);

        logger.info("Completed testSetCodeExamples().\n");
    }


    /**
     * This method tests the list code examples.
     */
    @Test
    public void testListCodeExamples() {
        logger.info("Beginning testListCodeExamples()...");

        // create a list with items in it
        Integer[] fib = { 1, 1, 2, 3, 5, 8, 13, 21 };
        List<Integer> list = new List<>(fib);
        logger.info("list: {}", list);

        // retrieve an element from the list
        int index = 6;
        int element = list.getElementAtIndex(index);
        assert element == 8;
        logger.info("The sixth element in {} is {}.", list, element);

        // retrieve a range of elements from the list
        List<Integer> sublist = list.getElementsInRange(2, 4);
        logger.info("The elements from {} in the index range [2..4] are {}.", list, sublist);

        // lookup the index of the first matching element in the list
        element = 1;
        index = list.getIndexOfElement(element);  // finds the first instance of the element
        assert index == 1;
        logger.info("The index of the first element in {} with value 1 is {}.", list, index);

        // append an element to the list
        element = list.getElementAtIndex(-1) + list.getElementAtIndex(-2);  // add the last two
        list.addElement(element);
        logger.info("Appended a new element to the list: {}", list);

        // replace the last element in the list
        element = 144;
        index = -1;  // last element
        list.replaceElementAtIndex(element, index);
        logger.info("Replaced the last element with 144: {}", list);

        // insert an element at the beginning of the list
        element = 0;
        index = 1;
        list.insertElementBeforeIndex(element, index);
        assert list.getElementAtIndex(index) == element;
        logger.info("Inserted zero as the first element in the list: {}", list);

        // insert new elements before the tenth element in the list
        index = 10;
        Integer[] moreFibs = { 34, 55, 89 };
        sublist = new List<>(moreFibs);
        list.insertElementsBeforeIndex(sublist, index);
        assert list.getElementAtIndex(index).equals(sublist.getElementAtIndex(1));
        logger.info("Inserted {} before the tenth element: {}", list);

        // remove the first element from the list
        index = 1;
        element = 0;
        assert element == list.removeElementAtIndex(index);

        // remove the last four elements from the list
        sublist.addElement(144);
        List<Integer> removedElements = list.removeElementsInRange(-1, -4);
        assert sublist.equals(removedElements);
        logger.info("Removed {} from the list: {}", removedElements, list);

        logger.info("Completed testListCodeExamples().\n");
    }


    /**
     * This method tests the dictionary code examples.
     */
    @Test
    public void testDictionaryCodeExamples() {
        logger.info("Beginning testDictionaryCodeExamples()...");

        logger.info("Completed testDictionaryCodeExamples().\n");
    }


    /**
     * This method tests the map code examples.
     */
    @Test
    public void testMapCodeExamples() {
        logger.info("Beginning testMapCodeExamples()...");

        logger.info("Completed testMapCodeExamples().\n");
    }


    /**
     * This method tests the queue code examples.
     */
    @Test
    public void testQueueCodeExamples() {
        logger.info("Beginning testQueueCodeExamples()...");

        logger.info("Completed testQueueCodeExamples().\n");
    }


    /**
     * This method tests the stack code examples.
     */
    @Test
    public void testStackCodeExamples() {
        logger.info("Beginning testStackCodeExamples()...");

        logger.info("Completed testStackCodeExamples().\n");
    }

}
