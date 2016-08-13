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
 * This collection class implements a stack (LIFO) data structure.  Attempting to access an
 * entity on an empty stack is considered a bug in the calling code and a runtime exception
 * is thrown.  The implementation dynamically scales up and down the size of the underlying
 * data structures as the number elements changes over time.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by this collection.
 */
public class Stack<E> extends ClosedCollection<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Stack.class);

    private final int capacity;


    /**
     * This constructor creates a new stack of unlimited capacity.
     */
    public Stack() {
        logger.entry();
        this.capacity = Integer.MAX_VALUE;
        logger.exit();
    }


    /**
     * This constructor creates a new stack with the specified capacity.
     *
     * @param capacity The maximum number of elements that can be on the stack
     * at one time.
     */
    public Stack(int capacity) {
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
     * This method pushes a new element onto the top of the stack.
     *
     * @param element The new element to be added.
     */
    public final void pushElement(E element) {
        logger.entry(element);
        if (list.size() < capacity) {
            list.add(element);
        } else {
            IllegalStateException exception = new IllegalStateException("Attempted to push an element onto a full stack.");
            throw logger.throwing(exception);
        }
        logger.exit();
    }


    /**
     * This method pops the top element off of the stack.  If the stack is empty
     * an exception is thrown.
     *
     * @return The top element from the stack.
     */
    public final E popElement() {
        logger.entry();
        E element = null;
        int size = list.size();
        if (size > 0) {
            element = list.remove(size - 1);  // change to zero based indexing
            logger.debug("The top element is: {}", element);
        } else {
            IllegalStateException exception = new IllegalStateException("Attempted to pop the top element of an empty stack.");
            throw logger.throwing(exception);
        }
        logger.exit();
        return element;
    }


    /**
     * This method returns a reference to the top element on the stack without
     * removing it from the stack.  If the stack is empty an exception is thrown.
     *
     * @return The top element on the stack.
     */
    public final E getTop() {
        logger.entry();
        E element = null;
        int size = list.size();
        if (size > 0) {
            element = list.get(size - 1);  // change to zero based indexing
            logger.debug("The top element is: {}", element);
        } else {
            IllegalStateException exception = new IllegalStateException("Attempted to access the top element of an empty stack.");
            throw logger.throwing(exception);
        }
        logger.exit();
        return element;
    }

}
