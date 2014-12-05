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
import craterdog.collections.interfaces.Iteratable;
import craterdog.core.Composite;
import craterdog.utils.NaturalComparator;
import java.util.Comparator;


/**
 * This abstract class defines the invariant methods that all collections must inherit.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class Collection<E> implements Comparable<Iteratable<E>>, Accessible<E> {


    @Override
    public String toString() {
        return toString("");
    }


    @Override
    public String toString(String indentation) {
        // check for an empty collection
        if (isEmpty()) {
            return "[]";
        }

        // see if the collection should be formatted across multiple lines
        boolean isMultiline = checkForMultiline();

        // start formatting the collection
        String nextIndentation = indentation + "    ";
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (isMultiline) {
            addNewLine(builder, nextIndentation);
        }

        // format each element in the collection
        Iterator<E> iterator = this.createDefaultIterator();
        do {
            E element = iterator.getNextElement();
            Composite composite = element instanceof Composite ? (Composite) element : null;
            if (composite != null) {
                builder.append(composite.toString(nextIndentation));
            } else {
                builder.append(element.toString());
            }
            if (iterator.hasNext()) {
                builder.append(",");
                if (isMultiline) {
                    addNewLine(builder, nextIndentation);
                } else {
                    builder.append(" ");
                }
            }
        } while (iterator.hasNext());

        // finish formatting the collection
        if (isMultiline) {
            addNewLine(builder, indentation);
        }
        builder.append("]");

        return builder.toString();
    }


    static private void addNewLine(StringBuilder builder, String indentation) {
            builder.append("\n");
            builder.append(indentation);
    }


    @Override
    public int hashCode() {
        int hash = 5;
        for (E element : this) {
            hash = 11 * hash + element.hashCode();
        }
        return hash;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        @SuppressWarnings("unchecked")
        final Collection<E> that = (Collection<E>) obj;
        if (this.getNumberOfElements() != that.getNumberOfElements()) return false;
        Iterator<E> thisIterator = this.createDefaultIterator();
        Iterator<E> thatIterator = that.createDefaultIterator();
        while (thisIterator.hasNextElement()) {
            E thisElement = thisIterator.getNextElement();
            E thatElement = thatIterator.getNextElement();
            if (!thisElement.equals(thatElement)) return false;
        }
        return true;
    }


    @Override
    public int compareTo(Iteratable<E> that) {
        Comparator<Object> comparator = new NaturalComparator<>();
        Iterator<E> thisIterator = this.createDefaultIterator();
        Iterator<E> thatIterator = that.createDefaultIterator();
        while (thisIterator.hasNextElement() && thatIterator.hasNextElement()) {
            E thisElement = thisIterator.getNextElement();
            E thatElement = thatIterator.getNextElement();
            int result = comparator.compare(thisElement, thatElement);
            if (result != 0) return result;
        }
        return Integer.compare(this.getNumberOfElements(), that.getNumberOfElements());
    }


    @Override
    public final boolean isEmpty() {
        return getNumberOfElements() == 0;
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


    @Override
    public final Iterator<E> iterator() {
        return createDefaultIterator();
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
