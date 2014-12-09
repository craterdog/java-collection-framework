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
        logger.info("A list of the fibonacci numbers: {}", list);

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
        logger.info("Appended a new fibonacci number to the list: {}", list);

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

        // create a dictionary with items in it
        String[] keys = { "bravo", "charlie", "delta" };
        Integer[] values = { 2, 3, 4 };
        Dictionary<Integer> dictionary = new Dictionary<>(keys, values);
        logger.info("A dictionary of numbers: {}", dictionary);

        dictionary.associateKeyWithValue("alpha", 1);
        logger.info("Appended a \"alpha-1\" key-value pair: {}", dictionary);

        dictionary.sortElements();
        logger.info("The list now sorted: {}", dictionary);

        int value = dictionary.getValueForKey("charlie");
        logger.info("The value for key \"charlie\" is: {}", value);

        dictionary.removeValueForKey("charlie");
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

        Map<URL, Double> stocks = new Map<>();
        logger.info("Start with an empty map of stock prices: {}", stocks);

        URL apple = new URL("http://apple.com");
        URL google = new URL("http://google.com");
        URL amazon = new URL("http://amazon.com");

        stocks.associateKeyWithValue(apple, 112.40);
        stocks.associateKeyWithValue(google, 526.98);
        stocks.associateKeyWithValue(amazon, 306.64);
        logger.info("Add some closing stock prices: {}", stocks);

        double price = stocks.getValueForKey(google);
        logger.info("Google's closing stock price is {}", price);

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

        Stack<String> stack = new Stack<>();
        logger.info("Start with an empty stack: {}", stack);

        String rock = "rock";
        String paper = "paper";
        String scissors = "scissors";

        stack.pushElementOnTop(rock);
        assert stack.getTopElement().equals(rock);
        logger.info("Push a rock on it: {}", stack);

        stack.pushElementOnTop(paper);
        assert stack.getTopElement().equals(paper);
        logger.info("Push paper on it: {}", stack);

        stack.pushElementOnTop(scissors);
        assert stack.getTopElement().equals(scissors);
        assert stack.getNumberOfElements() == 3;
        logger.info("Push scissors on it: {}", stack);

        assert stack.popElementOffTop().equals(scissors);
        assert stack.getNumberOfElements() == 2;
        logger.info("Pop scissors from it: {}", stack);

        assert stack.popElementOffTop().equals(paper);
        assert stack.getNumberOfElements() == 1;
        logger.info("Pop paper from it: {}", stack);

        assert stack.popElementOffTop().equals(rock);
        assert stack.isEmpty();
        logger.info("Pop rock from it: {}", stack);

        logger.info("Completed testStackCodeExamples().\n");
    }


    /**
     * This method tests the queue code examples.
     */
    @Test
    public void testQueueCodeExamples() throws Exception {
        logger.info("Beginning testQueueCodeExamples()...");

        Queue<Integer> queue = new Queue<>(256);  // capacity of 256 elements

        // start up some consumers
        Consumer consumer1 = new Consumer(queue);
        new Thread(consumer1).start();
        Consumer consumer2 = new Consumer(queue);
        new Thread(consumer2).start();

        // start up a producer
        Producer producer = new Producer(queue);
        new Thread(producer).start();

        // wait for them to process the messages
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
                    queue.addElementToTail(i);
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
                    queue.removeElementFromHead();
                }
            } catch (InterruptedException ex) {
                logger.info("  A consumer thread was interrupted.");
            }
            logger.info("  A consumer thread has completed.");
        }
    }
}
