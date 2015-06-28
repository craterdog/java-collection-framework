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

import craterdog.collections.abstractions.OpenCollection;
import craterdog.collections.abstractions.OpenCollectionTestUtils;
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
     * This method runs the standard time test each of the common Java and Crater Dog Technologies
     * collection types.
     */
    //@Test - turned off until needed since it takes a long time to run
    public void testCDCollectionPerformance() {

        logger.info("  Testing the performance of a craterdog.collections.Bag with default comparator...");
        OpenCollection<Integer> collection = new Bag<>();
        long durationInMilliseconds = OpenCollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.Bag with custom comparator...");
        OpenCollectionTestUtils.InOrderIntegerComparator comparator = new OpenCollectionTestUtils.InOrderIntegerComparator();
        collection = new Bag<>(comparator);
        durationInMilliseconds = OpenCollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.Set with default comparator...");
        collection = new Set<>();
        durationInMilliseconds = OpenCollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.Set with custom comparator...");
        collection = new Set<>(comparator);
        durationInMilliseconds = OpenCollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.List...");
        collection = new List<>();
        durationInMilliseconds = OpenCollectionTestUtils.runCDTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

    }


    /**
     * This method runs the standard time test each of the common Java and Crater Dog Technologies
     * collection types.
     */
    @Test //- turned off until needed since it takes a long time to run
    public void testJavaCollectionPerformance() {

        logger.info("  Testing the performance of a java.util.HashSet...");
        java.util.Collection<Integer> collection = new java.util.HashSet<>();
        long durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.LinkedHashSet...");
        collection = new java.util.LinkedHashSet<>();
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.TreeSet with default comparator...");
        collection = new java.util.TreeSet<>();
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.primitives.RandomizedTree with default comparator...");
        collection = new craterdog.collections.primitives.RandomizedTree<>();
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.TreeSet with custom comparator...");
        OpenCollectionTestUtils.InOrderIntegerComparator comparator = new OpenCollectionTestUtils.InOrderIntegerComparator();
        collection = new java.util.TreeSet<>(comparator);
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a craterdog.collections.primitives.RandomizedTree with custom comparator...");
        comparator = new OpenCollectionTestUtils.InOrderIntegerComparator();
        collection = new craterdog.collections.primitives.RandomizedTree<>(comparator);
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        /*
        logger.info("  Testing the performance of a java.util.ArrayList...");
        collection = new java.util.ArrayList<>();
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);

        logger.info("  Testing the performance of a java.util.LinkedList...");
        collection = new java.util.LinkedList<>();
        durationInMilliseconds = OpenCollectionTestUtils.runJavaTimeTest(collection);
        logger.info("  The test took {} milliseconds to run.\n", durationInMilliseconds);
        */

    }

}
