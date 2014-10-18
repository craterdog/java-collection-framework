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

import craterdog.collections.abstractions.*;
import craterdog.collections.interfaces.Indexed;
import craterdog.collections.interfaces.Iteratable;
import java.util.Arrays;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;


/**
 * This collection class implements a self-optimizing list which monitors its access
 * patterns and adjusts the underlying implementation accordingly.  The implementation
 * decision is between a dynamic array and a doubly linked list based on the probability
 * of receiving an access request, which doesn't change the size of the list, and the
 * probability of receiving a change request, which does.
 *
 * The probabilities are calculated based on the previous 100 requests:
 * <pre>
 *  * P(access) : the probability that the next request will be an access only request
 *  * P(change) : the probability that the next request will be a request that changes the size of the list
 * </pre>
 *
 * The cost (C) for each type of implementation is then calculated based on the current
 * size (N) of the list and the above probabilities as follows:
 * <pre>
 *  * C(array) =  P(access) + N * P(change)
 *  * C(list) = N * P(access) / 2 + P(change)
 * </pre>
 *
 * The above equations are based on the following big O characteristics for the two types
 * of implementations:
 *
 * <pre>
 *    ACCESS TYPE           DYNAMIC ARRAY      LINKED LIST
 *    Indexed Access             O(1)            O(N/2)
 *    Change in Size             O(N)            O(1)
 * </pre>
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public class List<E> extends SortableCollection<E> implements Indexed<E> {

    static private final XLogger logger = XLoggerFactory.getXLogger(List.class);

    // remaining requests before next reassessment
    static private final int TIMER_SET_POINT = 10;
    private int timer = TIMER_SET_POINT;

    // ratio [0..100] of access requests to change requests for the last 100 requests
    private int acRatio;

    // the current implementation
    private java.util.List<E> list;
    private ListType type;

    static private enum ListType {
        EMPTY,
        ARRAY,
        LIST
    }


    /**
     * This constructor creates a new empty list.
     */
    public List() {
        logger.entry();
        // high probability that the next requests will be a insertion of an elements so go with linked list
        acRatio = 10;  // guess that only 10 percent of the requests will likely be access requests
        type = ListType.EMPTY;
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
        if (size == 0) {
            // same as default constructor
            acRatio = 10;  // guess that only 10 percent of the requests will likely be access requests
            type = ListType.EMPTY;
        } else {
            // high probability that the next requests will be indexed reads so go with array
            acRatio = 90;  // guess that 90 percent of the requests will likely be access requests
            list = new java.util.ArrayList<>(size);
            type = ListType.ARRAY;
            list.addAll(Arrays.asList(elements));
        }
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
        if (size == 0) {
            // same as default constructor
            acRatio = 10;  // guess that only 10 percent of the requests will likely be access requests
            type = ListType.EMPTY;
        } else {
            // high probability that the next requests will be indexed reads so go with array
            acRatio = 90;  // guess that 90 percent of the requests will likely be access requests
            list = new java.util.ArrayList<>(size);
            type = ListType.ARRAY;
            for (E element : elements) {
                list.add(element);
            }
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
        int size = elements.size();
        if (size == 0) {
            // same as default constructor
            acRatio = 10;  // only 10 percent of the requests will likely be access requests
            type = ListType.EMPTY;
            logger.debug("The list is empty.");
        } else {
            // high probability that the next requests will be indexed reads so go with array
            acRatio = 90;  // 90 percent of the requests will likely be access requests
            list = new java.util.ArrayList<>(size);
            type = ListType.ARRAY;
            list.addAll(elements);
            logger.debug("Starting with a dynamic array based list.");
        }
        logger.exit();
    }


    @Override
    public final int getNumberOfElements() {
        logger.entry();
        int size = type == ListType.EMPTY ? 0 : list.size();
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
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
            logger.debug("Changing to a linked list based list.");
        }
        Iterator<E> iterator = new ListManipulator();
        logger.exit(iterator);
        return iterator;
    }


    @Override
    public final boolean addElement(E element) {
        logger.entry(element);
        acRatio--;
        if (--timer == 0) reassessImplementation();
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
            logger.debug("Changing to a linked list based list.");
        }
        boolean result = list.add(element);
        logger.exit(result);
        return result;
    }


    @Override
    public final boolean removeElement(E element) {
        logger.entry(element);
        acRatio--;
        if (--timer == 0) reassessImplementation();
        boolean result = false;
        if (type != ListType.EMPTY) {
            result = list.remove(element);
        }
        logger.exit(result);
        return result;
    }


    @Override
    public final void removeAllElements() {
        logger.entry();
        if (--timer == 0) reassessImplementation();
        if (type != ListType.EMPTY) {
            list = null;
            type = ListType.EMPTY;
            logger.debug("Changing to an empty list.");
        }
        logger.exit();
    }


    @Override
    public final E getElementAtIndex(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        acRatio++;
        if (--timer == 0) reassessImplementation();
        E element = type == ListType.EMPTY ? null : list.get(index - 1);
        logger.exit(element);
        return element;
    }


    @Override
    public final int getIndexOfElement(E element) {
        logger.entry(element);
        if (--timer == 0) reassessImplementation();
        if (type == ListType.EMPTY) {
            logger.exit(0);
            return 0;
        }
        int index = list.indexOf(element);
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
        List<E> result = new List<>();
        Manipulator<E> manipulator = createDefaultManipulator();
        manipulator.goToIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            E element = manipulator.removeNextElement();
            logger.debug("Removing element: {}", element);
            result.addElement(element);
        }
        logger.exit(result);
        return result;
    }


    @Override
    public Manipulator<E> createDefaultManipulator() {
        logger.entry();
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
            logger.debug("Changing to a linked list based list.");
        }
        Manipulator<E> manipulator = new ListManipulator();
        logger.exit(manipulator);
        return manipulator;
    }


    @Override
    protected Sorter<E> sorter() {
        logger.entry();
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
            logger.debug("Changing to a linked list based list.");
        }
        Sorter<E> sorter = super.sorter();
        logger.exit(sorter);
        return sorter;
    }


    @Override
    public final void insertElementBeforeIndex(E element, int index) {
        logger.entry(element, index);
        index = normalizedIndex(index);
        acRatio--;
        if (--timer == 0) reassessImplementation();
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
            logger.debug("Changing to a linked list based list.");
        }
        list.add(index - 1, element);
        logger.exit();
    }


    @Override
    public final E replaceElementAtIndex(E element, int index) {
        logger.entry(element, index);
        index = normalizedIndex(index);
        acRatio++;
        if (--timer == 0) reassessImplementation();
        E result = list.set(index - 1, element);
        logger.exit(result);
        return result;
    }


    @Override
    public final E removeElementAtIndex(int index) {
        logger.entry(index);
        index = normalizedIndex(index);
        acRatio--;
        if (--timer == 0) reassessImplementation();
        E result = list.remove(index - 1);
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
     * This method uses the following performance characteristics to reassess the current
     * list implemenation choice and change it if a better one is available.
     *
     *    ACCESS TYPE           DYNAMIC ARRAY      LINKED LIST
     *    Indexed Access            O(1)              O(N/2)
     *    Change in Size            O(N)              O(1)
     *    Scan/Search               O(N/2)            O(N/2)
     *
     */
    private void reassessImplementation() {
        logger.entry();
        timer = TIMER_SET_POINT;
        int size = getNumberOfElements();
        if (size == 0) return;

        normalizeRatio();
        int caRatio = 100 - acRatio;
        int arrayCost = acRatio + size * caRatio;
        int listCost = size * acRatio / 2 + caRatio;

        if (arrayCost < listCost) {
            // array is best
            if (type != ListType.ARRAY) {
                list = new java.util.ArrayList<>(list);
                type = ListType.ARRAY;
                logger.debug("Changing to a dynamic array based list.");
            }
        } else {  // listCost <= arrayCost
            // list is best
            if (type != ListType.LIST) {
                list = new java.util.LinkedList<>(list);
                type = ListType.LIST;
                logger.debug("Changing to a linked list based list.");
            }
        }
        logger.exit();
    }


    /*
     * This method makes sure that all counters stay in the range of [0..200] so that each
     * maps to the probability of that type of request occurring based on the past 100
     * requests.  Integers are used instead of actual probabilities to increase performance.
     */
    private void normalizeRatio() {
        logger.entry();
        acRatio = Math.max(0, acRatio);
        acRatio = Math.min(acRatio, 100);
        logger.exit();
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

        private java.util.ListIterator<E> iterator = list.listIterator();

        @Override
        public void goToStart() {
            logger.entry();
            iterator = list.listIterator();
            logger.exit();
        }

        @Override
        public void goToIndex(int index) {
            logger.entry(index);
            index = normalizedIndex(index);
            iterator = list.listIterator(index - 1);
            logger.exit();
        }

        @Override
        public void goToEnd() {
            logger.entry();
            iterator = list.listIterator(list.size());
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
            boolean result = iterator.nextIndex() < list.size();
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
