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
public final class List<E> extends SortableCollection<E> implements Indexed<E> {

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
        // high probability that the next requests will be a insertion of an elements so go with linked list
        acRatio = 10;  // guess that only 10 percent of the requests will likely be access requests
        type = ListType.EMPTY;
    }


    /**
     * This constructor creates a new list using the elements provided in an array.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public List(E[] elements) {
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
    }


    /**
     * This constructor creates a new list using the elements provided in a collection.
     *
     * @param elements The elements that should be used to seed the list.
     */
    public List(Iteratable<? extends E> elements) {
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
        int size = elements.size();
        if (size == 0) {
            // same as default constructor
            acRatio = 10;  // only 10 percent of the requests will likely be access requests
            type = ListType.EMPTY;
        } else {
            // high probability that the next requests will be indexed reads so go with array
            acRatio = 90;  // 90 percent of the requests will likely be access requests
            list = new java.util.ArrayList<>(size);
            type = ListType.ARRAY;
            list.addAll(elements);
        }
    }


    @Override
    public int getNumberOfElements() {
        int size = type == ListType.EMPTY ? 0 : list.size();
        return size;
    }


    @Override
    public final boolean containsElement(E element) {
        int index = getIndexOfElement(element);
        boolean result = index > 0;
        return result;
    }


    @Override
    public Iterator<E> createDefaultIterator() {
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
        }
        Iterator<E> iterator = new ListManipulator();
        return iterator;
    }


    @Override
    public boolean addElement(E element) {
        acRatio--;
        if (--timer == 0) reassessImplementation();
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
        }
        boolean result = list.add(element);
        return result;
    }


    @Override
    public boolean removeElement(E element) {
        acRatio--;
        if (--timer == 0) reassessImplementation();
        boolean result = false;
        if (type != ListType.EMPTY) {
            result = list.remove(element);
        }
        return result;
    }


    @Override
    public void removeAllElements() {
        if (--timer == 0) reassessImplementation();
        if (type != ListType.EMPTY) {
            list = null;
            type = ListType.EMPTY;
        }
    }


    @Override
    public E getElementAtIndex(int index) {
        index = normalizedIndex(index);
        acRatio++;
        if (--timer == 0) reassessImplementation();
        E element = type == ListType.EMPTY ? null : list.get(index - 1);
        return element;
    }


    @Override
    public int getIndexOfElement(E element) {
        if (--timer == 0) reassessImplementation();
        if (type == ListType.EMPTY) {
            return 0;
        }
        int index = list.indexOf(element);
        return index;
    }


    @Override
    public final Indexed<? super E> getElementsInRange(int firstIndex, int lastIndex) {
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<E> result = new List<>();
        Iterator<E> iterator = createDefaultIterator();
        iterator.goToIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            E element = iterator.getNextElement();
            result.addElement(element);
        }
        return result;
    }


    @Override
    public final void insertElementsBeforeIndex(Iteratable<? extends E> elements, int index) {
        index = normalizedIndex(index);
        Manipulator<E> manipulator = createDefaultManipulator();
        manipulator.goToIndex(index);
        for (E element : elements) {
            manipulator.insertElement(element);
        }
    }


    @Override
    public final Indexed<? super E> removeElementsInRange(int firstIndex, int lastIndex) {
        firstIndex = normalizedIndex(firstIndex);
        lastIndex = normalizedIndex(lastIndex);
        List<E> result = new List<>();
        Manipulator<E> manipulator = createDefaultManipulator();
        manipulator.goToIndex(firstIndex);
        int numberOfElements = lastIndex - firstIndex + 1;
        while (numberOfElements-- > 0) {
            E element = manipulator.removeNextElement();
            result.addElement(element);
        }
        return result;
    }


    @Override
    public Manipulator<E> createDefaultManipulator() {
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
        }
        Manipulator<E> manipulator = new ListManipulator();
        return manipulator;
    }


    @Override
    protected Sorter<E> sorter() {
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
        }
        Sorter<E> sorter = super.sorter();
        return sorter;
    }


    @Override
    public void insertElementBeforeIndex(E element, int index) {
        index = normalizedIndex(index);
        acRatio--;
        if (--timer == 0) reassessImplementation();
        if (type == ListType.EMPTY) {
            list = new java.util.LinkedList<>();
            type = ListType.LIST;
        }
        list.add(index - 1, element);
    }


    @Override
    public E replaceElementAtIndex(E element, int index) {
        index = normalizedIndex(index);
        acRatio++;
        if (--timer == 0) reassessImplementation();
        E result = list.set(index - 1, element);
        return result;
    }


    @Override
    public E removeElementAtIndex(int index) {
        index = normalizedIndex(index);
        acRatio--;
        if (--timer == 0) reassessImplementation();
        E result = list.remove(index - 1);
        return result;
    }


    /*
     * This method uses the following performance characteristics to reassess the current
     * list implemenation choice and change it if a better one is available.
     *
     *    ACCESS TYPE           DYNAMIC ARRAY      LINKED LIST
     *    Indexed Access             O(1)            O(N/2)
     *    Change in Size             O(N)            O(1)
     *    Scan/Search                O(N/2)          O(N/2)
     *
     */
    private void reassessImplementation() {
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
            }
        } else {  // listCost <= arrayCost
            // list is best
            if (type != ListType.LIST) {
                list = new java.util.LinkedList<>(list);
                type = ListType.LIST;
            }
        }
    }


    /*
     * This method makes sure that all counters stay in the range of [0..200] so that each
     * maps to the probability of that type of request occurring based on the past 100
     * requests.  Integers are used instead of actual probabilities to increase performance.
     */
    private void normalizeRatio() {
        acRatio = Math.max(0, acRatio);
        acRatio = Math.min(acRatio, 100);
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
            iterator = list.listIterator();
        }

        @Override
        public void goToIndex(int index) {
            index = normalizedIndex(index);
            iterator = list.listIterator(index - 1);
        }

        @Override
        public void goToEnd() {
            iterator = list.listIterator(list.size());
        }

        @Override
        public boolean hasPreviousElement() {
            return iterator.nextIndex() > 0;
        }

        @Override
        public boolean hasNextElement() {
            return iterator.nextIndex() < list.size();
        }

        @Override
        public E getNextElement() {
            if (!hasNextElement()) throw new IllegalStateException("The iterator is at the end of the list.");
            return iterator.next();
        }

        @Override
        public E getPreviousElement() {
            if (!hasPreviousElement()) throw new IllegalStateException("The iterator is at the beginning of the list.");
            return iterator.previous();
        }

        @Override
        public void insertElement(E element) {
            iterator.add(element);
        }

        @Override
        public E removeNextElement() {
            if (!hasNextElement()) throw new IllegalStateException("The iterator is at the end of the list.");
            E result = iterator.next();
            iterator.remove();
            return result;
        }

        @Override
        public E removePreviousElement() {
            if (!hasPreviousElement()) throw new IllegalStateException("The iterator is at the beginning of the list.");
            E result = iterator.previous();
            iterator.remove();
            return result;
        }

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
        List<E> result = new List<>(list1);
        result.addElements(list2);
        return result;
    }

}
