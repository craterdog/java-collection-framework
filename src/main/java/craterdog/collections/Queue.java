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

import craterdog.collections.abstractions.ClosedCollection;
import craterdog.collections.interfaces.FIFO;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a queue (FIFO) data structure.  Normally, multiple
 * threads will be accessing a queue at the same time.  This class enforces synchronized
 * access to all methods of this class.  The implementation dynamically scales up and down
 * the size of the underlying data structures as the number elements changes over time.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by this collection.
 */
public class Queue<E> extends ClosedCollection<E> implements FIFO<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Queue.class);

    private final int capacity;


    /**
     * This constructor creates a new queue of unlimited capacity.
     */
    public Queue() {
        logger.entry();
        this.capacity = Integer.MAX_VALUE;
        logger.exit();
    }


    /**
     * This constructor creates a new queue with the specified capacity.
     *
     * @param capacity The maximum number of elements that can be in the queue
     * at one time.
     */
    public Queue(int capacity) {
        logger.entry(capacity);
        this.capacity = capacity;
        logger.exit();
    }


    @Override
    public final synchronized void addElementToTail(E element) throws InterruptedException {
        logger.entry();
        while (true) {  // do this in a loop in case there are spurious wakeups (see Object.wait() javadoc)
            int size = list.getNumberOfElements();
            if (size < capacity) {
                logger.info("Adding the element: " + element);
                list.addElement(element);
                notify();  // waiting removeElement() calls
                break;
            } else {
                logger.info("Waiting for the queue to drop from maximum capacity...");
                wait();  // for a removeElement() call
            }
        }
        logger.exit();
    }


    @Override
    public final synchronized E removeElementFromHead() throws InterruptedException {
        logger.entry();
        E element = null;
        while (true) {  // do this in a loop in case there are spurious wakeups (see Object.wait() javadoc)
            int size = list.getNumberOfElements();
            if (size > 0) {
                element = list.removeElementAtIndex(1);
                logger.info("Removed the element: " + element);
                notify();  // waiting addElement() calls
                break;
            } else {
                logger.info("Waiting for an element to be added to the empty queue...");
                wait();  // for an addElement() call
            }
        }
        logger.exit(element);
        return element;
    }


    @Override
    public final synchronized E getHeadElement() {
        logger.entry();
        E element = null;
        int size = list.getNumberOfElements();
        if (size > 0) {
            element = list.getElementAtIndex(1);
        }
        logger.exit();
        return element;
    }

}
