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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import craterdog.collections.abstractions.Collection;
import craterdog.core.Iterator;
import craterdog.core.Manipulator;
import craterdog.collections.abstractions.SortableCollection;
import craterdog.collections.primitives.DynamicArray;
import java.util.Arrays;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a sortable list which performs very well for both inserts and
 * indexed lookups of its values.  The implementation dynamically scales up and down the size of
 * the underlying data structures as the number of associations changes over time. The indexing
 * is unit based and allows positive indexes starting at the beginning of the collection or
 * negative indexes starting at the end of the collection as follows:
 * <pre>
 *         1             2             3               N
 *    [element 1] . [element 2] . [element 3] ... [element N]
 *        -N          -(N-1)        -(N-2)            -1
 * </pre>
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public class List<E> extends SortableCollection<E> {

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
    @JsonCreator
    public List(E[] elements) {
        logger.entry(elements);
        int size = elements.length;
        this.array = new DynamicArray<>(size);
        this.array.addAll(Arrays.asList(elements));
        logger.exit();
    }


    @JsonValue
    @Override
    public E[] toArray() {
        return super.toArray();
    }


    /**
     * This constructor creates a new list using the elements provided in a collection.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public List(Collection<? extends E> elements) {
        logger.entry(elements);
        int size = elements.getSize();
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
    @SuppressWarnings("unchecked")
    public List<E> copy() {
        return (List<E>) super.copy();
    }


    @Override
    public final int getSize() {
        logger.entry();
        int size = array.size();
        logger.exit(size);
        return size;
    }


    @Override
    public final E getElement(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        E element = array.get(index - 1);  // convert to zero based indexing
        logger.exit(element);
        return element;
    }


    @Override
    public final int getIndex(E element) {
        logger.entry(element);
        int index = array.indexOf(element) + 1;  // convert to ordinal based indexing
        logger.exit(index);
        return index;
    }


    @Override
    public final List<E> getElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<E> result = new List<>();
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


    @Override
    public final boolean addElement(E element) {
        logger.entry(element);
        boolean result = array.add(element);
        logger.exit(result);
        return result;
    }


    /**
     * This method inserts the specified element into the collection before the element
     * associated with the specified index.
     *
     * @param element The new element to be inserted into the collection.
     * @param index The index of the element before which the new element is to be inserted.
     */
    public final void insertElement(E element, int index) {
        logger.entry(element, index);
        index = normalizedIndex(index);
        array.add(index - 1, element);  // convert to zero based indexing
        logger.exit();
    }


    /**
     * This method inserts the specified elements into the collection before the element
     * associated with the specified index.  The new elements are inserted in the same
     * order as they appear in the specified list.
     *
     * @param elements The new elements to be inserted into the collection.
     * @param index The index of the element before which the new element is to be inserted.
     */
    public final void insertElements(Iterable<? extends E> elements, int index) {
        logger.entry(elements, index);
        index = normalizedIndex(index);
        Manipulator<E> manipulator = createManipulator();
        manipulator.toIndex(index);
        for (E element : elements) {
            logger.debug("Inserting element: {}", element);
            manipulator.insertElement(element);
        }
        logger.exit();
    }


    /**
     * This method replaces an existing element in the collection with a new one.  The new
     * element replaces the existing element at the specified index.
     *
     * @param element The new element that will replace the existing one.
     * @param index The index of the existing element.
     *
     * @return The element that was at the specified index.
     */
    public final E replaceElement(E element, int index) {
        logger.entry(element, index);
        index = normalizedIndex(index);
        E result = array.set(index - 1, element);  // convert to zero based indexing
        logger.exit(result);
        return result;
    }


    /**
     * This method removes from the collection the element associated with the specified
     * index.
     *
     * @param index The index of the element to be removed.
     * @return The element at the specified index.
     */
    public final E removeElement(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        E result = array.remove(index - 1);  // convert to zero based indexing
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


    /**
     * This method removes from the collection the elements associated with the specified
     * index range.
     *
     * @param firstIndex The index of the first element to be removed.
     * @param lastIndex The index of the last element to be removed.
     * @return The list of the elements that were removed from the collection.
     */
    public final List<E> removeElements(int firstIndex, int lastIndex) {
        logger.entry(firstIndex, lastIndex);
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        if (lastIndex < firstIndex) {
            // handle the case where the indexes are backwards
            int swap = firstIndex;
            firstIndex = lastIndex;
            lastIndex = swap;
        }
        List<E> result = new List<>(array.remove(firstIndex - 1, lastIndex));  // convert to zero based indexing
        logger.exit(result);
        return result;
    }


    @Override
    public final void removeAll() {
        logger.entry();
        array.clear();
        logger.exit();
    }


    @Override
    public Iterator<E> createIterator() {
        logger.entry();
        Iterator<E> iterator = new ListManipulator();
        logger.exit(iterator);
        return iterator;
    }


    @Override
    public Manipulator<E> createManipulator() {
        logger.entry();
        Manipulator<E> manipulator = new ListManipulator();
        logger.exit(manipulator);
        return manipulator;
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
    static public <E> List<E> concatenation(List<E> list1, List<E> list2) {
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
        public void toStart() {
            logger.entry();
            iterator = array.iterator();
            logger.exit();
        }

        @Override
        public void toIndex(int index) {
            logger.entry(index);
            index = normalizedIndex(index);
            iterator = array.listIterator(index - 1);  // convert to zero based indexing
            logger.exit();
        }

        @Override
        public void toEnd() {
            logger.entry();
            iterator = array.listIterator(array.size());  // convert to zero based indexing
            logger.exit();
        }

        @Override
        public boolean hasPrevious() {
            logger.entry();
            boolean result = iterator.nextIndex() > 0;
            logger.exit(result);
            return result;
        }

        @Override
        public boolean hasNext() {
            logger.entry();
            boolean result = iterator.nextIndex() < array.size();
            logger.exit(result);
            return result;
        }

        @Override
        public E getNext() {
            logger.entry();
            if (!hasNext()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the list.");
                throw logger.throwing(exception);
            }
            E result = iterator.next();
            logger.exit(result);
            return result;
        }

        @Override
        public E getPrevious() {
            logger.entry();
            if (!hasPrevious()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the list.");
                throw logger.throwing(exception);
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
        public E removeNext() {
            logger.entry();
            if (!hasNext()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the end of the list.");
                throw logger.throwing(exception);
            }
            E result = iterator.next();
            iterator.remove();
            logger.exit(result);
            return result;
        }

        @Override
        public E removePrevious() {
            logger.entry();
            if (!hasPrevious()) {
                IllegalStateException exception = new IllegalStateException("The iterator is at the beginning of the list.");
                throw logger.throwing(exception);
            }
            E result = iterator.previous();
            iterator.remove();
            logger.exit(result);
            return result;
        }

    }

}
