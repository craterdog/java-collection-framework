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
import craterdog.collections.abstractions.OpenCollectionTestUtils;
import craterdog.collections.abstractions.Iterator;
import craterdog.collections.abstractions.OpenCollection;
import craterdog.collections.interfaces.Accessible;
import java.lang.reflect.Array;
import java.util.Random;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class performs unit tests on the <code>Map</code> collection class.
 *
 * @author Derk Norton
 */
public class MapTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(MapTest.class);
    static private final Random generator = new Random();


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Map Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Map Unit Tests.\n");
    }


    /**
     * This method tests the compareTo() operation.
     */
    @Test
    public void testMapComparison() {
        logger.info("Beginning testMapComparison()...");
        String[] keys = { "1", "1", "2", "3", "5", "8", "13" };
        Integer[] values = { 1, 1, 2, 3, 5, 8, 13 };
        Map<String, Integer> firstMap = new Map<>(keys, values);
        Map<String, Integer> secondMap = firstMap.copy();
        int comparison = firstMap.compareTo(secondMap);
        assertEquals(0, comparison);
        logger.info("Completed testMapComparison().\n");
    }


    /**
     * This method tests the toString() operation.
     */
    @Test
    public void testMapToString() {
        logger.info("Beginning testMapToString()...");

        Map<String, Object> emptyMap = new Map<>();
        String actual = emptyMap.toString();
        String expectedEmpty = "{ }";
        assertEquals(expectedEmpty, actual);
        logger.info("  The empty map is: {}", actual);

        Map<String, Integer> tinyMap = new Map<>();
        tinyMap.associateKeyWithValue("singleton", 1);
        actual = tinyMap.toString();
        String expectedTiny =
                "{\n" +
                "  \"singleton\" : 1\n" +
                "}";
        assertEquals(expectedTiny, actual);
        logger.info("  The tiny map is: {}", actual);

        Map<String, Integer> smallMap = new Map<>();
        smallMap.associateKeyWithValue("first", 1);
        smallMap.associateKeyWithValue("second", 2);
        smallMap.associateKeyWithValue("third", 3);
        actual = smallMap.toString();
        String expectedSmall =
                "{\n" +
                "  \"first\" : 1,\n" +
                "  \"second\" : 2,\n" +
                "  \"third\" : 3\n" +
                "}";
        assertEquals(expectedSmall, actual);
        logger.info("  The small map is: {}", actual);

        Integer[] array = { 1, 2, 3 };
        List<Integer> list = new List<>(array);
        Map<String, Object> compositeMap = new Map<>();
        compositeMap.associateKeyWithValue("first", 1);
        compositeMap.associateKeyWithValue("second", "B");
        compositeMap.associateKeyWithValue("third", list);
        compositeMap.associateKeyWithValue("fourth", smallMap);
        compositeMap.associateKeyWithValue("fifth", emptyMap);
        actual = compositeMap.toString();
        String expectedComposite =
                "{\n" +
                "  \"first\" : 1,\n" +
                "  \"second\" : \"B\",\n" +
                "  \"third\" : [ 1, 2, 3 ],\n" +
                "  \"fourth\" : {\n" +
                "    \"first\" : 1,\n" +
                "    \"second\" : 2,\n" +
                "    \"third\" : 3\n" +
                "  },\n" +
                "  \"fifth\" : { }\n" +
                "}";
        assertEquals(expectedComposite, actual);
        logger.info("  The composite map is: {}", actual);

        logger.info("Completed testMapToString().\n");
    }


    /**
     * This method tests the methods in the <code>Associative</code> interface.
     */
    @Test
    public void testMap() {
        logger.info("Beginning testMap()...");

        Integer[] keys = { 1, 6, 3, 2, 8, 9, 5 };
        Integer[] values = { 2, 4, 1, 8, 6, 7, 3 };

        logger.info("  Populating the map with key value pairs...");
        Map<Integer, Integer> map = new Map<>();
        int size = keys.length;
        for (int i = 0; i < size; i++) {
            int key = keys[i];
            int value = values[i];
            map.associateKeyWithValue(key, value);
        }

        logger.info("  Checking the keys are in the right order...");
        Integer[] mapKeys = map.getKeys().toArray();
        Assert.assertArrayEquals(keys, mapKeys);

        logger.info("  Checking the values are in the right order...");
        Integer[] mapValues = map.getValues().toArray();
        assertArrayEquals(values, mapValues);

        logger.info("  Checking the associations are correct and in the right order...");
        Accessible<Association<Integer, Integer>> associations = map.getAssociations();
        Iterator<Association<Integer, Integer>> iterator = associations.createDefaultIterator();
        for (int i = 0; i < size && iterator.hasNext(); i++) {
            Association<Integer, Integer> association = iterator.next();
            Integer key = keys[i];
            assertEquals(key, association.key);
            Integer value = values[i];
            assertEquals(value, association.value);
        }

        logger.info("  Retrieving the value for a key...");
        Integer value = map.getValueForKey(5);
        assertEquals(new Integer(3), value);

        logger.info("  Removing the value for a key...");
        value = map.removeValueForKey(2);
        assertEquals(new Integer(8), value);

        logger.info("  Attempting to retrieve the value of a nonexistent key...");
        value = map.getValueForKey(12);
        assertNull(value);

        logger.info("Completed testMap().\n");
    }


    /**
     * This method tests the standard map operations.
     */
    @Test
    public void testMapOperations() {
        logger.info("Beginning testMapOperations()...");

        Integer[] empty = { };
        Integer[] first = { 1, 2, 3 };
        Integer[] second = { 4, 5, 6 };
        Integer[] third = { 1, 2, 3, 4, 5, 6 };

        Map<Integer, Integer> emptyMap = new Map<>(empty, empty);
        Map<Integer, Integer> firstMap = new Map<>(first, first);
        Map<Integer, Integer> secondMap = new Map<>(second, second);
        Map<Integer, Integer> thirdMap = new Map<>(third, third);

        logger.info("  Testing the concatenate operation...");
        Map<Integer, Integer> map = Map.concatenate(emptyMap, firstMap);
        assertEquals("  The resulting map from the concatenation is incorrect.", firstMap, map);
        map = Map.concatenate(firstMap, emptyMap);
        assertEquals("  The resulting map from the concatenation is incorrect.", firstMap, map);
        map = Map.concatenate(firstMap, secondMap);
        assertEquals("  The resulting map from the concatenation is incorrect.", thirdMap, map);

        logger.info("  Testing the reduce operation...");
        Set<Integer> keys = new Set<>(empty);
        map = Map.reduce(emptyMap, keys);
        assertEquals("  The resulting map from the reduction is incorrect.", emptyMap, map);
        keys = new Set<>(second);
        map = Map.reduce(emptyMap, keys);
        assertEquals("  The resulting map from the reduction is incorrect.", emptyMap, map);
        keys = new Set<>(empty);
        map = Map.reduce(firstMap, keys);
        assertEquals("  The resulting map from the reduction is incorrect.", emptyMap, map);
        keys = new Set<>(second);
        map = Map.reduce(thirdMap, keys);
        assertEquals("  The resulting map from the reduction is incorrect.", secondMap, map);

        logger.info("Completed testMapOperations().\n");
    }


    /**
     * This method runs the standard time test on a map.
     */
    @Test
    public void testMapTiming() {
        logger.info("Beginning testMapTiming()...");

        OpenCollection<Association<Integer, Integer>> collection = new Map<>();
        long startInMillis = System.currentTimeMillis();

        for (int j = 0; j < 100; j++) {

            logger.debug("  Adding random elements to the collection...");
            int size = collection.getNumberOfElements();
            assert collection.isEmpty() || size > 0;
            for (int i = 0; i < 100; i++) {
                int value = generator.nextInt(10);
                Association<Integer, Integer> association = new Association<>(value, value);
                if (collection.addElement(association)) size++;
            }
            assert collection.getNumberOfElements() == size;

            logger.debug("  Looking for subsets of the collection...");
            Association<Integer, Integer> a1 = new Association<>(1, 1);
            Association<Integer, Integer> a2 = new Association<>(2, 2);
            Association<Integer, Integer> a3 = new Association<>(3, 3);
            @SuppressWarnings("unchecked")
            Association<Integer, Integer>[] possibles = (Association<Integer, Integer>[]) Array.newInstance(a1.getClass(), 3);
            possibles[0] = a1;
            possibles[1] = a2;
            possibles[2] = a3;
            Collection<Association<Integer, Integer>> elements = new List<>(possibles);
            collection.containsAnyElementsIn(elements);
            collection.containsAllElementsIn(elements);

            logger.debug("  Adding multiple elements to the collection...");
            size += collection.addElements(elements);
            a1 = new Association<>(4, 4);
            a2 = new Association<>(5, 5);
            a3 = new Association<>(6, 6);
            @SuppressWarnings("unchecked")
            Association<Integer, Integer>[] array = (Association<Integer, Integer>[]) Array.newInstance(a1.getClass(), 3);
            array[0] = a1;
            array[1] = a2;
            array[2] = a3;
            size += collection.addElements(array);
            assert collection.getNumberOfElements() == size;

            logger.debug("  Performing random searches on the collection...");
            for (int i = 0; i < 10; i++) {
                int value = generator.nextInt(10);
                Association<Integer, Integer> association = new Association<>(value, value);
                collection.containsElement(association);
            }

            logger.debug("  Iterating over the elements in the collection in order...");
            Iterator<Association<Integer, Integer>> iterator = collection.createDefaultIterator();
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
                Association<Integer, Integer> association = new Association<>(value, value);
                if (collection.removeElement(association)) size--;
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
        logger.info("  The test took " + durationInMillis + " milliseconds.");

        logger.info("Completed testMapTiming().\n");
    }


    /**
     * This method tests the ordered collection methods from the <code>Sortable</code> interface with a map.
     */
    @Test
    public void testSortingAMap() {
        logger.info("Beginning testSortingAMap()...");

        Map<Integer, Integer> map = new Map<>();
        Integer[] inOrder = { 1, 2, 3, 4, 5 };
        Integer[] reverseOrder = { 5, 4, 3, 2, 1 };

        logger.info("  Adding entries to the map...");
        for (int value : inOrder) {
            Association<Integer, Integer> newAssociation = new Association<>(value, value);
            map.addElement(newAssociation);
        }

        logger.info("  Confirming that the entries are in order...");
        Integer[] keys = map.getKeys().toArray();
        Assert.assertArrayEquals(inOrder, keys);

        logger.info("  Reversing the entries in the map...");
        OpenCollectionTestUtils.ReverseOrderAssociationComparator reverseComparator = new OpenCollectionTestUtils.ReverseOrderAssociationComparator();
        map.sortElements(reverseComparator);

        logger.info("  Confirming that the entries are in reverse order...");
        keys = map.getKeys().toArray();
        Assert.assertArrayEquals(reverseOrder, keys);

        logger.info("  Re-reversing the entries in the map...");
        OpenCollectionTestUtils.InOrderAssociationComparator inOrderComparator = new OpenCollectionTestUtils.InOrderAssociationComparator();
        map.sortElements(inOrderComparator);

        logger.info("  Confirming that the entries are back in order...");
        keys = map.getKeys().toArray();
        Assert.assertArrayEquals(inOrder, keys);

        logger.info("Completed testSortingAMap().\n");
    }


    /**
     * This method tests the ordered collection methods from the <code>Indexed</code> interface with a map.
     */
    @Test
    public void testElementInsertionAndDeletionOnAMap() {
        logger.info("Beginning testElementInsertionAndDeletionOnAMap()...");

        Map<Integer, Integer> map = new Map<>();
        Integer[] original = { 1, 2, 3, 4, 5 };

        logger.info("  Adding entries to the map...");
        for (int value : original) {
            Association<Integer, Integer> newAssociation = new Association<>(value, value);
            map.addElement(newAssociation);
        }

        logger.info("  Testing the insertion of a collection of elements...");
        Map<Integer, Integer> negativesMap = new Map<>();
        Integer[] negatives = { -3, -2, -1 };
        for (int value : negatives) {
            Association<Integer, Integer> newAssociation = new Association<>(value, value);
            negativesMap.addElement(newAssociation);
        }

        logger.info("Completed testElementInsertionAndDeletionOnAMap().\n");
    }

}
