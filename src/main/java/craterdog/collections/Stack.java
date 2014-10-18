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

import craterdog.collections.abstractions.Iterator;
import craterdog.collections.interfaces.LIFO;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a stack (LIFO) data structure.  Attempting to access an
 * entity on an empty stack is considered a bug in the calling code and a runtime exception
 * is thrown.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by this collection.
 */
public final class Stack<E> implements LIFO<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Stack.class);

    private final List<E> list;


    /**
     * This constructor creates a new empty stack.
     */
    public Stack() {
        logger.entry();
        this.list = new List<>();
        logger.exit();
    }


    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }


    @Override
    public int getNumberOfElements() {
        return list.getNumberOfElements();
    }


    @Override
    public Iterator<E> createDefaultIterator() {
        return list.createDefaultIterator();
    }


    @Override
    public final void toArray(E[] array) {
        int size = array.length;
        Iterator<E> iterator = createDefaultIterator();
        for (int index = 0; index < size && iterator.hasNextElement(); index++) {
            array[index] = iterator.getNextElement();
        }
    }


    @Override
    public void pushTopElement(E element) {
        logger.entry();
        list.addElement(element);
        logger.exit();
    }


    @Override
    public E popTopElement() {
        logger.entry();
        E element = null;
        int size = list.getNumberOfElements();
        if (size > 0) {
            element = list.removeElementAtIndex(size);
        } else {
            RuntimeException exception = new RuntimeException("Attempted to pop the top element of an empty stack.");
            logger.throwing(exception);
            throw exception;
        }
        logger.exit();
        return element;
    }


    @Override
    public void removeAllElements() {
        logger.entry();
        list.removeAllElements();
        logger.exit();
    }


    @Override
    public E getTopElement() {
        logger.entry();
        E element = null;
        int size = list.getNumberOfElements();
        if (size > 0) {
            element = list.getElementAtIndex(size);
        } else {
            RuntimeException exception = new RuntimeException("Attempted to access the top element of an empty stack.");
            logger.throwing(exception);
            throw exception;
        }
        logger.exit();
        return element;
    }


    @Override
    public final Iterator<E> iterator() {
        return createDefaultIterator();
    }


    @Override
    public String toString() {
        return toString("");
    }


    @Override
    public String toString(String indentation) {
        return list.toString(indentation);
    }

}
