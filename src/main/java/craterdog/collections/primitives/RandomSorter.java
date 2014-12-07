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
import craterdog.collections.interfaces.Indexed;
import craterdog.utils.RandomUtils;
import java.util.Comparator;


/**
 * This sorter class implements a randomizing sort algorithm.  The collection to be sorted
 * is randomly reordered such that the resulting order is completely random.
 *
 * @author Derk Norton
 * @param <E> The type of element being sorted.
 */
public class RandomSorter<E> extends Sorter<E> {

    @Override
    public void sortCollection(SortableCollection<E> collection, Comparator<? super E> comparator) {
        // see if any sorting is really required
        if (collection != null && collection.getNumberOfElements() > 1) {
            if (collection instanceof Indexed) {
                // randomize it in place
                @SuppressWarnings("unchecked")
                Indexed<E> indexedCollection = (Indexed<E>) collection;
                int size = collection.getNumberOfElements();
                randomizeIndexedCollection(indexedCollection, size);
            } else {
                // convert the collection to an array
                E[] array = collection.toArray();

                // randomize the array
                randomizeArray(array);

                // convert it back to a collection
                collection.removeAllElements();
                collection.addElements(array);
            }
        }
    }

    private void randomizeIndexedCollection(Indexed<E> indexedCollection, int size) {
        for (int index = size; index > 1; index--) {
            int randomIndex = RandomUtils.pickRandomIndex(index) + 1;  // use ordinal based indexing
            E swap = indexedCollection.getElementAtIndex(index);
            swap = indexedCollection.replaceElementAtIndex(swap, randomIndex);
            indexedCollection.replaceElementAtIndex(swap, index);
        }
    }

    private void randomizeArray(E[] array) {
        int size = array.length;
        for (int index = size; index > 1; index--) {
            int randomIndex = RandomUtils.pickRandomIndex(index);  // use zero based indexing
            E swap = array[index - 1];
            array[index - 1] = array[randomIndex];
            array[randomIndex] = swap;
        }
    }

}
