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
import craterdog.utils.NaturalComparator;
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
        if (collection != null && collection.getNumberOfElements() > 1) {

            // make sure the comparator exists
            if (comparator == null) comparator = new NaturalComparator<>();

            // convert the collection to an array
            E[] array = collection.toArray();

            // sort the array
            sortArray(array, comparator);

            // convert it back to a collection
            collection.removeAllElements();
            collection.addElements(array);

        }
    }


    private void sortArray(E[] array, Comparator<? super E> comparator) {
        // check to see if the array is already sorted
        int length = array.length;
        if (length < 2) return;

        // split the array into two halves
        int leftLength = length / 2;
        E[] left = Arrays.copyOfRange(array, 0, leftLength);
        E[] right = Arrays.copyOfRange(array, leftLength, length);

        // sort each half separately
        sortArray(left, comparator);
        sortArray(right, comparator);

        // merge the sorted halves back together
        mergeArrays(left, right, array, comparator);
    }


    private void mergeArrays(E[] left, E[] right, E[] result, Comparator<? super E> comparator) {
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
