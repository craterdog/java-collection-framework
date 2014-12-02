/************************************************************************
 * Copyright (c) Crater Dog Technologies(TM).  All Rights Reserved.     *
 ************************************************************************
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.        *
 *                                                                      *
 * This code is free software; you can redistribute it and/or modify it *
 * under the terms of The MIT License (MIT), as published by the Open   *
 * Source Initiative. (See http://opensource.org/licenses/MIT)          *
 ************************************************************************/
package craterdog.collections.primitives;

import craterdog.collections.abstractions.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;


/**
 * This sorter class implements a standard merge sort algorithm.  The collection to be sorted
 * is recursively split into two collections each of which are then sorted and then the two
 * collections are merged back into a sorted collection.
 *
 * @author Derk Norton
 * @param <E> The type of element being sorted.
 */
public class MergeSorter<E> extends Sorter<E> {

    @Override
    public void sortCollection(SortableCollection<E> collection, Comparator<? super E> comparator) {
        // see if any sorting is needed
        int size;
        if (collection != null && (size = collection.getNumberOfElements()) > 1) {

            // make sure the comparator exists
            if (comparator == null) comparator = new NaturalComparator<>();

            // convert the collection to an array
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
                if (comparator.compare(left[leftIndex], right[rightIndex]) < 0) {
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

}
