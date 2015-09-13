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

import java.net.URL;
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
     * This method tests the bag code examples.
     */
    @Test
    public void testBagCodeExamples() {
        logger.info("Beginning testBagCodeExamples()...");

        // Create an empty bag
        Bag<Integer> emptyBag = new Bag<>();

        // Create a bag with items in it
        Integer[] first = { 1, 5, 3 };
        Bag<Integer> firstBag = new Bag<>(first);

        // Create a second bag with items in it
        Integer[] second = { 4, 2, 6, 4 };
        Bag<Integer> secondBag = new Bag<>(second);

        // Create a third bag with all the items in it
        Integer[] third = { 1, 2, 3, 4, 4, 5, 6 };
        Bag<Integer> thirdBag = new Bag<>(third);

        // Merge a bag with the empty bag
        Bag<Integer> bag = Bag.aggregation(emptyBag, firstBag);
        assert bag.equals(firstBag);
        logger.info("{} merged with {} yields {}", emptyBag, firstBag, bag);

        // Merge two bags with items in them
        bag = Bag.aggregation(firstBag, secondBag);
        assert bag.equals(thirdBag);
        logger.info("{} merged with {} yields {}", firstBag, secondBag, bag);

        // Find the difference between an empty bag and one with items in it
        bag = Bag.difference(emptyBag, firstBag);
        assert bag.isEmpty();
        logger.info("The difference between {} and {} is {}", emptyBag, firstBag, bag);

        // Find the difference between a bag with items in it and an empty one
        bag = Bag.difference(firstBag, emptyBag);
        assert bag.equals(firstBag);
        logger.info("The difference between {} and {} is {}", firstBag, emptyBag, bag);

        // Find the difference between two bags with items in them
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

        // Create some sets
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

        // Find the logical "and" with an empty set
        Set<Integer> set = Set.and(emptySet, firstSet);
        assert set.isEmpty();
        logger.info("{} and {} yields {}", emptySet, firstSet, set);

        // Find the logical "and" with non-empty sets
        set = Set.and(firstSet, secondSet);
        assert set.equals(thirdSet);
        logger.info("{} and {} yields {}", firstSet, secondSet, set);

        // Find the logical "sans" (same as "a and not b")
        set = Set.sans(firstSet, secondSet);
        assert set.equals(fourthSet);
        logger.info("{} sans {} yields {}", firstSet, secondSet, set);

        // Find the logical "or" with an empty set
        set = Set.or(emptySet, firstSet);
        assert !set.isEmpty();
        logger.info("{} or {} yields {}", emptySet, firstSet, set);

        // Find the logical "or" with non-empty sets
        set = Set.or(firstSet, secondSet);
        assert set.equals(fifthSet);
        logger.info("{} or {} yields {}", firstSet, secondSet, set);

        // Find the logical "xor" (same as "(a and not b) or (not a and b)")
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

        // Create a list with items in it
        Integer[] fib = { 1, 1, 2, 3, 5, 8, 13, 21 };
        List<Integer> list = new List<>(fib);
        logger.info("A list of the fibonacci numbers: {}", list);

        // Retrieve an element from the list
        int index = 6;
        int element = list.getElement(index);
        assert element == 8;
        logger.info("The sixth element in {} is {}", list, element);

        // Retrieve a range of elements from the list
        List<Integer> sublist = list.getElements(2, 4);
        logger.info("The elements from {} in the index range [2..4] are {}", list, sublist);

        // Lookup the index of the first matching element in the list
        element = 1;
        index = list.getIndex(element);  // finds the first instance of the element
        assert index == 1;
        logger.info("The index of the first element in {} with value 1 is {}", list, index);

        // Append an element to the list
        element = list.getElement(-1) + list.getElement(-2);  // add the last two
        list.addElement(element);
        logger.info("Appended a new fibonacci number to the list: {}", list);

        // Replace the last element in the list
        element = 144;
        index = -1;  // last element
        list.replaceElement(element, index);
        logger.info("Replaced the last element with 144: {}", list);

        // Insert an element at the beginning of the list
        element = 0;
        index = 1;
        list.insertElement(element, index);
        assert list.getElement(index) == element;
        logger.info("Inserted zero as the first element in the list: {}", list);

        // Insert new elements before the tenth element in the list
        index = 10;
        Integer[] moreFibs = { 34, 55, 89 };
        sublist = new List<>(moreFibs);
        list.insertElements(sublist, index);
        assert list.getElement(index).equals(sublist.getElement(1));
        logger.info("Inserted {} before the tenth element: {}", sublist, list);

        // Remove the first element from the list
        index = 1;
        element = 0;
        assert element == list.removeElement(index);
        logger.info("Removed the first element from the list: {}", list);

        // Remove the last four elements from the list
        sublist.addElement(144);
        List<Integer> removedElements = list.removeElements(-1, -4);
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

        // Create a dictionary with some associations in it
        String[] keys = { "charlie", "bravo", "delta" };
        Integer[] values = { 3, 2, 4 };
        Dictionary<Integer> dictionary = new Dictionary<>(keys, values);
        logger.info("A dictionary of numbers: {}", dictionary);

        // Add a new association
        dictionary.setValue("alpha", 1);
        logger.info("Appended a \"alpha-1\" key-value pair: {}", dictionary);

        // Sort the dictionary
        dictionary.sortElements();
        logger.info("The list now sorted: {}", dictionary);

        // Retrieve the value for a key
        int value = dictionary.getValue("charlie");
        logger.info("The value for key \"charlie\" is: {}", value);

        // Remove an association
        dictionary.removeValue("charlie");
        logger.info("With the value for key \"charlie\" removed: {}", dictionary);

        logger.info("Completed testDictionaryCodeExamples().\n");
    }


    /**
     * This method tests the map code examples.
     * @throws java.lang.Exception
     */
    @Test
    public void testMapCodeExamples() throws Exception {
        logger.info("Beginning testMapCodeExamples()...");

        // Create an empty map
        Map<URL, Double> stocks = new Map<>();
        logger.info("Start with an empty map of stock prices: {}", stocks);

        // Add some closing stock prices to it
        URL apple = new URL("http://apple.com");
        stocks.setValue(apple, 112.40);
        URL google = new URL("http://google.com");
        stocks.setValue(google, 526.98);
        URL amazon = new URL("http://amazon.com");
        stocks.setValue(amazon, 306.64);
        logger.info("Add some closing stock prices: {}", stocks);

        // Retrieve the closing price for Google
        double price = stocks.getValue(google);
        logger.info("Google's closing stock price is {}", price);

        // Sort the stock prices by company URL
        stocks.sortElements();
        logger.info("The stock prices sorted by company web site: {}", stocks);

        logger.info("Completed testMapCodeExamples().\n");
    }


    /**
     * This method tests the stack code examples.
     */
    @Test
    public void testStackCodeExamples() {
        logger.info("Beginning testStackCodeExamples()...");

        // Allocate an empty stack
        Stack<String> stack = new Stack<>();
        logger.info("Start with an empty stack: {}", stack);

        // Push a rock onto it
        String rock = "rock";
        stack.pushElement(rock);
        assert stack.getTop().equals(rock);
        logger.info("Push a rock on it: {}", stack);

        // Push paper onto it
        String paper = "paper";
        stack.pushElement(paper);
        assert stack.getTop().equals(paper);
        logger.info("Push paper on it: {}", stack);

        // Push scissors onto it
        String scissors = "scissors";
        stack.pushElement(scissors);
        assert stack.getTop().equals(scissors);
        assert stack.getSize() == 3;
        logger.info("Push scissors on it: {}", stack);

        // Pop off the scissors
        assert stack.popElement().equals(scissors);
        assert stack.getSize() == 2;
        logger.info("Pop scissors from it: {}", stack);

        // Pop off the paper
        assert stack.popElement().equals(paper);
        assert stack.getSize() == 1;
        logger.info("Pop paper from it: {}", stack);

        // Pop off the rock
        assert stack.popElement().equals(rock);
        assert stack.isEmpty();
        logger.info("Pop rock from it: {}", stack);

        logger.info("Completed testStackCodeExamples().\n");
    }


    /**
     * This method tests the queue code examples.
     * @throws java.lang.Exception
     */
    @Test
    public void testQueueCodeExamples() throws Exception {
        logger.info("Beginning testQueueCodeExamples()...");

        // Allocate an empty queue
        Queue<Integer> queue = new Queue<>(256);  // capacity of 256 elements

        // Start up some consumers
        Consumer consumer1 = new Consumer(queue);
        new Thread(consumer1).start();
        Consumer consumer2 = new Consumer(queue);
        new Thread(consumer2).start();

        // Start up a producer
        Producer producer = new Producer(queue);
        new Thread(producer).start();

        // Wait for them to process the messages
        Thread.sleep(200);

        logger.info("Completed testQueueCodeExamples().\n");
    }


    private class Producer implements Runnable {
        private final Queue<Integer> queue;

        private Producer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            logger.info("  The producer thread has started...");
            try {
                for (int i = 0; i < 100; i++) {
                    queue.addElement(i);
                }
            } catch (InterruptedException ex) {
                logger.info("  The producer thread was interrupted.");
            }
            logger.info("  The producer thread has completed.");
        }
    }


    private class Consumer implements Runnable {
        private final Queue<Integer> queue;

        private Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            logger.info("  A consumer thread has started...");
            try {
                for (int i = 0; i < 50; i++) {
                    queue.removeElement();
                }
            } catch (InterruptedException ex) {
                logger.info("  A consumer thread was interrupted.");
            }
            logger.info("  A consumer thread has completed.");
        }
    }
}
