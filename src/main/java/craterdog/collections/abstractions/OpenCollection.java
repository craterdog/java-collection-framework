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


/**
 * This abstract class defines the invariant methods that all collections must inherit.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class OpenCollection<E> extends Collection<E> implements Accessible<E> {

    @Override
    public final boolean containsAnyElementsIn(Iterable<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result = containsElement(element);
            if (result) break;
        }
        return result;
    }


    @Override
    public final boolean containsAllElementsIn(Iterable<? extends E> collection) {
        boolean result = false;
        for (E element : collection) {
            result = containsElement(element);
            if (!result) break;
        }
        return result;
    }


    @Override
    public final int addElements(E[] elements) {
        int count = 0;
        for (E element : elements) {
            if (addElement(element)) count++;
        }
        return count;
    }


    @Override
    public final int addElements(Iterable<? extends E> elements) {
        int count = 0;
        for (E element : elements) {
            if (addElement(element)) count++;
        }
        return count;
    }


    @Override
    public final int removeElements(E[] elements) {
        int counter = 0;
        for (E element : elements) {
            if (removeElement(element)) {
                counter++;
            }
        }
        return counter;
    }


    @Override
    public final int removeElements(Iterable<? extends E> elements) {
        int counter = 0;
        for (E element : elements) {
            if (removeElement(element)) {
                counter++;
            }
        }
        return counter;
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


    private boolean checkForMultiline() {
        boolean isMultiline = false;
        for (E element : this) {
            if (element instanceof String && getNumberOfElements() > 3 ||
                    ! (element instanceof Number ||
                       element instanceof Boolean ||
                       element instanceof Character)) {
                isMultiline = true;
                break;
            }
        }
        return isMultiline;
    }

}
