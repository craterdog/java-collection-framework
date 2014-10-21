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
import craterdog.collections.abstractions.CollectionTestUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This class performs unit tests on the <code>Bag</code> collection class.
 *
 * @author Derk Norton
 */
public class ComparisonTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(ComparisonTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Comparison Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Comparison Tests.\n");
    }


    /**
     * This method runs the standard time test each of the common Java and Craterdog
     * collection types.
     */
    //@Test - turned off until needed since it takes a long time to run
    public void testCDCollectionPerformance() {

        logger.info("  Testing the performance of a craterdog.collections.Bag with default comparator...");
        Collection<Integer> collection = new Bag<>();
        long durationInMilliseconds = CollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.Bag with custom comparator...");
        CollectionTestUtils.InOrderIntegerComparator comparator = new CollectionTestUtils.InOrderIntegerComparator();
        collection = new Bag<>(comparator);
        durationInMilliseconds = CollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.Set with default comparator...");
        collection = new Set<>();
        durationInMilliseconds = CollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.Set with custom comparator...");
        collection = new Set<>(comparator);
        durationInMilliseconds = CollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.List...");
        collection = new List<>();
        durationInMilliseconds = CollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

    }


    /**
     * This method runs the standard time test each of the common Java and Craterdog
     * collection types.
     */
    //@Test - turned off until needed since it takes a long time to run
    public void testJavaCollectionPerformance() {

        logger.info("  Testing the performance of a java.util.HashSet...");
        java.util.Collection<Integer> collection = new java.util.HashSet<>();
        long durationInMilliseconds = CollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.LinkedHashSet...");
        collection = new java.util.LinkedHashSet<>();
        durationInMilliseconds = CollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.TreeSet with default comparator...");
        collection = new java.util.TreeSet<>();
        durationInMilliseconds = CollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.TreeSet with custom comparator...");
        CollectionTestUtils.InOrderIntegerComparator comparator = new CollectionTestUtils.InOrderIntegerComparator();
        collection = new java.util.TreeSet<>(comparator);
        durationInMilliseconds = CollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.ArrayList...");
        collection = new java.util.ArrayList<>();
        durationInMilliseconds = CollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.LinkedList...");
        collection = new java.util.LinkedList<>();
        durationInMilliseconds = CollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

    }

}
