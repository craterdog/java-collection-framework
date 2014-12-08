/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.abstractions;

import craterdog.collections.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class implements a collection that does not allow its internal structure to be
 * directly manipulated.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by this collection.
 */
public abstract class ClosedCollection<E> extends Collection<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(ClosedCollection.class);

    protected final List<E> list = new List<>();


    @Override
    public Iterator<E> createDefaultIterator() {
        logger.entry();
        Iterator<E> iterator = list.createDefaultIterator();
        logger.exit(iterator);
        return iterator;
    }


    @Override
    public int getNumberOfElements() {
        logger.entry();
        int count = list.getNumberOfElements();
        logger.exit(count);
        return count;
    }


    @Override
    public E getElementAtIndex(int index) {
        logger.entry(index);
        E element = list.getElementAtIndex(index);
        logger.exit(element);
        return element;
    }


    @Override
    public int getIndexOfElement(E element) {
        logger.entry(element);
        int index = list.getIndexOfElement(element);
        logger.exit(index);
        return index;
    }


    @Override
    public Collection<E> getElementsInRange(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        Collection<E> result = list.getElementsInRange(firstIndex, lastIndex);
        logger.exit(result);
        return result;
    }


    @Override
    public void removeAllElements() {
        logger.entry();
        list.removeAllElements();
        logger.exit();
    }

}
