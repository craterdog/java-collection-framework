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

import craterdog.collections.List;
import craterdog.collections.primitives.LinkedList;
import craterdog.core.Iterator;
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

    protected final LinkedList<E> list = new LinkedList<>();


    @Override
    public int getSize() {
        logger.entry();
        int count = list.size();
        logger.exit(count);
        return count;
    }


    @Override
    public int getIndex(E element) {
        logger.entry(element);
        int index = list.indexOf(element) + 1;  // change to unit based indexing
        logger.exit(index);
        return index;
    }


    @Override
    public E getElement(int index) {
        logger.entry(index);
        index = normalizedIndex(index) - 1;  // change to zero based indexing
        E element = list.get(index);
        logger.exit(element);
        return element;
    }


    @Override
    public Collection<E> getElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex) - 1;  // change to zero based indexing
        lastIndex = normalizedIndex(lastIndex) - 1;  // change to zero based indexing
        List<E> results = new List<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            results.addElement(list.get(i));
        }
        logger.exit(results);
        return results;
    }


    @Override
    public void removeAll() {
        logger.entry();
        list.clear();
        logger.exit();
    }


    @Override
    public Iterator<E> createIterator() {
        logger.entry();
        Iterator<E> iterator = list.iterator();
        logger.exit(iterator);
        return iterator;
    }

}
