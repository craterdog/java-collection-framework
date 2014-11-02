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
import craterdog.collections.abstractions.Manipulator;
import craterdog.collections.abstractions.SortableCollection;
import craterdog.collections.abstractions.Sorter;
import craterdog.collections.interfaces.Indexed;
import craterdog.collections.interfaces.Iteratable;
import craterdog.collections.primitives.DynamicArray;
import java.util.Arrays;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a sortable list which performs very well for both inserts and
 * indexed lookups of its values.  The implementation dynamically scales up and down the size of
 * the underlying data structures as the number of associations changes over time.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public class List<E> extends SortableCollection<E> implements Indexed<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(List.class);

    private final DynamicArray<E> array;


    /**
     * This constructor creates a new empty list.
     */
    public List() {
        logger.entry();
        this.array = new DynamicArray<>();
        logger.exit();
    }


    /**
     * This constructor creates a new list using the elements provided in an array.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public List(E[] elements) {
        logger.entry(elements);
        int size = elements.length;
        this.array = new DynamicArray<>(size);
        this.array.addAll(Arrays.asList(elements));
        logger.exit();
    }


    /**
     * This constructor creates a new list using the elements provided in a collection.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public List(Iteratable<? extends E> elements) {
        logger.entry(elements);
        int size = elements.getNumberOfElements();
        this.array = new DynamicArray<>(size);
        for (E element : elements) {
            this.array.add(element);
        }
        logger.exit();
    }


    /**
     * This constructor creates a new list using the elements provided in a java collection.
     *
     * In general it is confusing to mix java collections with craterdog collections since
     * the former uses zero based indexing and the latter uses positive and negative ordinal
     * based indexing.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public List(java.util.Collection<? extends E> elements) {
        logger.entry(elements);
        this.array = new DynamicArray<>(elements);
        logger.exit();
    }


    @Override
    public final int getNumberOfElements() {
        logger.entry();
        int size = array.size();
        logger.exit(size);
        return size;
    }


    @Override
    public final boolean containsElement(E element) {
        logger.entry(element);
        int index = getIndexOfElement(element);
        boolean result = index > 0;
        logger.exit(result);
        return result;
    }


    @Override
    public Iterator<E> createDefaultIterator() {
        logger.entry();
        Iterator<E> iterator = new ListManipulator();
        logger.exit(iterator);
        return iterator;
    }


    @Override
    public final boolean addElement(E element) {
        logger.entry(element);
        boolean result = array.add(element);
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean removeElement(E element) {
        logger.entry(element);
        boolean result = array.remove(element);
        logger.exit(result);
        return result;
    }


    @Override
    public final void removeAllElements() {
        logger.entry();
        array.clear();
        logger.exit();
    }


    @Override
    public final E getElementAtIndex(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        E element = array.get(index - 1);  // convert to zero based indexing
        logger.exit(element);
        return element;
    }


    @Override
    public final int getIndexOfElement(E element) {
        logger.entry(element);
        int index = array.indexOf(element) + 1;  // convert to ordinal based indexing
        logger.exit(index);
        return index;
    }


    @Override
    public final Indexed<? super E> getElementsInRange(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<E> result = new List<>();
        Iterator<E> iterator = createDefaultIterator();
        iterator.goToIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            E element = iterator.getNextElement();
            logger.debug("Including element: {}", element);
            result.addElement(element);
        }
        logger.exit(result);
        return result;
    }


    @Override
    public final void insertElementsBeforeIndex(Iterable<? extends E> elements, int index) {
        logger.entry(elements, index);
        index = normalizedIndex(index);
        Manipulator<E> manipulator = createDefaultManipulator();
        manipulator.goToIndex(index);
        for (E element : elements) {
            logger.debug("Inserting element: {}", element);
            manipulator.insertElement(element);
        }
        logger.exit();
    }


    @Override
    public final Indexed<? super E> removeElementsInRange(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<E> result = new List<>(array.remove(firstIndex - 1, lastIndex - firstIndex + 1));
        logger.exit(result);
        return result;
    }


    @Override
    public Manipulator<E> createDefaultManipulator() {
        logger.entry();
        Manipulator<E> manipulator = new ListManipulator();
        logger.exit(manipulator);
        return manipulator;
    }


    @Override
    protected Sorter<E> sorter() {
        logger.entry();
        Sorter<E> sorter = super.sorter();
        logger.exit(sorter);
        return sorter;
    }


    @Override
    public final void insertElementBeforeIndex(E element, int index) {
        logger.entry(element, index);
        index = normalizedIndex(index);
        array.add(index - 1, element);  // convert to zero based indexing
        logger.exit();
    }


    @Override
    public final E replaceElementAtIndex(E element, int index) {
        logger.entry(element, index);
        index = normalizedIndex(index);
        E result = array.set(index - 1, element);  // convert to zero based indexing
        logger.exit(result);
        return result;
    }


    @Override
    public final E removeElementAtIndex(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        E result = array.remove(index - 1);  // convert to zero based indexing
        logger.exit(result);
        return result;
    }


    /**
     * This function returns a new list that contains the all the elements from
     * both the specified lists.
     *
     * @param <E> The type of element contained in the lists.
     * @param list1 The first list whose elements are to be added.
     * @param list2 The second list whose elements are to be added.
     * @return The resulting list.
     */
    static public <E> List<E> concatenate(List<E> list1, List<E> list2) {
        logger.entry(list1, list2);
        List<E> result = new List<>(list1);
        result.addElements(list2);
        logger.exit(result);
        return result;
    }


    /*
     * This manipulator class implements both the <code>Iterator</code> abstraction
     * and the <code>Manipulator</code> abstraction so it can be used as either depending
     * on how it is returned from the list.  It utilizes the list iterators for the
     * underlying list implementations.  Like most iterators, it should be used to access
     * a list exclusively without any other requests, especially requests that change the
     * length of the list, being made directly on the same list.  None of the manipulator
     * methods will cause the implementation of the list to be switched to a different
     * implementation.
     */
    private class ListManipulator extends Manipulator<E> {

        private java.util.ListIterator<E> iterator = array.iterator();

        @Override
        public void goToStart() {
            logger.entry();
            iterator = array.iterator();
            logger.exit();
        }

        @Override
        public void goToIndex(int index) {
            logger.entry(index);
            index = normalizedIndex(index);
            iterator = array.listIterator(index - 1);  // convert to zero based indexing
            logger.exit();
        }

        @Override
        public void goToEnd() {
            logger.entry();
            iterator = array.listIterator(array.size());  // convert to zero based indexing
            logger.exit();
        }

        @Override
        public boolean hasPreviousElement() {
            logger.entry();
            boolean result = iterator.nextIndex() > 0;
            logger.exit(result);
            return result;
        }

        @Override
        public boolean hasNextElement() {
            logger.entry();
            boolean result = iterator.nextIndex() < array.size();
            logger.exit(result);
            return result;
        }

        @Override
        public E getNextElement() {
            logger.entry();
            if (!hasNextElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the list.");
                logger.throwing(exception);
                throw exception;
            }
            E result = iterator.next();
            logger.exit(result);
            return result;
        }

        @Override
        public E getPreviousElement() {
            logger.entry();
            if (!hasPreviousElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the list.");
                logger.throwing(exception);
                throw exception;
            }
            E result = iterator.previous();
            logger.exit(result);
            return result;
        }

        @Override
        public void insertElement(E element) {
            logger.entry(element);
            iterator.add(element);
            logger.exit();
        }

        @Override
        public E removeNextElement() {
            logger.entry();
            if (!hasNextElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the list.");
                logger.throwing(exception);
                throw exception;
            }
            E result = iterator.next();
            iterator.remove();
            logger.exit(result);
            return result;
        }

        @Override
        public E removePreviousElement() {
            logger.entry();
            if (!hasPreviousElement()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the list.");
                logger.throwing(exception);
                throw exception;
            }
            E result = iterator.previous();
            iterator.remove();
            logger.exit(result);
            return result;
        }

    }

}
