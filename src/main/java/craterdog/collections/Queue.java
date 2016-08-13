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

import com.fasterxml.jackson.annotation.JsonValue;
import craterdog.collections.abstractions.ClosedCollection;
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
public class Queue<E> extends ClosedCollection<E> {

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


    @JsonValue
    @Override
    public E[] toArray() {
        return super.toArray();
    }


    /**
     * This method adds a new element to the tail of the queue.  If the queue
     * is currently at capacity this method will block until the capacity
     * drops.
     *
     * @param element The new element to be added.
     * @throws java.lang.InterruptedException The thread that was waiting to add an element
     * was interrupted.
     */
    public final synchronized void addElement(E element) throws InterruptedException {
        logger.entry();
        while (true) {  // do this in a loop in case there are spurious wakeups (see Object.wait() javadoc)
            int size = list.size();
            if (size < capacity) {
                logger.debug("Adding the element: " + element);
                list.add(element);
                notify();  // waiting removeElement() calls
                break;
            } else {
                logger.debug("Waiting for the queue to drop from maximum capacity...");
                wait();  // for a removeElement() call
            }
        }
        logger.exit();
    }


    /**
     * This method removes the next element from the head of the queue.  If
     * the queue is empty, the method blocks until an element is available.
     *
     * @return The head element in the queue.
     * @throws java.lang.InterruptedException The thread that was waiting to remove an element
     * was interrupted.
     */
    public final synchronized E removeElement() throws InterruptedException {
        logger.entry();
        E element = null;
        while (true) {  // do this in a loop in case there are spurious wakeups (see Object.wait() javadoc)
            int size = list.size();
            if (size > 0) {
                element = list.remove(0);  // zero based indexing
                logger.debug("Removed the element: " + element);
                notify();  // waiting addElement() calls
                break;
            } else {
                logger.debug("Waiting for an element to be added to the empty queue...");
                wait();  // for an addElement() call
            }
        }
        logger.exit(element);
        return element;
    }


    /**
     * This method returns a reference to the element at the head of the queue
     * without removing the element from the queue.
     *
     * @return The head element in the queue, or null if the queue is empty.
     */
    public final synchronized E getHead() {
        logger.entry();
        E element = null;
        int size = list.size();
        if (size > 0) {
            element = list.get(0);  // zero based indexing
        }
        logger.exit();
        return element;
    }

}
