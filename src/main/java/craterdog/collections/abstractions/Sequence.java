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
import craterdog.core.Sequential;
import craterdog.smart.SmartObject;
import craterdog.utils.NaturalComparator;
import java.lang.reflect.Array;
import java.util.Comparator;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This abstract class defines the invariant methods that all sequences must inherit.
 *
 * @author Derk Norton
 * @param <E> The type of elements that are in the sequence.
 */
public abstract class Sequence<E> extends SmartObject<Sequence<E>>
        implements Comparable<Sequence<E>>, Sequential<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(Sequence.class);


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
            final Sequence<E> that = (Sequence<E>) object;
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
    public int compareTo(Sequence<E> that) {
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

}
