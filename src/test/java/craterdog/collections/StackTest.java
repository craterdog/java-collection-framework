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

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

/**
 * This class performs unit tests on the <code>Stack</code> collection class.
 *
 * @author Derk Norton
 */
public class StackTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(StackTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Stack Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Stack Unit Tests.\n");
    }


    /**
     * This method tests the methods in the <code>LIFO</code> interface.
     */
    @Test
    public void testStack() {
        logger.info("Beginning testStack()...");

        Stack<Integer> stack = new Stack<>();

        try {
            logger.info("  Attempting to access the top element of an empty stack...");
            stack.getTop();
            fail("  An empty stack exception should have been thrown.");
        } catch (RuntimeException e) {
            logger.info("  Empty stack exception thrown.");
        }

        logger.info("  Pushing elements onto the stack: {}", stack);
        for (int i = 1; i <= 5; i++) {
            stack.pushElement(i);
            int size = stack.getSize();
            assertEquals("  The stack size is incorrect.", i, size);
            int element = stack.getTop();
            assertEquals("  The top element is incorrect.", i, element);
        }

        logger.info("  Popping elements off the stack: {}", stack);
        for (int i = 5; i >= 1; i--) {
            int element = stack.popElement();
            assertEquals("  The popped element is incorrect.", i, element);
            int size = stack.getSize();
            assertEquals("  The stack size is incorrect.", i - 1, size);
        }

        try {
            logger.info("  Attempting to pop the top element off of an empty stack...");
            stack.popElement();
            fail("  An empty stack exception should have been thrown.");
        } catch (RuntimeException e) {
            logger.info("  Empty stack exception thrown.");
        }

        logger.info("Completed testStack().\n");
    }

}
