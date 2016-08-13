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

import craterdog.core.Iterator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class implements a collection that allows its internal structure to be
 * directly manipulated.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class OpenCollection<E> extends Collection<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(OpenCollection.class);


    @Override
    public Collection<E> getElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        OpenCollection<E> result = emptyCopy();
        Iterator<E> iterator = createIterator();
        iterator.toIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            E element = iterator.getNext();
            logger.debug("Including element: {}", element);
            result.addElement(element);
        }
        logger.exit(result);
        return result;
    }


    /**
     * This method adds a new element to the collection.  If the collection
     * is empty then the new element will be the first element in the collection.
     *
     * @param element The new element to be added.
     * @return Whether or not the element was actually added to the collection.
     */
    public abstract boolean addElement(E element);


    /**
     * This method adds an array of new elements to the collection.  The new
     * elements will be added in the order they appear in the specified array.
     *
     * @param elements The array of new elements to be added.
     * @return The number of elements that were actually added to the collection.
     */
    public int addElements(E[] elements) {
        logger.entry(elements);
        int count = 0;
        for (E element : elements) {
            if (addElement(element)) count++;
        }
        logger.exit(count);
        return count;
    }


    /**
     * This method adds a list of new elements to the collection.  The new
     * elements will be added in the order they appear in the specified collection.
     *
     * @param elements The list of new elements to be added.
     * @return The number of elements that were actually added to the collection.
     */
    public int addElements(Iterable<? extends E> elements) {
        logger.entry(elements);
        int count = 0;
        for (E element : elements) {
            if (addElement(element)) count++;
        }
        logger.exit(count);
        return count;
    }


    /**
     * This method removes the specified element from the collection.  If the element
     * is not found in the collection, no changes are made.
     *
     * @param element The element to be removed.
     * @return Whether or not the element was found.
     */
    public abstract boolean removeElement(E element);


    /**
     * This method removes the elements in the specified array from the collection.
     * The number of matching elements is returned.
     *
     * @param elements The array of elements to be removed from the collection.
     * @return The number of elements that were actually removed.
     */
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


    /**
     * This method removes the specified elements from the collection.  The number of
     * matching elements is returned.
     *
     * @param elements The list of elements to be removed from the collection.
     * @return The number of elements that were actually removed.
     */
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
     * This protected method must be implemented by all concrete open collections
     * and pass back an empty concrete collection.
     *
     * @param <T> The concrete type of open collection.
     * @return An empty concrete open collection.
     */
    protected abstract <T extends OpenCollection<E>> T emptyCopy();

}
