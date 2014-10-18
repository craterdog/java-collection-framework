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
import craterdog.collections.interfaces.Iteratable;
import java.util.Comparator;
import java.util.Random;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This utility class provides functions that are used across all unit tests for collections.
 *
 * @author Derk Norton
 */
public class CollectionTestUtils {

    static private final XLogger logger = XLoggerFactory.getXLogger(CollectionTestUtils.class);
    static private final Random generator = new Random();


    /**
     * This utility function runs a standard time test on a collection.
     *
     * @param collection The collection to be tested.
     */
    static public void runTimeTest(Collection<Integer> collection) {
        long startInMillis = System.currentTimeMillis();

        for (int j = 0; j < 100; j++) {

            logger.debug("  Adding random elements to the collection...");
            int size = collection.getNumberOfElements();
            assert collection.isEmpty() || size > 0;
            for (int i = 0; i < 100; i++) {
                int value = generator.nextInt(10);
                if (collection.addElement(value)) size++;
            }
            assert collection.getNumberOfElements() == size;

            logger.debug("  Looking for subsets of the collection...");
            Integer[] possibles = { 1, 2, 3 };
            Iteratable<Integer> elements = new List<>(possibles);
            collection.containsAnyElementsIn(elements);
            collection.containsAllElementsIn(elements);

            logger.debug("  Adding multiple elements to the collection...");
            size += collection.addElements(elements);
            Integer[] array = { 4, 5, 6 };
            size += collection.addElements(array);
            assert collection.getNumberOfElements() == size;

            logger.debug("  Performing random searches on the collection...");
            for (int i = 0; i < 10; i++) {
                int value = generator.nextInt(10);
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
            for (int i = 0; i < 20; i++) {
                int value = generator.nextInt(10);
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
        logger.info("  The timed test took " + durationInMillis + " milliseconds.");
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
