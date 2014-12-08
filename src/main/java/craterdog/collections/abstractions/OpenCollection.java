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

import craterdog.collections.interfaces.Dynamic;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines the invariant methods that all collections must inherit.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class OpenCollection<E> extends Collection<E> implements Dynamic<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(OpenCollection.class);


    @Override
    public int addElements(E[] elements) {
        logger.entry(elements);
        int count = 0;
        for (E element : elements) {
            if (addElement(element)) count++;
        }
        logger.exit(count);
        return count;
    }


    @Override
    public int addElements(Iterable<? extends E> elements) {
        logger.entry(elements);
        int count = 0;
        for (E element : elements) {
            if (addElement(element)) count++;
        }
        logger.exit(count);
        return count;
    }


    @Override
    public int removeElements(E[] elements) {
        logger.entry(elements);
        int count = 0;
        for (E element : elements) {
            if (removeElement(element)) {
                count++;
            }
        }
        logger.exit(count);
        return count;
    }


    @Override
    public int removeElements(Iterable<? extends E> elements) {
        logger.entry(elements);
        int count = 0;
        for (E element : elements) {
            if (removeElement(element)) {
                count++;
            }
        }
        logger.exit(count);
        return count;
    }


    /**
     * This method converts negative indexes into their corresponding positive indexes and
     * then checks to make sure the index is in the range [1..size].
     *
     * The mapping between indexes is as follows:
     * <pre>
     * Negative Indexes:   -N      -N + 1     -N + 2     -N + 3   ...   -1
     * Positive Indexes:    1         2          3          4     ...    N
     * </pre>
     *
     * @param index The index to be normalized.
     * @return The normalized [1..N] index.
     */
    protected final int normalizedIndex(int index) {
        int size = getNumberOfElements();
        if (index < 0) index = index + size + 1;
        if (index < 1 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return index;
    }

}
