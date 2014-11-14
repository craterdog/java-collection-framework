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
import org.junit.Test;
import org.junit.BeforeClass;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This unit test performs multi-threaded tests on the blocking <code>Queue</code> class.
 *
 * @author Derk Norton
 */
public class QueueTest {

    static private final XLogger logger = XLoggerFactory.getXLogger(QueueTest.class);


    /**
     * Log a message at the beginning of the tests.
     */
    @BeforeClass
    static public void setUpClass() {
        logger.info("Running Queue Unit Tests...\n");
    }


    /**
     * Log a message at the end of the tests.
     */
    @AfterClass
    static public void tearDownClass() {
        logger.info("Completed Queue Unit Tests.\n");
    }


    /**
     * This method creates one producer and two consumer threads that test the methods
     * in the <code>FIFO</code> interface.
     */
    @Test
    public void testQueue() {
        logger.info("Beginning testQueue()...");

        Queue<Integer> queue = new Queue<>(4);

        logger.info("  Testing multi-threaded access to the blocking queue...");
        Producer p = new Producer(queue);
        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        Thread c1Thread = new Thread(c1);
        c1Thread.start();
        Thread c2Thread = new Thread(c2);
        c2Thread.start();
        Thread pThread = new Thread(p);
        pThread.start();
        try {
            pThread.join();
            Thread.sleep(200);
            c1Thread.interrupt();
            c2Thread.interrupt();
        } catch(InterruptedException e) {
            logger.info("  The test was interrupted.");
        }

        logger.info("Completed testQueue().\n");
    }


    private class Producer implements Runnable {
        private final Queue<Integer> queue;

        private Producer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                logger.info("  Starting a producer thread...");
                int previous = 1;
                int value = 1;
                while (value < 100) {
                    Thread.sleep(value);
                    logger.info("  Attempting to add an element to the queue...");
                    queue.addElementToTail(value);
                    logger.info("  The following value was added: " + value);
                    int temp = value;
                    value += previous;
                    previous = temp;
                }
            } catch (InterruptedException ex) {
                logger.info("  The producer thread was interrupted.");
            }
        }
    }


    private class Consumer implements Runnable {
        private final Queue<Integer> queue;

        private Consumer(Queue<Integer> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                logger.info("  Starting a consumer thread...");
                int previous = 1;
                int index = 1;
                while (index < 100) {
                    logger.info("  Attempting to remove an element from the queue: {}", queue);
                    int value = queue.removeElementFromHead();
                    logger.info("  The following value was removed: " + value);
                    int temp = index;
                    index += previous;
                    previous = temp;
                    Thread.sleep(100 - index);
                }
            } catch (InterruptedException ex) {
                logger.info("  The consumer thread was interrupted.");
            }
        }
    }

}
