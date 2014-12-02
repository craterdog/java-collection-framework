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

import craterdog.collections.List;
import craterdog.collections.interfaces.Sortable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;


/**
 * This abstract class defines the invariant methods that all sortable collections must inherit.
 * A sortable collection allows the order of its elements to be determined externally.  By
 * default, the elements will be placed in the order in which they were added to the collection.
 * Additionally, the elements can be sorted in various ways depending on a specified sorting
 * algorithm and comparison function.
 *
 * @author Derk Norton
 * @param <E> The type of element managed by the collection.
 */
public abstract class SortableCollection<E> extends Collection<E> implements Sortable<E> {


    @Override
    public final void sortElements(Comparator<? super E> comparator) {
        Sorter<E> sorter = createDefaultSorter();
        sorter.sortCollection(this, comparator);
    }


    @Override
    public final void sortElements(Sorter<E> sorter, Comparator<? super E> comparator) {
        sorter.sortCollection(this, comparator);
    }


    /**
     * This method returns a new collection that is the concatenation of this collection with
     * the specified collection.
     *
     * @param <E> The type of element contained in the collections.
     * @param collection1 The first collection to be concatenated.
     * @param collection2 The second collection to be concatenated.
     * @return The resulting collection.
     */
    static public <E> SortableCollection<E> concatenate(SortableCollection<E> collection1, SortableCollection<E> collection2) {
        List<E> result = new List<>(collection1);
        result.addElements(collection2);
        return result;
    }


    /**
     * This method returns a createDefaultSorter that can be used by any subclass to sort its elements.
     *
     * @return The default createDefaultSorter algorithm.
     */
    protected Sorter<E> createDefaultSorter() {
        return new MergeSorter();
    }


    /*
     * This createDefaultSorter class uses a standard merge sort algorithm.  The collection is recursively split into
     * two collections each of which are then sorted and then the two collections are merged back into a
     * sorted collection.
     */
    private class MergeSorter extends Sorter<E> {

        @Override
        public void sortCollection(SortableCollection<E> collection, Comparator<? super E> comparator) {
            // see if any sorting is needed
            int size = collection.getNumberOfElements();
            if (size > 1) {

                // convert it to an array
                Iterator<E> iterator = collection.createDefaultIterator();
                E template = iterator.getNextElement();  // TOTAL HACK but java requires a template to use for allocating a new array
                @SuppressWarnings("unchecked")
                E[] array = (E[]) Array.newInstance(template.getClass(), size);
                collection.toArray(array);

                // sort the array
                sortList(array, comparator);

                // convert it back to a collection
                collection.removeAllElements();
                collection.addElements(array);

            }
        }

        private void sortList(E[] list, Comparator<? super E> comparator) {
            // check to see if the list is already sorted
            int length = list.length;
            if (length < 2) return;

            // split the list into two halves
            int leftLength = length / 2;
            E[] left = Arrays.copyOfRange(list, 0, leftLength);
            E[] right = Arrays.copyOfRange(list, leftLength, length);

            // sort each half separately
            sortList(left, comparator);
            sortList(right, comparator);

            // merge the sorted halves back together
            mergeLists(left, right, list, comparator);
        }

        private void mergeLists(E[] left, E[] right, E[] result, Comparator<? super E> comparator) {
            int leftIndex = 0;
            int rightIndex = 0;
            int resultIndex = 0;
            while (resultIndex < result.length) {
                if (leftIndex < left.length && rightIndex < right.length) {
                    // still have elements in both halves
                    if (compareElements(comparator, left[leftIndex], right[rightIndex]) < 0) {
                        // copy the next left element to the result
                        result[resultIndex++] = left[leftIndex++];
                    } else {
                        // copy the next right element to the result
                        result[resultIndex++] = right[rightIndex++];
                    }
                } else if (leftIndex < left.length) {
                    // copy the rest of the left half to the result
                    int leftRemaining = left.length - leftIndex;
                    System.arraycopy(left, leftIndex, result, resultIndex, leftRemaining);
                    leftIndex += leftRemaining;
                    resultIndex += leftRemaining;
                } else {
                    // copy the rest of the right half to the result
                    int rightRemaining = right.length - rightIndex;
                    System.arraycopy(right, rightIndex, result, resultIndex, rightRemaining);
                    rightIndex += rightRemaining;
                    resultIndex += rightRemaining;
                }
            }
        }

        private int compareElements(Comparator<? super E> comparator, E firstElement, E secondElement) {
            int comparison;
                if (comparator != null) {
                comparison = comparator.compare(firstElement, secondElement);
            } else {
                @SuppressWarnings("unchecked")
                Comparable<E> comparable = (Comparable<E>) firstElement;  // may throw ClassCastException
                comparison = comparable.compareTo(secondElement);
            }
            return comparison;
        }

    }

}
