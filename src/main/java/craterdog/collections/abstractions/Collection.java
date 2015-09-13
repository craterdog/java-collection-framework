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

import craterdog.collections.interfaces.Accessible;
import craterdog.core.Sequential;
import craterdog.smart.SmartObject;
import craterdog.utils.NaturalComparator;
import java.lang.reflect.Array;
import java.util.Comparator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines the invariant methods that all collections must inherit.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class Collection<E> extends SmartObject<Collection<E>>
        implements Comparable<Collection<E>>, Accessible<E>, Sequential<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Collection.class);


    @Override
    public int hashCode() {
        logger.entry();
        int hash = 5;
        for (E element : this) {
            hash = 11 * hash + element.hashCode();
        }
        logger.exit(hash);
        return hash;
    }


    @Override
    public boolean equals(Object object) {
        logger.entry(object);
        boolean result = false;
        if (object != null && getClass() == object.getClass()) {
            @SuppressWarnings("unchecked")
            final Collection<E> that = (Collection<E>) object;
            if (this.getSize() == that.getSize()) {
                result = true;  // so far anyway...
                Iterator<E> thisIterator = this.createIterator();
                Iterator<E> thatIterator = that.createIterator();
                while (thisIterator.hasNext()) {
                    E thisElement = thisIterator.getNext();
                    E thatElement = thatIterator.getNext();
                    if (!thisElement.equals(thatElement)) {
                        result = false;  // oops, found a difference
                        break;
                    }
                }
            }
        }
        logger.exit(result);
        return result;
    }


    @Override
    public int compareTo(Collection<E> that) {
        logger.entry(that);
        if (that == null) return 1;
        if (this == that) return 0;  // same object
        int result = 0;
        Comparator<Object> comparator = new NaturalComparator<>();
        Iterator<E> thisIterator = this.createIterator();
        Iterator<E> thatIterator = that.createIterator();
        while (thisIterator.hasNext() && thatIterator.hasNext()) {
            E thisElement = thisIterator.getNext();
            E thatElement = thatIterator.getNext();
            result = comparator.compare(thisElement, thatElement);
            if (result != 0) break;
        }
        if (result == 0) {
            // same so far, check for different lengths
            result = Integer.compare(this.getSize(), that.getSize());
        }
        logger.exit(result);
        return result;
    }


    @Override
    public int getNumberOfElements() {
        logger.entry();
        int result = getSize();
        logger.exit(result);
        return result;
    }


    @Override
    public boolean isEmpty() {
        logger.entry();
        boolean result = getSize() == 0;
        logger.exit(result);
        return result;
    }


    @Override
    public boolean containsElement(E element) {
        logger.entry(element);
        int index = getIndex(element);
        boolean result = index > 0;
        logger.exit(result);
        return result;
    }


    @Override
    public boolean containsAny(Iterable<? extends E> collection) {
        logger.entry(collection);
        boolean result = false;
        for (E element : collection) {
            result = containsElement(element);
            if (result) break;
        }
        logger.exit(result);
        return result;
    }


    @Override
    public boolean containsAll(Iterable<? extends E> collection) {
        logger.entry(collection);
        boolean result = false;
        for (E element : collection) {
            result = containsElement(element);
            if (!result) break;
        }
        logger.exit(result);
        return result;
    }


    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        logger.entry();
        E[] array = (E[]) new Object[0];  // OK to use type Object array since it is empty
        int size = this.getSize();
        if (size > 0) {
            // Requires a TOTAL HACK since we cannot instantiate a parameterized array explicitly!
            Iterator<E> iterator = createIterator();
            E template = iterator.getNext();  // we know there must be at least one element
            array = (E[]) Array.newInstance(template.getClass(), size);
            array[0] = template;  // copy in the first element
            for (int index = 1; index < size; index++) {
                array[index] = iterator.getNext();  // copy the rest of the elements
            }
        }
        logger.exit(array);
        return array;
    }


    @Override
    public final Iterator<E> iterator() {
        logger.entry();
        Iterator<E> iterator = createIterator();
        logger.exit(iterator);
        return iterator;
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
        int size = getSize();
        if (index < 0) index = index + size + 1;
        if (index < 1 || index > size) throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        return index;
    }

}