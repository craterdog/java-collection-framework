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
import craterdog.collections.interfaces.LIFO;
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
public class Stack<E> extends ClosedCollection<E> implements LIFO<E> {

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


    @Override
    @SuppressWarnings("unchecked")
    public Stack<E> copy() {
        return (Stack<E>) super.copy();
    }


    @Override
    public final void pushElement(E element) {
        logger.entry(element);
        if (list.getSize() < capacity) {
            list.addElement(element);
        } else {
            IllegalStateException exception = new IllegalStateException("Attempted to push an element onto a full stack.");
            logger.throwing(exception);
            throw exception;
        }
        logger.exit();
    }


    @Override
    public final E popElement() {
        logger.entry();
        E element = null;
        int size = list.getSize();
        if (size > 0) {
            element = list.removeElement(size);
            logger.debug("The top element is: {}", element);
        } else {
            IllegalStateException exception = new IllegalStateException("Attempted to pop the top element of an empty stack.");
            logger.throwing(exception);
            throw exception;
        }
        logger.exit();
        return element;
    }


    @Override
    public final E getTop() {
        logger.entry();
        E element = null;
        int size = list.getSize();
        if (size > 0) {
            element = list.getElement(size);
            logger.debug("The top element is: {}", element);
        } else {
            IllegalStateException exception = new IllegalStateException("Attempted to access the top element of an empty stack.");
            logger.throwing(exception);
            throw exception;
        }
        logger.exit();
        return element;
    }

}
