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

import java.util.Collection;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This unit test class tests each of the methods defined in the <code>DynamicArray</code> class.
 *
 * @author Derk Norton
 */
public class ListTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(ListTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Various List Class Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Various List Class Unit Tests.\n");
    }


    @Test
    public void testMethods() throws InstantiationException, IllegalAccessException {
        Class<?>[] classes = {java.util.ArrayList.class, java.util.LinkedList.class, DynamicArray.class, LinkedList.class};

        for (Class<?> clazz : classes) {
            String classname = clazz.getName();
            logger.info("Testing list class: {}", classname);
            long startTime = System.currentTimeMillis();

            int count = 1000;
            while (count-- > 0) {
            @SuppressWarnings("unchecked")
            List<Integer> list = (List<Integer>) clazz.newInstance();

            // confirm that the list starts out empty
            assertTrue(list.isEmpty());

            // add in enough elements to get the list to resize its capacity
            for (int i = 0; i < 50; i++) {
                list.add(i);
                assertTrue(list.contains(i));
                assertEquals(i, (int) list.get(i));
                assertEquals(i, list.indexOf(i));
                assertEquals(i, list.size() - 1);
            }

            // compare arrays
            Object[] array1 = list.toArray();
            Object[] array2 = list.toArray(new Object[array1.length]);
            assertArrayEquals(array1, array2);

            // insert something in the middle somewhere
            list.add(20, 50);

            // make sure its in the right place
            assertEquals(50, (int) list.get(20));

            // insert something right after it
            list.add(21, 51);

            // make sure the first one is still there
            assertEquals(50, (int) list.get(20));  // make sure it is still the same

            // insert a couple more
            Collection<Integer> additions = new DynamicArray<>();
            additions.add(52);
            additions.add(53);
            list.addAll(22, additions);
            assertTrue(list.containsAll(additions));

            // make sure the size has grown properly
            assertEquals(54, list.size());

            // test default iterator
            for (int element : list) {
                assertNotNull(element);
            }
            // remove the first one that was inserted (note it must be as the Object, not the index)
            list.remove(new Integer(50));

            // make sure the next one moved down one
            assertEquals(20, list.indexOf(51));

            // remove all but one of the new ones
            List<Integer> expected = list.subList(20, 22);
            if (list instanceof DynamicArray) {
                DynamicArray<Integer> array = (DynamicArray<Integer>) list;
                @SuppressWarnings("unchecked")
                DynamicArray<Integer> copy = (DynamicArray<Integer>) array.clone();
                copy.retainAll(array);
                assertEquals(array, copy);
                DynamicArray<Integer> actual = array.remove(20, 22);
                assertEquals(expected, actual);
            } else if (list instanceof java.util.ArrayList) {
                java.util.ArrayList<Integer> arrayList = (java.util.ArrayList<Integer>) list;
                @SuppressWarnings("unchecked")
                java.util.ArrayList<Integer> copy = (java.util.ArrayList<Integer>) arrayList.clone();
                copy.retainAll(arrayList);
                assertEquals(arrayList, copy);
                arrayList.remove(20);
                arrayList.remove(21);
            } else if (list instanceof LinkedList) {
                LinkedList<Integer> linked = (LinkedList<Integer>) list;
                @SuppressWarnings("unchecked")
                LinkedList<Integer> copy = (LinkedList<Integer>) linked.clone();
                copy.retainAll(linked);
                assertEquals(linked, copy);
                LinkedList<Integer> actual = linked.remove(20, 22);
                assertEquals(expected, actual);
            } else if (list instanceof java.util.LinkedList) {
                java.util.LinkedList<Integer> linkedList = (java.util.LinkedList<Integer>) list;
                @SuppressWarnings("unchecked")
                java.util.LinkedList<Integer> copy = (java.util.LinkedList<Integer>) linkedList.clone();
                copy.retainAll(linkedList);
                assertEquals(linkedList, copy);
                linkedList.remove(20);
                linkedList.remove(21);
            }

            // confirm that the original elements before the insertion point were not touched
            for (int i = 0; i < 20; i++) {
                assertEquals(i, (int) list.get(i));
                assertEquals(i, list.indexOf(i));
            }

            // make sure the element after the insertion point moved up one
            for (int i = 21; i < 50; i++) {
                assertEquals(i - 1, (int) list.get(i));
                assertEquals(i + 1, list.indexOf(i));
            }

            // change the value of the last one that was inserted
            list.set(20, 54);

            // confirm the change
            assertEquals(54, (int) list.get(20));

            // remove the last one that was inserted
            Collection<Integer> removals = new DynamicArray<>();
            removals.add(54);
            list.removeAll(removals);

            // remove the elements one by one so that the list resizes its capacity back to the minimum
            for (int i = 49; i > 4; i--) {
                list.remove(i);
                assertEquals(i, list.size());
            }

            // clear out the rest of the elements
            list.clear();

            // confirm that the list is now empty
            assertTrue(list.isEmpty());
            }

            long duration = System.currentTimeMillis() - startTime;
            logger.info("Testing of list class: {} completed in {} milliseconds.\n", classname, duration);
        }

    }

}
