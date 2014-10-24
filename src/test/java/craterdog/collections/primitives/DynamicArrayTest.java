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
public class DynamicArrayTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(DynamicArrayTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running DynamicArray Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed DynamicArray Unit Tests.\n");
    }


    @Test
    public void testMethods() {
        DynamicArray<Integer> array = new DynamicArray<>();

        // confirm that the array starts out empty
        assertTrue(array.isEmpty());

        // add in enough elements to get the array to resize its capacity
        for (int i = 0; i < 50; i++) {
            array.add(i);
            assertTrue(array.contains(i));
            assertEquals(i, (int) array.get(i));
            assertEquals(i, array.indexOf(i));
            assertEquals(i, array.size() - 1);
        }

        // insert something in the middle somewhere
        array.add(20, 50);

        // make sure its in the right place
        assertEquals(50, (int) array.get(20));

        // insert something right after it
        array.add(21, 51);

        // make sure the first one is still there
        assertEquals(50, (int) array.get(20));  // make sure it is still the same

        // insert a couple more
        array.add(22, 52);
        array.add(23, 53);

        // make sure the size has grown properly
        assertEquals(54, array.size());

        // test default iterator
        for (int element : array) {
            logger.info("Element: {}", element);
        }
        // remove the first one that was inserted (note it must be as the Object, not the index)
        array.remove(new Integer(50));

        // make sure the next one moved down one
        assertEquals(20, array.indexOf(51));

        // remove all but one of the new ones
        array.remove(20, 2);

        // confirm that the original elements before the insertion point were not touched
        for (int i = 0; i < 20; i++) {
            assertEquals(i, (int) array.get(i));
            assertEquals(i, array.indexOf(i));
        }

        // make sure the element after the insertion point moved up one
        for (int i = 21; i < 51; i++) {
            assertEquals(i - 1, (int) array.get(i));
            assertEquals(i, array.indexOf(i - 1));
        }

        // change the value of the last one that was inserted
        array.set(20, 54);

        // confirm the change
        assertEquals(54, (int) array.get(20));

        // remove the last one that was inserted
        array.remove(20);

        // remove the elements one by one so that the array resizes its capacity back to the minimum
        for (int i = 49; i > 4; i--) {
            array.remove(i);
            assertEquals(i, array.size());
        }

        // clear out the rest of the elements
        array.clear();

        // confirm that the array is now empty
        assertTrue(array.isEmpty());

    }

}
