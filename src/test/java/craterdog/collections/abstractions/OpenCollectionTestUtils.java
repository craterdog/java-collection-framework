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

import craterdog.collections.Association;
import craterdog.collections.List;
import craterdog.collections.interfaces.Accessible;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This utility class provides functions that are used across all unit tests for collections.
 *
 * @author Derk Norton
 */
public class OpenCollectionTestUtils {

    static private final XLogger logger = XLoggerFactory.getXLogger(OpenCollectionTestUtils.class);
    static private final Random generator = new Random();


    /**
     * This utility function runs a standard time test on a Crater Dog collection.
     *
     * @param collection The Crater Dog collection to be tested.
     * @return The duration of the test in milliseconds.
     */
    static public long runCDTimeTest(OpenCollection<Integer> collection) {
        long startInMillis = System.currentTimeMillis();

        for (int j = 0; j < 1000; j++) {

            logger.debug("  Adding random elements to the collection...");
            int size = collection.getNumberOfElements();
            assert collection.isEmpty() || size > 0;
            for (int i = 0; i < 1000; i++) {
                int value = generator.nextInt(100);
                if (collection.addElement(value)) size++;
            }
            assert collection.getNumberOfElements() == size;

            logger.debug("  Looking for subsets of the collection...");
            Integer[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            Accessible<Integer> elements = new List<>(array);
            collection.containsAnyElementsIn(elements);
            collection.containsAllElementsIn(elements);

            logger.debug("  Adding multiple elements to the collection...");
            size += collection.addElements(elements);
            size += collection.addElements(array);
            assert collection.getNumberOfElements() == size;

            logger.debug("  Performing random searches on the collection...");
            for (int i = 0; i < 1000; i++) {
                int value = generator.nextInt(100);
                collection.containsElement(value);
            }

            logger.debug("  Iterating over the elements in the collection in order...");
            Iterator<Integer> iterator = collection.createDefaultIterator();
            while (iterator.hasNext()) {
                iterator.getNextElement();
            }

            logger.debug("  Iterating over the elements in the collection in reverse order...");
            while (iterator.hasPreviousElement()) {
                iterator.getPreviousElement();
            }

            logger.debug("  Removing random elements from the collection...");
            for (int i = 0; i < 1000; i++) {
                int value = generator.nextInt(100);
                if (collection.removeElement(value)) size--;
            }
            assert size == collection.getNumberOfElements();

            logger.debug("  Removing multiple elements from the collection...");
            size -= collection.removeElements(array);
            size -= collection.removeElements(elements);
            assert collection.getNumberOfElements() == size;

        }

        logger.debug("  Removing all the elements from the collection...");
        collection.removeAllElements();

        long stopInMillis = System.currentTimeMillis();
        long durationInMillis = stopInMillis - startInMillis;
        return durationInMillis;
    }


    /**
     * This utility function runs a standard time test on a Java collection.
     *
     * @param collection The Java collection to be tested.
     * @return The duration of the test in milliseconds.
     */
    static public long runJavaTimeTest(java.util.Collection<Integer> collection) {
        long startInMillis = System.currentTimeMillis();

        for (int j = 0; j < 1000; j++) {

            logger.debug("  Adding random elements to the collection...");
            int size = collection.size();
            assert collection.isEmpty() || size > 0;
            for (int i = 0; i < 1000; i++) {
                int value = generator.nextInt(100);
                if (collection.add(value)) size++;
            }
            assert collection.size() == size;

            logger.debug("  Looking for subsets of the collection...");
            Integer[] array = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
            java.util.Collection<Integer> elements = java.util.Arrays.asList(array);
            //no similar method for "collection.containsAnyElementsIn(elements);" so fake it
            for (Integer integer : elements) {
                if (collection.contains(integer)) break;
            }
            collection.containsAll(elements);

            logger.debug("  Adding multiple elements to the collection...");
            collection.addAll(elements);
            collection.addAll(Arrays.asList(array));
            size = collection.size();

            logger.debug("  Performing random searches on the collection...");
            for (int i = 0; i < 1000; i++) {
                int value = generator.nextInt(100);
                collection.contains(value);
            }

            logger.debug("  Iterating over the elements in the collection in order...");
            java.util.Iterator<Integer> iterator = collection.iterator();
            while (iterator.hasNext()) {
                iterator.next();
            }

            logger.debug("  Iterating over the elements in the collection in reverse order...");
            java.util.List<Integer> list = new java.util.ArrayList<>(collection);
            java.util.ListIterator<Integer> listIterator = list.listIterator();
            while (listIterator.hasPrevious()) {
                listIterator.previous();
            }

            logger.debug("  Removing random elements from the collection...");
            for (int i = 0; i < 1000; i++) {
                int value = generator.nextInt(100);
                if (collection.remove(value)) size--;
            }
            assert size == collection.size();

            logger.debug("  Removing multiple elements from the collection...");
            collection.removeAll(Arrays.asList(array));
            collection.removeAll(elements);
            collection.size();

        }

        logger.debug("  Removing all the elements from the collection...");
        collection.clear();

        long stopInMillis = System.currentTimeMillis();
        long durationInMillis = stopInMillis - startInMillis;
        return durationInMillis;
    }


    /**
     * This class provides a comparator that can be used to order integers
     * in an ordered collect in their natural order.
     */
    static public class InOrderIntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer element1, Integer element2) {
            return element1.compareTo(element2);
        }

    }


    /**
     * This class provides a comparator that can be used to order integers
     * in an ordered collect in their reverse order.
     */
    static public class ReverseOrderIntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer element1, Integer element2) {
            return element2.compareTo(element1);
        }

    }


    /**
     * This class provides a comparator that can be used to order associations
     * in an ordered collect in their natural key order.
     */
    static public class InOrderAssociationComparator implements Comparator<Association<Integer, Integer>> {

        @Override
        public int compare(Association<Integer, Integer> element1, Association<Integer, Integer> element2) {
            return element1.key.compareTo(element2.key);
        }

    }


    /**
     * This class provides a comparator that can be used to order associations
     * in an ordered collect in their reverse key order.
     */
    static public class ReverseOrderAssociationComparator implements Comparator<Association<Integer, Integer>> {

        @Override
        public int compare(Association<Integer, Integer> element1, Association<Integer, Integer> element2) {
            return element2.key.compareTo(element1.key);
        }

    }

}
