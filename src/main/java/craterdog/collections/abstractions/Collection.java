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
import craterdog.core.Composite;
import craterdog.core.Sequential;
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
public abstract class Collection<E> implements Comparable<Collection<E>>, Accessible<E>, Sequential<E>, Composite {

    static private final XLogger logger = XLoggerFactory.getXLogger(Collection.class);


    @Override
    public String toString() {
        logger.entry();
        String string = toString("");
        logger.exit(string);
        return string;
    }


    @Override
    public String toString(String indentation) {
        logger.entry(indentation);
        String string = "[]";  // empty collection

        // check for an empty collection
        if (!isEmpty()) {

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
            string = builder.toString();

        }

        logger.exit(string);
        return string;
    }


    static private void addNewLine(StringBuilder builder, String indentation) {
            builder.append("\n");
            builder.append(indentation);
    }


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
            if (this.getNumberOfElements() == that.getNumberOfElements()) {
                result = true;  // so far anyway...
                Iterator<E> thisIterator = this.createDefaultIterator();
                Iterator<E> thatIterator = that.createDefaultIterator();
                while (thisIterator.hasNextElement()) {
                    E thisElement = thisIterator.getNextElement();
                    E thatElement = thatIterator.getNextElement();
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
        int result;
        Comparator<Object> comparator = new NaturalComparator<>();
        Iterator<E> thisIterator = this.createDefaultIterator();
        Iterator<E> thatIterator = that.createDefaultIterator();
        while (thisIterator.hasNextElement() && thatIterator.hasNextElement()) {
            E thisElement = thisIterator.getNextElement();
            E thatElement = thatIterator.getNextElement();
            result = comparator.compare(thisElement, thatElement);
            if (result != 0) break;
        }
        result = Integer.compare(this.getNumberOfElements(), that.getNumberOfElements());
        logger.exit(result);
        return result;
    }


    @Override
    public boolean isEmpty() {
        logger.entry();
        boolean result = getNumberOfElements() == 0;
        logger.exit(result);
        return result;
    }


    @Override
    public boolean containsElement(E element) {
        logger.entry(element);
        int index = getIndexOfElement(element);
        boolean result = index > 0;
        logger.exit(result);
        return result;
    }


    @Override
    public boolean containsAnyElementsIn(Iterable<? extends E> collection) {
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
    public boolean containsAllElementsIn(Iterable<? extends E> collection) {
        logger.entry(collection);
        boolean result = false;
        for (E element : collection) {
            result = containsElement(element);
            if (!result) break;
        }
        logger.exit(result);
        return result;
    }


    @Override
    @Deprecated
    public void toArray(E[] array) {
        logger.entry(array);
        int size = array.length;
        Iterator<E> iterator = createDefaultIterator();
        for (int index = 0; index < size && iterator.hasNextElement(); index++) {
            array[index] = iterator.getNextElement();
        }
        logger.exit();
    }


    @SuppressWarnings("unchecked")
    @Override
    public E[] toArray() {
        logger.entry();
        E[] array = (E[]) new Object[0];  // OK to use type Object array since it is empty
        int size = this.getNumberOfElements();
        if (size > 0) {
            // Requires a TOTAL HACK since we cannot instantiate a parameterized array explicitly!
            Iterator<E> iterator = createDefaultIterator();
            E template = iterator.getNextElement();  // we know there must be at least one element
            array = (E[]) Array.newInstance(template.getClass(), size);
            array[0] = template;  // copy in the first element
            for (int index = 1; index < size; index++) {
                array[index] = iterator.getNextElement();  // copy the rest of the elements
            }
        }
        logger.exit(array);
        return array;
    }


    @Override
    public final Iterator<E> iterator() {
        logger.entry();
        Iterator<E> iterator = createDefaultIterator();
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
